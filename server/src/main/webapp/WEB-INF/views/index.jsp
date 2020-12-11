<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>首页</title>
    <%@include file="head.jsp" %>
</head>
<body>
    <div class="container">
        <h1 class="text-center">网上在线投票</h1>

        <table class="table table-striped" style="margin-top: 30px;">
            <tr>
                <td>#</td>
                <td>标题</td>
                <td>描述</td>
                <td>投票类型</td>
                <td>投票开始时间</td>
                <td>投票结束时间</td>
                <td>操作</td>
            </tr>

            <c:forEach items="${PageInfo.list}" var="item" varStatus="itemStatus">
                <tr>
                    <td>${itemStatus.count}</td>
                    <td>${item.title}</td>
                    <td>${item.description}</td>
                    <td>
                        <span class="label label-primary">
                            <c:choose>
                                <c:when test="${item.selectType == 1}">单选</c:when>
                                <c:when test="${item.selectType == 2}">多选</c:when>
                            </c:choose>
                        </span>
                    </td>
                    <td>${fn:replace(item.startTime, 'T', ' ')}</td>
                    <td>${fn:replace(item.endTime, 'T', ' ')}</td>
                    <td>
                        <c:choose>
                            <c:when test="${item.canVote}">
                                <a href="theme/${item.id}">
                                    <button type="button" class="btn btn-info">进入投票</button>
                                </a>
                            </c:when>
                            <c:when test="${!item.canVote}">
                                <button type="button" class="btn btn-danger" disabled="disabled">投票已过期或未到投票时间</button>
                            </c:when>
                        </c:choose>

                    </td>
                </tr>
            </c:forEach>
        </table>

        <nav aria-label="Page navigation" class="text-right">
            <ul class="pagination">
                <li <c:if test="${!PageInfo.hasPreviousPage}">class="disabled"</c:if> >
                    <a <c:if test="${PageInfo.hasPreviousPage}">href="list?pageNo=${PageInfo.prePage}"</c:if> aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                <c:forEach items="${PageInfo.navigatepageNums}" var="navigatePage">
                    <li <c:if test="${navigatePage} == ${PageInfo.pageNum}">class="active"</c:if> >
                        <a href="list?pageNo=${navigatePage}">${navigatePage}</a>
                    </li>
                </c:forEach>
                <li <c:if test="${!PageInfo.hasNextPage}">class="disabled"</c:if> >
                    <a <c:if test="${PageInfo.hasNextPage}">href="list?pageNo=${PageInfo.nextPage}"</c:if> aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
    </div>

${PageInfo}
</body>
</html>
