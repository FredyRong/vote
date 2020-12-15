package com.fredy.vote.server.service;

import com.fredy.vote.model.entity.User;
import com.fredy.vote.model.entity.UserExample;
import com.fredy.vote.model.mapper.UserMapper;
import com.fredy.vote.server.cache.PermissionCache;
import com.fredy.vote.server.enums.SysConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户自定义的realm-用户shiro的认证、授权
 * @author Fredy
 * @date 2020-12-14 14:16
 */
@Slf4j
public class CustomRealm extends AuthorizingRealm {

    private static final Long SESSION_KEY_TIMEOUT = 3600_000L;

    @Resource
    private UserMapper userMapper;


    /**
     * @Description: 授权
     * @Author: Fredy
     * @Date: 2020-12-14
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        if(user.getType() == SysConstant.UserType.ADMIN.getCode()) {
            info.setStringPermissions(PermissionCache.adminPermissions());
        }

        return info;
    }

    /**
     * @Description: 认证-登录
     * @Author: Fredy
     * @Date: 2020-12-14
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        String password = String.valueOf(token.getPassword());
        log.info("当前登录的用户名={} 密码={} ",userName,password);

        // 查找数据库
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andUsernameEqualTo(userName);
        List<User> userList = userMapper.selectByExample(userExample);

        // 校验
        if(CollectionUtils.isEmpty(userList)) {
            throw new UnknownAccountException("用户名不存在！");
        }
        User user = userList.get(0);
        if(!user.getPassword().equals(password)) {
            throw new IncorrectCredentialsException("用户名密码不匹配！");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getUsername(), password, getName());
        setSession("user", user);

        return info;
    }


    /**
     * @Description: 将key与对应的value塞入shiro的session中，最终交给HttpSession进行管理
     * @Author: Fredy
     * @Date: 2020-12-14
     */
    private void setSession(String key, Object value) {
        Session session = SecurityUtils.getSubject().getSession();
        if (session != null) {
            session.setAttribute(key, value);
            session.setTimeout(SESSION_KEY_TIMEOUT);
        }
    }
}
