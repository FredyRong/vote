<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>投票主题管理</title>
    <%@include file="head.jsp" %>
</head>
<body>
    <div class="container">
        <h1 class="text-center">投票主题管理</h1>

        <a href="${ctx}/admin/add">
            <button type="button" class="btn btn-primary">添加</button>
        </a>

        <table class="table table-striped" style="margin-top: 30px;">
            <tr>
                <td>#</td>
                <td>标题</td>
                <td>描述</td>
                <td>投票类型</td>
                <td>状态</td>
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
                    <td>
                        <c:choose>
                            <c:when test="${item.status == 0}"><span class="label label-danger">已删除</span></c:when>
                            <c:when test="${item.status == 1}"><span class="label label-danger">已过期</span></c:when>
                            <c:when test="${item.status == 2}"><span class="label label-warning">已暂停</span></c:when>
                            <c:when test="${item.status == 3}"><span class="label label-success">启用中</span></c:when>
                        </c:choose>
                    </td>
                    <td>${fn:replace(item.startTime, 'T', ' ')}</td>
                    <td>${fn:replace(item.endTime, 'T', ' ')}</td>
                    <td>
                        <a href="${ctx}/admin/update/${item.id}">
                            <button type="button" class="btn btn-success">编辑</button>
                        </a>
                        <button type="button" class="btn btn-danger" onclick="handleVoteThemeDelete(${item.id})">删除</button>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <nav aria-label="Page navigation" class="text-right">
            <ul class="pagination">
                <li <c:if test="${!PageInfo.hasPreviousPage}">class="disabled"</c:if> >
                    <a <c:if test="${PageInfo.hasPreviousPage}">href="${ctx}/admin/list?pageNo=${PageInfo.prePage}"</c:if> aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                <c:forEach items="${PageInfo.navigatepageNums}" var="navigatePage">
                    <li <c:if test="${navigatePage} == ${PageInfo.pageNum}">class="active"</c:if> >
                        <a href="${ctx}/admin/list?pageNo=${navigatePage}">${navigatePage}</a>
                    </li>
                </c:forEach>
                <li <c:if test="${!PageInfo.hasNextPage}">class="disabled"</c:if> >
                    <a <c:if test="${PageInfo.hasNextPage}">href="${ctx}/admin/list?pageNo=${PageInfo.nextPage}"</c:if> aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
    </div>

<script>
    function handleVoteThemeDelete(id) {
        $.ajax ({
            url: "${ctx}/theme/delete/" + id,
            type: "DELETE",
            success: function (res) {
                if(res.code === 200) {
                    alert("删除投票主题成功！");
                    window.location.reload();
                } else {
                    alert(res.msg);
                }
            },
            error: function (err) {
                alert("发生错误！");
                console.log(err);
            }
        })
    }
</script>

</body>
</html>
