package com.agatha.agatha.service;

import com.agatha.agatha.entity.Novel;
import com.agatha.agatha.entity.Chapter;
import java.util.List;
import java.util.Optional;

import com.agatha.agatha.dto.ChapterInfo;
import com.agatha.agatha.entity.Novel;

import java.io.IOException;
import java.util.List;

public interface NovelService {
    List<Novel> searchNovels(String keyword);
    Optional<Novel> getNovelById(Long id);
    Novel saveNovel(Novel novel);
    void deleteNovel(Long id);

//新增章节
List<ChapterInfo> loadChaptersForNovel(Novel novel) throws IOException;
    ChapterInfo getChapter(Novel novel, int chapterNumber) throws IOException;}
