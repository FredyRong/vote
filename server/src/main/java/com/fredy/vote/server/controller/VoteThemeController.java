package com.fredy.vote.server.controller;

import com.fredy.vote.api.enums.StatusCode;
import com.fredy.vote.api.response.BaseResponse;
import com.fredy.vote.model.dto.VoteThemeDto;
import com.fredy.vote.server.service.VoteThemeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fredy
 * @date 2020-11-17 10:52
 */
@Controller
@RequestMapping("/theme")
@ResponseBody
@Log4j2
public class VoteThemeController {

    @Resource
    private VoteThemeService voteThemeService;

    @PostMapping("/add")
    public BaseResponse addVoteTheme(@Valid VoteThemeDto dto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());
            return new BaseResponse(StatusCode.INVALID_PARAMS, errors);
        }

        voteThemeService.addVoteTheme(dto);

        return new BaseResponse(StatusCode.SUCCESS);
    }
}
