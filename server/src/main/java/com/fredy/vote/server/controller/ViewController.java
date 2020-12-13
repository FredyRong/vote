package com.fredy.vote.server.controller;

import com.fredy.vote.api.Exception.CustomizeException;
import com.fredy.vote.api.enums.StatusCode;
import com.fredy.vote.server.dto.UserVoteDetailDto;
import com.fredy.vote.server.dto.VoteThemeDto;
import com.fredy.vote.server.service.VoteThemeService;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Fredy
 * @date 2020-12-05 22:42
 */
@Controller
@Log4j2
public class ViewController {

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

        PageInfo voteThemeDtoPageInfo = voteThemeService.getVoteThemeList(pageNo, pageSize, filed, direction, false);
        model.addAttribute("PageInfo", voteThemeDtoPageInfo);

        return "index";
    }

    /**
     * @Description: 获取单个投票主题信息
     * @Author: Fredy
     * @Date: 2020-11-23
     */
    @GetMapping("/theme/{id:[1-9]\\d*}")
    public String getVoteTheme(@PathVariable Integer id, Model model) {
        if (id == null) {
            throw new CustomizeException(StatusCode.INVALID_PARAMS);
        }

        VoteThemeDto voteThemeDto = voteThemeService.getVoteTheme(id);
        model.addAttribute("VoteTheme", voteThemeDto);

        return "item";
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


    /**
     * @Description: 获取全部投票主题list(admin)
     * @Author: Fredy
     * @Date: 2020-12-13
     */
    @GetMapping({"/admin", "/admin/list"})
    public String getAdminVoteThemeList(@RequestParam(defaultValue = "1") Integer pageNo,
                                   @RequestParam(defaultValue = "5") Integer pageSize,
                                   @RequestParam(defaultValue = "update_time") String filed,
                                   @RequestParam(defaultValue = "desc") String direction,
                                   Model model) {
        if (pageNo <= 0 || pageSize <= 0 || (!direction.equals("asc") && !direction.equals("desc"))) {
            throw new CustomizeException(StatusCode.INVALID_PARAMS);
        }

        PageInfo voteThemeDtoPageInfo = voteThemeService.getVoteThemeList(pageNo, pageSize, filed, direction, true);
        model.addAttribute("PageInfo", voteThemeDtoPageInfo);

        return "admin";
    }

    /**
     * @Description: 跳转添加投票主题
     * @Author: Fredy
     * @Date: 2020-12-12
     */
    @GetMapping("/admin/add")
    public String add() {
        return "add";
    }


    @GetMapping("/admin/update/{id:[1-9]\\d*}")
    public String update(@PathVariable Integer id, Model model) {
        VoteThemeDto voteTheme = voteThemeService.getVoteTheme(id);

        model.addAttribute("VoteTheme", voteTheme);

        List<String> optionList = new ArrayList<>(voteTheme.getOptions().values());
        String options = StringUtils.join(optionList, "\r\n");
        model.addAttribute("options", options);

        return "update";
    }
}
