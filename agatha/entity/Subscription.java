//订阅实体（包含 email, subscribedAt, status, source 等字段）
//使用JPA和Lombok的实体类，用于映射subscription表
package com.agatha.agatha.entity;//定义类的包名

import jakarta.persistence.*;//引入JPA的注解比如@Entity等，用于将java类映射到数据库表
import lombok.Data;
import lombok.NoArgsConstructor;//都是lombok的注解，自动生成getter等方法、自动生产无参构造函数，JPA要求实体类必须有一个无参构造器
import java.time.LocalDateTime;

@Data//lombok注解
@NoArgsConstructor//无参构造
@Entity//JPA实体类，会被映射到数据库中的一张表
@Table(name = "subscriptions")//告诉JPA在数据库中创建一个名为subscription的表
public class Subscription {//定义一个表叫subscription，对应数据库的一条记录
   //定义枚举类型
    public enum SubscriptionStatus {
       ACTIVE,//正常订阅
       CANCELLED,//用户取消
       EXPIRED//已过期，且所有deng枚举常量必须大写
   }


    @Id//主键字段
    @GeneratedValue(strategy = GenerationType.IDENTITY)//主键生产策略为IDENTITY用于数据库自动递增主键
    private Long id;//主键字段类型

    @Column(name = "name")//将name字段映射到数据库表中的name列
    private String name;//用户名字

    @Column(name = "email",/*映射到email列*/nullable = false/*不能为空*/,unique = true)//确保每个邮箱地址在数据库中都是唯一的，放置用户重复订阅
    private String email;//用户邮箱地址
    //订阅时间
    @Column(name = "subscribed_at"/*映射新表，用于统计订阅趋势*/, nullable = false,updatable = false)
    private LocalDateTime subscribedAt = LocalDateTime.now();

    //订阅状态（ACTIVE,CANCELLED,EXPIRED等）
    @Enumerated(EnumType.STRING)//用来指定枚举类型在数据库中的存储方式,把枚举的名字存到数据库中
    @Column(name = "status",nullable = false)
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    //订阅来源
    @Column(name = "source")
    private String source;
}
