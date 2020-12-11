package com.fredy.vote.server.controller;

import com.fredy.vote.api.Exception.CustomizeException;
import com.fredy.vote.api.enums.StatusCode;
import com.fredy.vote.api.response.BaseResponse;
import com.fredy.vote.server.dto.UserVoteDetailDto;
import com.fredy.vote.server.dto.VoteDto;
import com.fredy.vote.server.dto.VoteThemeDto;
import com.fredy.vote.server.service.VoteThemeService;
import com.fredy.vote.server.utils.IpUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fredy
 * @date 2020-11-17 10:52
 */
@Controller
@RequestMapping("/theme")
@Log4j2
public class VoteThemeController {

    @Resource
    private VoteThemeService voteThemeService;

    /**
     * @Description: 添加投票主题
     * @Author: Fredy
     * @Date: 2020-11-23
     */
    @ResponseBody
    @PostMapping("/add")
    public BaseResponse addVoteTheme(@Valid @RequestBody VoteThemeDto dto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());
            return new BaseResponse(StatusCode.INVALID_PARAMS, errors);
        }

        voteThemeService.addVoteTheme(dto);

        return new BaseResponse(StatusCode.SUCCESS);
    }

    
    /**
     * @Description: 获取单个投票主题信息
     * @Author: Fredy
     * @Date: 2020-11-23
     */
    @GetMapping("/{id:[1-9]\\d*}")
    public String getVoteTheme(@PathVariable Integer id, Model model) {
        if (id == null) {
            throw new CustomizeException(StatusCode.INVALID_PARAMS);
        }

        VoteThemeDto voteThemeDto = voteThemeService.getVoteTheme(id);
        model.addAttribute("VoteTheme", voteThemeDto);

        return "item";
    }


    /**
     * @Description: 投票
     * @Author: Fredy
     * @Date: 2020-11-26
     */
    @ResponseBody
    @PostMapping("/vote")
    public BaseResponse vote(@Valid @RequestBody VoteDto voteDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());
            return new BaseResponse(StatusCode.INVALID_PARAMS, errors);
        }

        String ip = null;
        try {
            ip = IpUtil.getIpAddr(request);
        } catch (Exception e) {
            throw new CustomizeException(StatusCode.IP_FAILED);
        }
        voteThemeService.vote(voteDto, ip);

        return new BaseResponse(StatusCode.SUCCESS);
    }


    /**
     * @Description: 获取特定的用户投票详情
     * @Author: Fredy
     * @Date: 2020-11-30
     */
    @GetMapping("/user/detail/{voteThemeId:[1-9]\\d*}")
    public String getSpecificUserVoteDetail(@PathVariable Integer voteThemeId, Model model) {
        Integer userId = 1; // 暂时写死

        UserVoteDetailDto specificUserVoteDetail = voteThemeService.getSpecificUserVoteDetail(userId, voteThemeId);
        model.addAttribute("SpecificUserVoteDetail", specificUserVoteDetail);

        return "detail";
    }
}
