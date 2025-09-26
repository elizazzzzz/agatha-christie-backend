//小说实体

package com.agatha.agatha.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

@Data
@Entity
public class Novel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//主键

    @NotBlank(message = "书名不能为空")
    private String title;//书名

    @Column(columnDefinition = "TEXT")
    private String characters;//角色名
    private String description;//简介
    private String coverUrl;//封面照片路径/URL，映射前端img src
    // 文件系统存储方式：存储小说TXT文件的路径
    private String contentPath;//存正文文件路径
    // 数据库存储方式：直接存储小说内容
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    // 用于统计信息
    @Column(name = "total_pages")
    private Integer totalPages;

    @Column(name ="word_count")
    private Long wordCount;
    // 存储类型标识：区分内容存储在文件系统("FILE")还是数据库("DATABASE")
    @Column(name ="storage_type")
    private String storageType;
}

