package com.agatha.agatha.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Generated;


@MappedSuperclass
public abstract class Media {//公开定义一个抽象类Media

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;//private访问权限限制符，封装，只能类内部访问
    private int durationInMinutes;//String、int均为数据类型，定义了变量可以存储什么样的数据
    private boolean isPlaying;//播放状态
    public Media() {}
    //构造函数：public 类名（）{}

    public Media(String title, int durationInMinutes) {
        this.title = title;
        this.durationInMinutes = durationInMinutes;//初始化字段
        this.isPlaying = false;//初始状态为不播放
    }
    //非抽象方法（具体方法），提供通用功能，因为不管是任何流媒体，对于标题和持续时间都是需要的，用getter和setter方法访问private字段

    @Generated
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Generated
    public String getTitle(){//getter方法体
        return title;
    }
    @Generated
    public int getDurationInMinutes(){//getter方法体
        return durationInMinutes;
    }

    public void setTitle(String title) {//setter方法体
        this.title = title;
    }

    public void setDurationInMinutes(int durationInMinutes) {//setter方法体
        this.durationInMinutes = durationInMinutes;
    }
    public void setPlaying(boolean playing){
        this.isPlaying = playing;
    }
    //displayInfo的意思：显示信息，void 表示没有返回值
    public void displayInfo(){//显示信息方法体，此类方法属于非抽象方法，在抽象类Media中有实现，任何继承子类都可以调用，在后面可以实现代码复用
        //代码复用，将相同的功能封装在抽象类Media中，子类继承抽象类Media，并实现抽象方法，从而实现代码复用。还可以利用super.displayInfo()方法，调用抽象类Media中的displayInfo()方法并且可以再添加自己的逻辑。
        System.out.println("标题: " + title);
        System.out.println("持续时间: " + durationInMinutes + " 分钟");
        //没有返回值就意味着直接将信息打印到控制台
    }
    public abstract void play();//抽象类方法，只有方法签名，没有方法体，但强制性所有子类必须实现，也就是都必须可以播放。
}

