package com.yihu.base.security.rbas;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by Administrator on 2018/4/3 0003.
 * 通过username获取user信息（可写死可数据库查找）
 */
public interface UserServiceProvider extends UserDetailsService {
}
