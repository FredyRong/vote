package com.fredy.vote.server.controller;

import com.fredy.vote.api.Exception.CustomizeException;
import com.fredy.vote.api.enums.StatusCode;
import com.fredy.vote.api.response.BaseResponse;
import com.fredy.vote.server.dto.VoteDto;
import com.fredy.vote.server.dto.VoteThemeDto;
import com.fredy.vote.server.enums.SysConstant;
import com.fredy.vote.server.service.VoteThemeService;
import com.fredy.vote.server.utils.IpUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
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
    @RequiresPermissions("theme:add")
    @ResponseBody
    @PostMapping("/add")
    public BaseResponse addVoteTheme(@Valid @RequestBody VoteThemeDto dto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());
            return new BaseResponse(StatusCode.INVALID_PARAMS, errors);
        }

        if(dto.getStartTime().isAfter(dto.getEndTime())) {
            return new BaseResponse(StatusCode.INVALID_PARAMS, "投票开始时间不能在结束时间之后！");
        } else if(dto.getStatus() == SysConstant.VoteThemeStatus.SUCCESS.getCode() && dto.getEndTime().isBefore(LocalDateTime.now())) {
            return new BaseResponse(StatusCode.INVALID_PARAMS, "投票结束时间不能比当前时间还晚！");
        }

        voteThemeService.addVoteTheme(dto);

        return new BaseResponse(StatusCode.SUCCESS);
    }


    /**
     * @Description: 更新投票主题
     * @Author: Fredy
     * @Date: 2020-12-13
     */
    @RequiresPermissions("theme:update")
    @ResponseBody
    @PutMapping("/update/{id:[1-9]\\d*}")
    public BaseResponse updateVoteTheme(@PathVariable Integer id,
                                        @Valid @RequestBody VoteThemeDto dto,
                                        BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());
            return new BaseResponse(StatusCode.INVALID_PARAMS, errors);
        }

        if(dto.getStartTime().isAfter(dto.getEndTime())) {
            return new BaseResponse(StatusCode.INVALID_PARAMS, "投票开始时间不能在结束时间之后！");
        } else if(dto.getStatus() == SysConstant.VoteThemeStatus.SUCCESS.getCode() && dto.getEndTime().isBefore(LocalDateTime.now())) {
            return new BaseResponse(StatusCode.INVALID_PARAMS, "投票结束时间不能比当前时间还晚！");
        }

        dto.setId(id);
        voteThemeService.updateVoteTheme(dto);

        return new BaseResponse(StatusCode.SUCCESS);
    }


    /**
     * @Description: 删除投票主题
     * @Author: Fredy
     * @Date: 2020-12-15
     */
    @RequiresPermissions("theme:delete")
    @ResponseBody
    @DeleteMapping("/delete/{id:[1-9]\\d*}")
    public BaseResponse deleteVoteTheme(@PathVariable Integer id) {
        voteThemeService.deleteVoteTheme(id);

        return new BaseResponse(StatusCode.SUCCESS);
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

}
