package com.fredy.vote.server.controller;

import com.fredy.vote.api.enums.StatusCode;
import com.fredy.vote.api.response.BaseResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Fredy
 * @date 2020-11-13 15:11
 */
@Controller
public class HelloController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello () {
        return "hello";
    }

    @GetMapping("/hello2")
    @ResponseBody
    public BaseResponse hello2 () {
        return new BaseResponse(StatusCode.SUCCESS);
    }
}
