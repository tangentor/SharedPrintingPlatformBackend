package org.swunlp.printer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表(User)实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("t_user")
public class User implements Serializable {
	private static final long serialVersionUID = -40356785423868312L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 用户角色id
	 */
	private String rid;
	/**
	 * 账号状态（0正常 1停用）
	 */
	private String status;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;


	@TableField(value = "name",exist = false)
	private String roleName;
//	private Role role;

	private String openid;
}
