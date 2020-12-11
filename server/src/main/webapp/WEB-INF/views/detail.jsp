<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %><html>
<head>
    <title>投票详情</title>
    <%@include file="head.jsp" %>
</head>
<body>
    <div class="container">
        <h1 class="text-center">投票详情</h1>

        <table class="table table-striped">
            <tr>
                <td>标题</td>
                <td>${SpecificUserVoteDetail.voteTheme.title}</td>
            </tr>

            <tr>
                <td>描述</td>
                <td>${SpecificUserVoteDetail.voteTheme.description}</td>
            </tr>

            <tr>
                <td>状态</td>
                <td>
                    <c:choose>
                        <c:when test="${SpecificUserVoteDetail.voteTheme.status == 0}"><span class="label label-danger">已删除</span></c:when>
                        <c:when test="${SpecificUserVoteDetail.voteTheme.status == 1}"><span class="label label-danger">已过期</span></c:when>
                        <c:when test="${SpecificUserVoteDetail.voteTheme.status == 2}"><span class="label label-warning">停止投票</span></c:when>
                        <c:when test="${SpecificUserVoteDetail.voteTheme.status == 3}"><span class="label label-primary">正在投票</span></c:when>
                    </c:choose>
                </td>
            </tr>

            <tr>
                <td>投票类型</td>
                <td>
                    <span class="label label-primary">
                        <c:choose>
                            <c:when test="${SpecificUserVoteDetail.voteTheme.selectType == 1}">单选</c:when>
                            <c:when test="${SpecificUserVoteDetail.voteTheme.selectType == 2}">多选</c:when>
                        </c:choose>
                    </span>
                </td>
            </tr>

            <tr>
                <td>投票开始时间</td>
                <td>${fn:replace(SpecificUserVoteDetail.voteTheme.startTime, 'T', ' ')}</td>
            </tr>

            <tr>
                <td>投票结束时间</td>
                <td>${fn:replace(SpecificUserVoteDetail.voteTheme.endTime, 'T', ' ')}</td>
            </tr>

            <tr>
                <td>投票结果</td>
                <td>
                    <c:forEach items="${SpecificUserVoteDetail.voteThemeOptionList}" var="VoteThemeOption">
                        <p>
                            ${VoteThemeOption.label} : ${VoteThemeOption.total}票
                            <c:if test="${SpecificUserVoteDetail.userVoteOptionList.contains(VoteThemeOption.id)}">
                                <span style="color: red; font-weight: bolder">（您投过此票！）</span>
                            </c:if>
                        </p>
                    </c:forEach>
                </td>
            </tr>
        </table>

        <div class="text-center">
            <a href="${ctx}/">
                <button type="button" class="btn btn-primary">返回首页</button>
            </a>
        </div>
    </div>
</body>
</html>
