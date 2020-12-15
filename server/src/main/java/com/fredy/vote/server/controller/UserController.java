package com.fredy.vote.server.controller;

import com.fredy.vote.api.enums.StatusCode;
import com.fredy.vote.api.response.BaseResponse;
import com.fredy.vote.server.dto.LoginDto;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fredy
 * @date 2020-12-14 14:41
 */
@RequestMapping("/user")
@Log4j2
@Controller
public class UserController {

    @Resource
    private Environment env;


    /**
     * @Description: 登录接口
     * @Author: Fredy
     * @Date: 2020-12-14
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult) {
        log.info(loginDto);

        if(bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());
            return new BaseResponse(StatusCode.INVALID_PARAMS, errors);
        }

        String errorMsg = "";

        try {
            if (!SecurityUtils.getSubject().isAuthenticated()) {
                String newPsd = new Md5Hash(loginDto.getPassword(), env.getProperty("shiro.encrypt.password.salt")).toString();
                UsernamePasswordToken token = new UsernamePasswordToken(loginDto.getUserName(), newPsd);
                SecurityUtils.getSubject().login(token);
            }
        } catch (UnknownAccountException e) {
            errorMsg = e.getMessage();
        } catch (DisabledAccountException e) {
            errorMsg = e.getMessage();
        } catch (IncorrectCredentialsException e) {
            errorMsg = e.getMessage();
        } catch (Exception e) {
            errorMsg = "用户登录异常，请联系管理官！";
            e.printStackTrace();
        }

        if(StringUtils.isBlank(errorMsg)) {
            return new BaseResponse(StatusCode.SUCCESS);
        } else {
            return new BaseResponse(StatusCode.USER_LOGIN_FAILED, errorMsg);
        }
    }

}
