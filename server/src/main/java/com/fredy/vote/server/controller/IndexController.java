package com.fredy.vote.server.controller;

import com.fredy.vote.api.Exception.CustomizeException;
import com.fredy.vote.api.enums.StatusCode;
import com.fredy.vote.server.service.VoteThemeService;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @author Fredy
 * @date 2020-12-05 22:42
 */
@Controller
@Log4j2
public class IndexController {

    @Resource
    private VoteThemeService voteThemeService;

    /**
     * @Description: 获取按(时间)排序的投票主题list
     * @Author: Fredy
     * @Date: 2020-11-24
     */
    @GetMapping({"/", "/list"})
    public String getVoteThemeList(@RequestParam(defaultValue = "1") Integer pageNo,
                                   @RequestParam(defaultValue = "5") Integer pageSize,
                                   @RequestParam(defaultValue = "update_time") String filed,
                                   @RequestParam(defaultValue = "desc") String direction,
                                   Model model) {
        if (pageNo <= 0 || pageSize <= 0 || (!direction.equals("asc") && !direction.equals("desc"))) {
            throw new CustomizeException(StatusCode.INVALID_PARAMS);
        }

        PageInfo voteThemeDtoPageInfo = voteThemeService.getVoteThemeList(pageNo, pageSize, filed, direction);
        model.addAttribute("PageInfo", voteThemeDtoPageInfo);

        return "index";
    }
}
