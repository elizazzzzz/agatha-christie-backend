package com.agatha.agatha.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "tv_series")
public class TVSeries extends Media {//继承Media类
    private int episodes;//访问限制符，集数
    // 默认构造函数（JPA要求）
    public TVSeries() {
        super("", 0);
    }
    //构造方法
    public TVSeries(String title,int durationInMinutes,int episodes){
        super(title,durationInMinutes);//调用父类的构造方法
        this.episodes=episodes;//初始化episodes字段
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }


    //重写父类的displayInfo方法并增加信息。父类Media中的displayInfo()方法已经实现，这里只需要在子类中添加自己的逻辑。
    @Override
    public void displayInfo(){
        super.displayInfo();//调用父类的displayInfo()方法
        System.out.println("集数: " + episodes);//输出集数
    }
    @Override//重写父类的抽象方法
    public void play(){
        System.out.println("正在播放电视剧：《" + getTitle()+"》");
        System.out.println("电视剧时长：" + getDurationInMinutes() + " 分钟");
        System.out.println("请注意，本剧共有"+ episodes + "集");
    }

}
