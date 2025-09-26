//对外暴露搜索API（eg.GET /api/novels/search?keyword=xxx,返回List<Novel>包含id,title,description,characters,coverUrl。GET /api/novels/{id} → 返回单本小说详情（含 contentPath，用于"查看正文"）
package com.agatha.agatha.controller;

import com.agatha.agatha.dto.ChapterInfo;

import com.agatha.agatha.entity.Chapter;
import com.agatha.agatha.entity.Novel;
import com.agatha.agatha.service.NovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Comparator;
import java.io.InputStream;


@RestController
@RequestMapping("/api/novels")
public class NovelController {

    @Autowired
    private NovelService novelService;

    @PostMapping
    public ResponseEntity<Novel> createNovel(@RequestBody Novel novel) {
        Novel createdNovel = novelService.saveNovel(novel);
        return new ResponseEntity<>(createdNovel, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Novel>> batchCreateNovels(@RequestBody List<Novel> novels) {
        List<Novel> createdNovels = novels.stream()
                .map(novelService::saveNovel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(createdNovels, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Novel> getAllNovels() {
        return novelService.searchNovels(null); // keyword 为空时返回所有
    }

    /**
     * 根据 ID 获取小说详情
     * 示例：GET /api/novels/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Novel> getNovelById(@PathVariable Long id) {
        Optional<Novel> novel = novelService.getNovelById(id);
        return novel.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public List<Novel> searchNovels(@RequestParam(required = false) String keyword) {
        return novelService.searchNovels(keyword);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Novel> updateNovel(@PathVariable Long id, @RequestBody Novel novelDetails) {
        Optional<Novel> novel = novelService.getNovelById(id);
        if (novel.isPresent()) {
            novelDetails.setId(id);
            Novel updatedNovel = novelService.saveNovel(novelDetails);
            return new ResponseEntity<>(updatedNovel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNovel(@PathVariable Long id) {
        Optional<Novel> novel = novelService.getNovelById(id);
        if (novel.isPresent()) {
            novelService.deleteNovel(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * 根据小说ID获取小说内容
     * 支持两种存储方式：文件系统存储和数据库存储
     *
     * @param id 小说ID
     * @return 小说内容文本
     */

    @GetMapping("/{id}/content")
    public ResponseEntity<String> getNovelContent(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,// 当前页码
            @RequestParam(defaultValue = "1000") int pageSize) {// 每页大小

        return novelService.getNovelById(id)
                .map(novel -> {
                    try {
                        String content = null;

                        if ("DATABASE".equalsIgnoreCase(novel.getStorageType()) && novel.getContent() != null) {
                            // 数据库存储
                            content = novel.getContent();
                        } else if (novel.getContentPath() != null) {
                            // 文件存储
                            Path path = Paths.get(novel.getContentPath());
                            if (!Files.exists(path)) {
                                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("文件不存在");
                            }
                            content = Files.readString(path, StandardCharsets.UTF_8);
                        }

                        if (content == null) {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("小说内容未找到");
                        }

                        // 分页逻辑
                        int start = (page - 1) * pageSize;
                        int end = Math.min(start + pageSize, content.length());

                        if (start >= content.length()) {
                            return ResponseEntity.ok(""); // 没有更多内容
                        }

                        return ResponseEntity.ok(content.substring(start, end));

                    } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("文件读取失败: " + e.getMessage());
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("小说不存在"));
    }

    /**
     * 根据小说ID获取章节列表
     *
     * @param id 小说ID
     * @return 章节列表
     */
    // 在 NovelController 中添加日志
    @GetMapping("/{id}/chapters")
    public ResponseEntity<List<ChapterInfo>> getNovelChapters(@PathVariable Long id) {
        Optional<Novel> optionalNovel = novelService.getNovelById(id);
        if (optionalNovel.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            List<ChapterInfo> chapters = novelService.loadChaptersForNovel(optionalNovel.get());
            return ResponseEntity.ok(chapters);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/chapters/{chapterNumber}")
    public ResponseEntity<Map<String, Object>> getChapterContent(@PathVariable Long id, @PathVariable Integer chapterNumber) {
        Optional<Novel> optionalNovel = novelService.getNovelById(id);
        if (optionalNovel.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            ChapterInfo chapter = novelService.getChapter(optionalNovel.get(), chapterNumber);
            if (chapter == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            Map<String, Object> resp = new HashMap<>();
            resp.put("chapterNumber", chapter.getChapterNumber());
            resp.put("title", chapter.getTitle());
            resp.put("content", chapter.getContent());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 从文件中提取章节的方法
    private List<ChapterInfo> extractChaptersFromFile(Novel novel) throws IOException {
        List<ChapterInfo> chapters = new ArrayList<>();

        String cp = novel.getContentPath();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        // normalize path token
        String token = (cp == null) ? "" : cp.trim();
        if (token.startsWith("classpath:")) token = token.substring("classpath:".length());
        if (token.startsWith("/")) token = token.substring(1); // remove leading slash

        // 修复：如果路径包含本地磁盘路径，提取文件名部分
        if (token.contains(":\\") || token.contains(":\\")) {
            // 处理类似 D:\AgathaChristie\novels\1.txt(东方快车谋杀案).txt 的路径
            int lastSlash = token.lastIndexOf('\\');
            if (lastSlash != -1) {
                token = token.substring(lastSlash + 1);
            }
            // 进一步提取实际文件名，去除括号部分
            int bracketStart = token.indexOf('(');
            int bracketEnd = token.indexOf(')');
            if (bracketStart != -1 && bracketEnd != -1 && bracketEnd > bracketStart) {
                token = token.substring(bracketStart + 1, bracketEnd);
            } else if (token.contains(".txt")) {
                token = token.substring(0, token.lastIndexOf(".txt"));
            }
        }

        // 1) If contentPath ends with .txt -> try to treat it as a single file resource
        if (token.toLowerCase().endsWith(".txt")) {
            String[] tryPatterns = new String[] {
                    "classpath*:static/AgathaChristie/" + token,
                    "classpath*:static/AgathaChristie/novels/" + token,
                    "classpath*:novels/" + token,
                    "classpath*:" + token
            };
            for (String pat : tryPatterns) {
                Resource[] res = resolver.getResources(pat);
                if (res != null && res.length > 0) {
                    // take first match, split into chapters
                    return splitBookResourceToChapters(res[0]);
                }
            }
        }

        // 2) If token is probably a folder/slug -> try to read all .txt in that folder
        if (!token.isEmpty()) {
            String[] folderPatterns = new String[] {
                    "classpath*:static/AgathaChristie/novels/" + token + "/*.txt",
                    "classpath*:novels/" + token + "/*.txt",
                    "classpath*:static/novels/" + token + "/*.txt"
            };
            for (String pat : folderPatterns) {
                Resource[] res = resolver.getResources(pat);
                if (res != null && res.length > 0) {
                    Arrays.sort(res, Comparator.comparing(Resource::getFilename, Comparator.nullsLast(String::compareTo)));
                    int idx = 1;
                    for (Resource r : res) {
                        try (InputStream is = r.getInputStream()) {
                            String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                            String title = getFirstLine(text);
                            chapters.add(new ChapterInfo(idx++, title, text));
                        }
                    }
                    return chapters;
                }
            }
        }

        // 3) Fallback: try to fuzzy-match filenames under known novels directories
        Resource[] allStatic = resolver.getResources("classpath*:static/AgathaChristie/novels/*.txt");
        String tokenClean = token.replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5]", "").toLowerCase();
        for (Resource r : allStatic) {
            String fn = r.getFilename();
            if (fn == null) continue;
            String fnClean = fn.replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5]", "").toLowerCase();
            if (!tokenClean.isEmpty() && fnClean.contains(tokenClean)) {
                return splitBookResourceToChapters(r);
            }
        }

        // 4) Last resort: try files in static/novels (other folder)
        Resource[] alt = resolver.getResources("classpath*:static/novels/*.txt");
        for (Resource r : alt) {
            String fn = r.getFilename();
            if (fn == null) continue;
            String fnClean = fn.replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5]", "").toLowerCase();
            if (!tokenClean.isEmpty() && fnClean.contains(tokenClean)) {
                return splitBookResourceToChapters(r);
            }
        }

        // Couldn't find any matching resource
        throw new IOException("未找到匹配的小说资源，contentPath=" + novel.getContentPath());
    }

    // helper: read a single resource (full book) and split into chapters (marker / regex)
    private List<ChapterInfo> splitBookResourceToChapters(Resource res) throws IOException {
        try (InputStream is = res.getInputStream()) {
            String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return splitChaptersFromText(text);
        }
    }

    private List<ChapterInfo> splitChaptersFromText(String text) {
        List<ChapterInfo> chapters = new ArrayList<>();
        if (text == null || text.isBlank()) return chapters;

        // Prefer explicit marker (easy to split)
        if (text.contains("CHAPTER_START::")) {
            String[] parts = text.split("CHAPTER_START::");
            int idx = 1;
            for (int i = 1; i < parts.length; i++) {
                String block = parts[i].trim();
                if (block.isEmpty()) continue;
                String title = block.split("\\r?\\n", 2)[0].trim();
                chapters.add(new ChapterInfo(idx++, title, block));
            }
            return chapters;
        }

        // Otherwise detect chapter headings by regex (中文/英文)
        Pattern p = Pattern.compile("(?m)^(第\\s*\\d+\\s*章.*|CHAPTER\\s+\\d+.*)$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);

        List<Integer> starts = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        while (m.find()) {
            starts.add(m.start());
            titles.add(m.group().trim());
        }

        if (starts.isEmpty()) {
            // no chapters found -> treat whole text as one chapter
            chapters.add(new ChapterInfo(1, "全文", text.trim()));
            return chapters;
        }

        // if there's some preface before first chapter, keep it as "前言"
        if (starts.get(0) > 0) {
            String preface = text.substring(0, starts.get(0)).trim();
            if (!preface.isEmpty()) {
                chapters.add(new ChapterInfo(0, "前言", preface));
            }
        }

        for (int i = 0; i < starts.size(); i++) {
            int s = starts.get(i);
            int e = (i + 1 < starts.size()) ? starts.get(i + 1) : text.length();
            String block = text.substring(s, e).trim();
            String title = titles.get(i);
            chapters.add(new ChapterInfo(i + 1, title, block));
        }

        return chapters;
    }

    private String getFirstLine(String text) {
        if (text == null) return "";
        String[] lines = text.split("\\r?\\n", 2);
        return lines.length > 0 ? lines[0].trim() : "";
    }

}
