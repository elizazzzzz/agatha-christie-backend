package com.agatha.agatha.entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "chapters")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "novel_id")
    private Long novelId;//关联的小说ID

    @NotNull
    @Column(name = "chapter_number")
    private Integer chapterNumber;//章节序号

    @NotBlank
    private String title;//章节标题

    @Column(columnDefinition = "LONGTEXT")
    private String content;//章节内容
}
