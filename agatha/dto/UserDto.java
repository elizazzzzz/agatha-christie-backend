//用户DTO(Data Transfer Object)用于敏感数据加密,不返回密码
package com.agatha.agatha.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
}
