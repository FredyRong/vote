<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>投票</title>
    <%@include file="head.jsp" %>
</head>
<body>
    <div class="container">
        <h1 class="text-center">投票</h1>

        <form class="form-horizontal">
            <input type="hidden" name="userId" value="1">
            <input type="hidden" name="voteThemeId" value="${VoteTheme.id}">

            <div class="form-group">
                <label class="col-sm-2 control-label">标题</label>
                <div class="col-sm-10">
                    <p class="form-control-static">${VoteTheme.title}</p>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">描述</label>
                <div class="col-sm-10">
                    <p class="form-control-static">${VoteTheme.description}</p>
                </div>
            </div>
            
            <div class="form-group">
                <label class="col-sm-2 control-label">状态</label>
                <div class="col-sm-10">
                    <c:choose>
                        <c:when test="${VoteTheme.status == 0}"><span class="label label-danger">已删除</span></c:when>
                        <c:when test="${VoteTheme.status == 1}"><span class="label label-danger">已过期</span></c:when>
                        <c:when test="${VoteTheme.status == 2}"><span class="label label-warning">停止投票</span></c:when>
                        <c:when test="${VoteTheme.status == 3}"><span class="label label-primary">正在投票</span></c:when>
                    </c:choose>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">投票开始时间</label>
                <div class="col-sm-10">
                    <p class="form-control-static">${fn:replace(VoteTheme.startTime, 'T', ' ')}</p>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">投票结束时间</label>
                <div class="col-sm-10">
                    <p class="form-control-static">${fn:replace(VoteTheme.endTime, 'T', ' ')}</p>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">选项</label>
                <div class="col-sm-10">
                    <c:choose>
                        <c:when test="${VoteTheme.selectType == 1}">
                            <div class="radio">
                                <c:forEach items="${VoteTheme.options}" var="option">
                                    <label class="checkbox-inline">
                                        <input type="radio" value="${option.key}" name="optionValue">
                                            ${option.value}
                                    </label>
                                </c:forEach>
                            </div>

                        </c:when>

                        <c:when test="${VoteTheme.selectType == 2}">
                            <div class="checkbox">
                                <c:forEach items="${VoteTheme.options}" var="option">
                                    <label class="radio-inline">
                                        <input type="checkbox" value="${option.key}" name="optionValue">
                                            ${option.value}
                                    </label>
                                </c:forEach>
                            </div>
                        </c:when>
                    </c:choose>
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${VoteTheme.status == 3 && VoteTheme.canVote}">
                            <button type="button" class="btn btn-primary" onclick="handleVote()">投票</button>
                        </c:when>
                        <c:when test="${VoteTheme.status == 2}">
                            <button type="button"class="btn btn-danger" disabled>该投票已被管理员禁止投票</button>
                        </c:when>
                        <c:when test="${VoteTheme.status == 1}">
                            <button type="button"class="btn btn-danger" disabled>该投票已过期</button>
                        </c:when>
                        <c:when test="${VoteTheme.status == 0}">
                            <button type="button" class="btn btn-danger" disabled>该投票已删除</button>
                        </c:when>
                    </c:choose>
                    <a href="${ctx}/">
                        <button type="button" class="btn btn-info">返回首页</button>
                    </a>
                </div>
            </div>
        </form>
    </div>

    <style>
        .label {
            line-height: 27px;
        }
    </style>

    <script>
        function handleVote() {
            let data = {};

            data.userId = $("input[name='userId']").val();
            data.voteThemeId = $("input[name='voteThemeId']").val();

            <c:choose>
                <c:when test="${VoteTheme.selectType == 1}">
                    let optionValue = $("input[name='optionValue']:checked").val();
                    if(optionValue) {
                        data.optionValue = [optionValue];
                    } else {
                        alert("您还没有投票！");
                        return;
                    }
                </c:when>

                <c:when test="${VoteTheme.selectType == 2}">
                    let optionValue = [];
                    $("input[name='optionValue']:checked").each(function (i) {
                        optionValue.push($(this).val());
                    });
                    if(optionValue.length !== 0) {
                        data.optionValue = optionValue;
                    } else {
                        alert("您还没有投票！");
                        return;
                    }
                </c:when>
            </c:choose>

            $.ajax({
                url: "${ctx}/theme/vote",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(data),
                dataType: "json",
                success: function(res) {
                    if(res.code === 200) {
                        alert("投票成功！");
                        window.location.href = "${ctx}/user/detail/${VoteTheme.id}";
                    } else {
                        alert(res.msg);
                        window.location.href = "${ctx}/user/detail/${VoteTheme.id}";
                    }
                },
                error: function(err) {
                    alert("发生错误！");
                    console.log(err);
                }
            });
            return false;
        }
    </script>
</body>
</html>
