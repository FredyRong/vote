package com.fredy.vote.server.dto;

import com.fredy.vote.server.enums.SysConstant;
import com.fredy.vote.server.annotation.EnumValid;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Fredy
 * @date 2020-11-17 11:02
 */
@Data
public class VoteThemeDto {

    private Integer id;

    // 标题
    @NotBlank(message = "标题不能为空")
    private String title;

    // 描述
    @NotBlank(message = "描述不能为空")
    private String description;

    // 选项类型，1：单选 2：多选
    @EnumValid(target = SysConstant.VoteThemeSelectType.class, message = "选项类型错误")
    private Integer selectType;

    // 状态，0：已删除 1：已过期 2：暂停 3：启用
    @EnumValid(target = SysConstant.VoteThemeStatus.class, message = "状态值错误")
    private Integer status;

    // 投票开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "投票开始时间不能为空")
    private LocalDateTime startTime;

    // 投票结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "投票结束时间不能为空")
    private LocalDateTime endTime;

    // 选项 key: value value：label
    @NotEmpty(message = "选项不能为空")
    private Map<String, String> options;

    // 投票数 key: value value: total
    private Map<String, Long> voteNumbers;
}
