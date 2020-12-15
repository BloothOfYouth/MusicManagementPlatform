package com.hjf.music.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hjf.music.dao.AdminDao;
import com.hjf.music.entity.Admin;
import com.hjf.music.utils.JWTUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author hjf
 */
@Component
public class AdminRealm extends AuthorizingRealm {

    @Autowired
    private AdminDao adminDao;

    /**
     * 为了让realm支持jwt的凭证校验
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证
     * 登录认证校验
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();

        // 解密获得username，用于和数据库进行对比
        if (JWTUtils.getDecodedJWT(token) == null) {
            throw new AuthenticationException("token认证失败！");
        }
        String username = JWTUtils.getDecodedJWT(token).getClaim("username").asString();
        if (username == null || !JWTUtils.verify(token, username)) {
            throw new AuthenticationException("token认证失败！");
        }
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        Admin admin = adminDao.selectOne(queryWrapper);
        if (admin == null) {
            throw new AuthenticationException("该用户不存在！");
        }
        return new SimpleAuthenticationInfo(token, token, "MyRealm");
    }
}
