//实现接口

package com.agatha.agatha.repository;

import java.util.List;
import com.agatha.agatha.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    //添加查询方法
    List<User> findByUsernameContainingOrEmailContaining(String username, String email);
    //用于通过用户名或邮箱进行模糊查询
}
