package com.agatha.agatha.repository;

import com.agatha.agatha.entity.Novel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovelRepository extends JpaRepository<Novel, Long> {

    // 这个方法是 Spring Data JPA 命名查询，它会根据方法名自动生成 SQL
    // 它可以同时对 title, characters, 和 description 三个字段进行模糊匹配，并且不区分大小写
    // 你的 NovelServiceImp.java 中已经调用了此方法，因此必须在这里声明它
    List<Novel> findByTitleContainingIgnoreCaseOrCharactersContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title, String characters, String description);
}