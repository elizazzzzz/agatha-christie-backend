package com.agatha.agatha.repository;

import com.agatha.agatha.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface ChapterRepository  extends JpaRepository<Chapter,Long>{
    //根据小说ID获取所有章节（仅目录信息）
    List<Chapter> findByNovelIdOrderByChapterNumber(Long novelId);
    //根据章节ID获取章节内容
    Optional<Chapter> findByNovelIdAndChapterNumber(Long novelId, Integer chapterNumber);
    //根据章节ID获取章节信息
    boolean existsByNovelId(Long novelId);

}
