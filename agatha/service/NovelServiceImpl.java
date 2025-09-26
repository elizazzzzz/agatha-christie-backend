package com.agatha.agatha.service;

import com.agatha.agatha.dto.ChapterInfo;
import com.agatha.agatha.entity.Novel;
import com.agatha.agatha.repository.NovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class NovelServiceImpl implements NovelService {

    @Autowired
    private NovelRepository novelRepository;

    // cache keyed by bookSlug (可以是 novel.getContentPath() 或者自己约定的 slug)
    private final ConcurrentHashMap<String, List<ChapterInfo>> cache = new ConcurrentHashMap<>();

    @Override
    public List<Novel> searchNovels(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return novelRepository.findAll();
        }
        return novelRepository.findByTitleContainingIgnoreCaseOrCharactersContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                keyword, keyword, keyword
        );
    }

    @Override
    public Novel saveNovel(Novel novel) {
        return novelRepository.save(novel);
    }

    @Override
    public Optional<Novel> getNovelById(Long id) {
        return novelRepository.findById(id);
    }

    @Override
    public void deleteNovel(Long id) {
        novelRepository.deleteById(id);
    }

    @Override
    public List<ChapterInfo> loadChaptersForNovel(Novel novel) throws IOException {
        // 从 Novel 获取书的 slug 或 folder 名。请保证你的 Novel.contentPath（或你用的字段）存储了该 folder 名
        String bookSlug = novel.getContentPath();
        if (bookSlug == null || bookSlug.trim().isEmpty()) {
            // 回退：用 id 作为 slug（前提是你在 resources/novels/ 下放了以 id 命名的文件夹）
            bookSlug = "novel-" + novel.getId();
        }
        
        // 提取文件夹名称（处理完整路径的情况）
        if (bookSlug.contains("\\")) {
            // Windows路径
            bookSlug = bookSlug.substring(bookSlug.lastIndexOf('\\') + 1);
        } else if (bookSlug.contains("/")) {
            // Unix路径
            bookSlug = bookSlug.substring(bookSlug.lastIndexOf('/') + 1);
        }
        
        bookSlug = bookSlug.trim();

        // 如果缓存中有，直接返回
        if (cache.containsKey(bookSlug)) {
            return cache.get(bookSlug);
        }

        // 在 classpath 内找文件：src/main/resources/novels/<bookSlug>/*.txt
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 注意 pattern 前缀用 "classpath*:"，在 jar 和 IDE 下都能找到
        String pattern = "classpath*:novels/" + bookSlug + "/*.txt";
        Resource[] resources = resolver.getResources(pattern);

        if (resources == null || resources.length == 0) {
            // 没有找到文件，抛出异常以便 controller 返回 500 或者可返回空列表
            throw new IOException("未在 classpath 找到任何章节文件: pattern=" + pattern);
        }

        // 按文件名排序（001.txt, 002.txt ...）
        Arrays.sort(resources, Comparator.comparing(Resource::getFilename, Comparator.nullsLast(String::compareTo)));

        List<ChapterInfo> chapters = new ArrayList<>();
        int idx = 1;
        for (Resource r : resources) {
            try (InputStream is = r.getInputStream()) {
                byte[] data = is.readAllBytes();
                String text = new String(data, StandardCharsets.UTF_8).trim();
                // 默认第一行作为标题
                String[] lines = text.split("\\r?\\n", 2);
                String title = lines.length > 0 ? lines[0].trim() : "章节 " + idx;
                chapters.add(new ChapterInfo(idx, title, text));
                idx++;
            }
        }

        cache.put(bookSlug, chapters);
        return chapters;
    }

    @Override
    public ChapterInfo getChapter(Novel novel, int chapterNumber) throws IOException {
        List<ChapterInfo> list = loadChaptersForNovel(novel);
        if (chapterNumber <= 0 || chapterNumber > list.size()) {
            return null;
        }
        return list.get(chapterNumber - 1);
    }
}