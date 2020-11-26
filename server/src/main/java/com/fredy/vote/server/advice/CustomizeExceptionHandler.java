package com.fredy.vote.server.advice;

import com.fredy.vote.api.Exception.CustomizeException;
import com.fredy.vote.api.enums.StatusCode;
import com.fredy.vote.api.response.BaseResponse;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
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
            log.error("异常拦截器打印错误信息-出错: {}", ioException.getMessage());
        }

        return null;

//        String contentType = request.getContentType();
//        if("application/json".equals(contentType)) {
//            BaseResponse baseResponse;
//
//            if(ex instanceof CustomizeException) {
//                baseResponse = new BaseResponse((CustomizeException) ex);
//            } else {
//                baseResponse = new BaseResponse(StatusCode.SYSTEM_ERROR);
//            }
//
//            try {
//                response.setCharacterEncoding("UTF-8");
//                response.setContentType("application/json");
//                response.setStatus(200);
//
//                PrintWriter writer = response.getWriter();
//                writer.write(new Gson().toJson(baseResponse));
//                writer.close();
//            } catch (IOException ioException) {
//                log.error("异常拦截器打印错误信息-出错", ioException.fillInStackTrace());
//            }
//
//            return null;
//        } else {
//            if(ex instanceof CustomizeException) {
//                model.addAttribute("message", ((CustomizeException) ex).getMsg());
//            } else {
//                model.addAttribute("message,", StatusCode.SYSTEM_ERROR.getMsg());
//            }
//
//            return new ModelAndView("error");
//        }
    }

}
