package com.agatha.agatha.dto;

public class ChapterInfo {
    private Integer chapterNumber;
    private String title;
    private String content;

    public ChapterInfo() {}

    public ChapterInfo(Integer chapterNumber, String title, String content) {
        this.chapterNumber = chapterNumber;
        this.title = title;
        this.content = content;
    }

    public Integer getChapterNumber() { return chapterNumber; }
    public void setChapterNumber(Integer chapterNumber) { this.chapterNumber = chapterNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
