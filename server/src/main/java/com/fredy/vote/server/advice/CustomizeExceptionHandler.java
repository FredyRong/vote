package com.fredy.vote.server.advice;

import com.fredy.vote.api.Exception.CustomizeException;
import com.fredy.vote.api.enums.StatusCode;
import com.fredy.vote.api.response.BaseResponse;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Fredy
 * @date 2020-11-17 10:30
 */
@ControllerAdvice
@Log4j2
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable ex, Model model, HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if("application/json".equals(contentType)) {
            BaseResponse baseResponse;

            if(ex instanceof CustomizeException) {
                baseResponse = new BaseResponse((CustomizeException) ex);
                log.error("异常拦截器-拦截的异常: {}", ((CustomizeException) ex).getMsg(), ex.fillInStackTrace());
            } else {
                baseResponse = new BaseResponse(StatusCode.SYSTEM_ERROR);
                log.error("异常拦截器-拦截的异常: {}", ex.getMessage(), ex.fillInStackTrace());
            }

            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.setStatus(200);

                PrintWriter writer = response.getWriter();
                writer.write(new Gson().toJson(baseResponse));
                writer.close();
            } catch (IOException ioException) {
                log.error("异常拦截器打印错误信息-出错", ioException.fillInStackTrace());
            }

            return null;
        } else {
            if(ex instanceof CustomizeException) {
                log.error("异常拦截器-拦截的异常: {}", ((CustomizeException) ex).getMsg(), ex.fillInStackTrace());
            } else {
                log.error("异常拦截器-拦截的异常: {}", ex.getMessage(), ex.fillInStackTrace());
            }

            if(ex instanceof CustomizeException) {
                model.addAttribute("message", ((CustomizeException) ex).getMsg());
                return new ModelAndView("error");
            } else if(ex instanceof UnauthorizedException) {
                model.addAttribute("message", StatusCode.USER_NOT_PERMISSION.getMsg());
                return new ModelAndView("unauth");
            } else {
                model.addAttribute("message", StatusCode.SYSTEM_ERROR.getMsg());
                return new ModelAndView("error");
            }


        }
    }

}
