package com.agatha.agatha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.agatha.agatha.service.MediaService;
import com.agatha.agatha.entity.Movie;
import com.agatha.agatha.entity.TVSeries;
import java.util.List;


@RestController//注解，这个类是一个特殊控制器，将处理RESTfFallAPI请求并自动将方法返回的对象转换成JSON/xml的格式给前端
@RequestMapping("/api/media")//定义这个控制器中所有方法的基准URL路径，控制器里所有方法都以/api/media开头
public class MediaController {
    private final MediaService mediaService;//依赖注入MediaService接口
    //final意味着mediaService字段被赋值后，不再改变，确保引用不变

    @Autowired//注解，依赖注入，告诉Spring需要一个MediaService实例，并自动将这个实例注入到这个类中，Spring将再对象池（Ioc容器中找到MediaService的一个实现类（MediaServiceImpl），然后将这个实例自动转给MediaController的构造函数）
    public MediaController(MediaService mediaService) {//构造函数注入，在MediaController被创建时执行，@Autowired注解告诉Spring将这个实例注入到这个类中，好处在于能保证mediaService字段总是在对象创建时被初始化且final，提高健壮性
        this.mediaService = mediaService;
    }

    @GetMapping("/{id}")//将与@RequestMapping（”/api/media“）组合，形成完整的URL路径，如/api/media/{id}
    public Object getMediaById(@PathVariable Long id) {//方法签名：公开方法，返回Object对象，名字叫getMediaById()，参数是Long类型的id
        return mediaService.getMediaById(id);//调用MediaService的方法，返回Object对象
    }//控制器通过接口调用服务方法，不关心具体实现。

    @GetMapping//获取所有媒体
    public Object getAllMedia() {
        return mediaService.getAllMedia();
    }

    @PostMapping("/movies")//创建电影
    public Object createMovie(@RequestBody Movie movie) {
        return mediaService.createMedia(movie);
    }

    @PostMapping("/tvseries")//创建电视剧
    public Object createTVSeries(@RequestBody TVSeries tvSeries) {
        return mediaService.createMedia(tvSeries);
    }

    @PutMapping("/{id}")
    public Object updateMedia(@PathVariable Long id, @RequestBody Object mediaDetails) {
        return mediaService.updateMedia(id, mediaDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
    }

    @PostMapping("/{id}/play")
    public void playMedia(@PathVariable Long id) {
        mediaService.playMedia(id);
    }
}
