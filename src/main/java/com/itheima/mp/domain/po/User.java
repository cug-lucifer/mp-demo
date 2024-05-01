package com.itheima.mp.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    public Long id;

    /**
     * 用户名
     */
    @TableField("`username`")
    public String username;

    /**
     * 密码
     */
    public String password;

    /**
     * 注册手机号
     */
    public String phone;

    /**
     * 详细信息
     */
    public String info;

    /**
     * 使用状态（1正常 2冻结）
     */
    public Integer status;

    /**
     * 账户余额
     */
    public Integer balance;

    /**
     * 创建时间
     */
    public LocalDateTime createTime;

    /**
     * 更新时间
     */
    public LocalDateTime updateTime;
}
