package com.agatha.agatha.repository;

import com.agatha.agatha.entity.TVSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TVSeriesRepository extends JpaRepository<TVSeries, Long> {
    List<TVSeries> findByTitleContainingIgnoreCase(String title);
}
