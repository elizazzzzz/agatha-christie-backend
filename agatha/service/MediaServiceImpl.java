//实现类，提供抽象方法的具体实现
//应用接口规则：实现接口
package com.agatha.agatha.service;

//引入spring框架.模式.表明这个类是一个服务组件
import com.agatha.agatha.entity.Movie;
import com.agatha.agatha.entity.TVSeries;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import com.agatha.agatha.repository.MovieRepository;
import com.agatha.agatha.repository.TVSeriesRepository;

@Service//创建服务组件,让Spring创建并管理它的实例
public class MediaServiceImpl implements MediaService {
    //声明 final 字段，MovieRepository和TVSeriesRepository用来与数据库交互，数据仓库
    private final MovieRepository movieRepository;
    private final TVSeriesRepository tvSeriesRepository;

    //构造函数来注入Repository
    public MediaServiceImpl(MovieRepository movieRepository, TVSeriesRepository tvSeriesRepository) {
        this.movieRepository = movieRepository;
        this.tvSeriesRepository = tvSeriesRepository;
    }

    @Override//实现从数据库获取流媒体逻辑
    public Object getMediaById(Long id) {
        // 先在Movie中查找
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie != null) {
            return movie;
        }
        // 再在TVSeries中查找
        TVSeries tvSeries = tvSeriesRepository.findById(id).orElse(null);
        return tvSeries;
    }

    @Override//实际从数据库获取所有媒体的逻辑
    public List<Object> getAllMedia() {
        List<Object> allMedia = new ArrayList<>();
        allMedia.addAll(movieRepository.findAll());
        allMedia.addAll(tvSeriesRepository.findAll());
        return allMedia;
    }

    @Override//实际保存媒体逻辑
    public Object createMedia(Object media) {
        // 这里需要根据传入的对象类型来决定保存到哪个Repository
        if (media instanceof Movie) {
            return movieRepository.save((Movie) media);
        } else if (media instanceof TVSeries) {
            return tvSeriesRepository.save((TVSeries) media);
        }
        return null;
    }

    @Override//更新 mediaDetails 中的字段
    public Object updateMedia(Long id, Object mediaDetails) {
        if (mediaDetails instanceof Movie) {
            Movie movie = movieRepository.findById(id).orElse(null);
            if (movie != null) {
                Movie updatedMovie = (Movie) mediaDetails;
                movie.setTitle(updatedMovie.getTitle());
                movie.setDurationInMinutes(updatedMovie.getDurationInMinutes());
                return movieRepository.save(movie);
            }
        } else if (mediaDetails instanceof TVSeries) {
            TVSeries tvSeries = tvSeriesRepository.findById(id).orElse(null);
            if (tvSeries != null) {
                TVSeries updatedTVSeries = (TVSeries) mediaDetails;
                tvSeries.setTitle(updatedTVSeries.getTitle());
                tvSeries.setDurationInMinutes(updatedTVSeries.getDurationInMinutes());
                tvSeries.setEpisodes(updatedTVSeries.getEpisodes());
                return tvSeriesRepository.save(tvSeries);
            }
        }
        return null;
    }

    @Override//删除媒体逻辑
    public void deleteMedia(Long id) {
        // 尝试从Movie中删除
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return;
        }
        // 尝试从TVSeries中删除
        if (tvSeriesRepository.existsById(id)) {
            tvSeriesRepository.deleteById(id);
        }
    }

    @Override
    public Void playMedia(Long id) {
        Object media = getMediaById(id);
        if (media != null) {
            if (media instanceof Movie) {
                Movie movie = (Movie) media;
                movie.setPlaying(true);
                movieRepository.save(movie);
                System.out.println("正在播放电影: " + movie.getTitle());
            } else if (media instanceof TVSeries) {
                TVSeries tvSeries = (TVSeries) media;
                tvSeries.setPlaying(true);
                tvSeriesRepository.save(tvSeries);
                System.out.println("正在播放电视剧: " + tvSeries.getTitle());
            }
        }
        return null;
    }
}
