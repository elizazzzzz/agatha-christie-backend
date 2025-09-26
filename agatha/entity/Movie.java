package com.agatha.agatha.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie extends Media {//继承Media类
    // 默认构造函数（JPA要求）
    public Movie() {
        super("", 0);
    }
    //构造方法
    public Movie(String title, int durationInMinutes) {
        super(title, durationInMinutes);//调用父类的构造方法,super()
    }
    //实现抽象方法
    @Override//重写父类的抽象方法
    public void play() {
        System.out.println("正在播放电影：" + getTitle());
        System.out.println("电影时长：" + getDurationInMinutes() + " 分钟");
        System.out.println("电影已开始，请欣赏电影！");
    }
}
