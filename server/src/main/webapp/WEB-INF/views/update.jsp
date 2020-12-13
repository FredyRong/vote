<%@ page import="com.fredy.vote.model.entity.VoteTheme" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>更新投票主题</title>
    <%@include file="head.jsp" %>
    <link rel="stylesheet" href="${ctx}/static/plugins/bootstrap/css/bootstrap-datetimepicker.min.css" type="text/css">
    <script src="${ctx}/static/plugins/bootstrap/js/bootstrap-datetimepicker.min.js"></script>
</head>
<body>
    <div class="container">
        <h1 class="text-center">更新投票主题</h1>

        <form>
            <div class="form-group">
                <label for="title">标题</label>
                <input type="text" class="form-control" id="title" value="${VoteTheme.title}" placeholder="请填写标题">
            </div>

            <div class="form-group">
                <label for="description">描述</label>
                <input type="text" class="form-control" id="description" value="${VoteTheme.description}" placeholder="请填写描述">
            </div>

            <div class="form-group">
                <label for="description">选项类型</label>
                <div>
                    <label class="radio-inline">
                        <input type="radio" name="selectType" value="1" <c:if test="${VoteTheme.selectType == 1}" >checked</c:if> > 单选
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="selectType" value="2" <c:if test="${VoteTheme.selectType == 2}" >checked</c:if> > 多选
                    </label>
                </div>
            </div>

            <div class="form-group">
                <label for="options">选项值</label>
                <textarea class="form-control" id="options" name="options" placeholder="请输入选项值，每个换行填写" rows="6">${options}</textarea>
            </div>

            <div class="row">
                <div class="col-sm-3">
                    <div class="form-group">
                        <label for="startTime">投票开始时间</label>
                        <div class="input-group date" id="datetimepicker1">
                            <input type="text" class="form-control" id="startTime" name="startTime" readonly value="${VoteTheme.startTime}">
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                    </div>
                </div>

                <div class="col-sm-3">
                    <div class="form-group">
                        <label for="endTime">投票结束时间</label>
                        <div class="input-group date" id="datetimepicker2">
                            <input type="text" class="form-control" id="endTime" name="endTime" readonly value="${VoteTheme.endTime}">
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label for="status">状态</label>
                <div>
                    <label class="radio-inline">
                        <input type="radio" name="status" value="0" <c:if test="${VoteTheme.status == 0}" >checked</c:if>> 已删除
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="status" value="1" disabled <c:if test="${VoteTheme.status == 1}" >checked</c:if>> 已过期
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="status" value="2" <c:if test="${VoteTheme.status == 2}" >checked</c:if>> 暂停
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="status" value="3" <c:if test="${VoteTheme.status == 3}" >checked</c:if>> 启用
                    </label>
                </div>
            </div>

            <div class="form-group">
                <button type="button" class="btn btn-primary" onclick="handleVoteThemeUpdate()">确定</button>
                <a href="${ctx}/admin">
                    <button type="button" class="btn btn-info">返回首页</button>
                </a>
            </div>
        </form>
    </div>

<script>
    $(function () {
        const picker1 = $("#datetimepicker1").datetimepicker({
            format: 'yyyy-mm-ddThh:ii',
            autoclose: true
        });
        const picker2 = $("#datetimepicker2").datetimepicker({
            format: 'yyyy-mm-ddThh:ii',
            autoclose: true
        });
    })

    function handleVoteThemeUpdate() {
        const data = handleParams();
        console.log(data);

        $.ajax({
            url: "${ctx}/theme/update/${VoteTheme.id}",
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify(data),
            dataType: "json",
            success: function (res) {
                if(res.code === 200) {
                    alert("更新投票主题成功！");
                    window.location.href = "${ctx}/admin";
                } else if(res.code === 501) {
                    if(res.data instanceof Array) {
                        alert(res.data.join("\r\n"));
                    } else {
                        alert(res.data);
                    }
                } else {
                    alert(res.msg);
                }
            },
            error: function (err) {
                alert("发生错误！");
                console.log(err);
            }
        })

        function handleParams() {
            let params = {};

            params.title = $("#title").val();
            params.description = $("#description").val();
            params.selectType = $("input[name='selectType']:checked").val();
            params.status = $("input[name='status']:checked").val();
            params.startTime = $("#startTime").val();
            params.endTime = $("#endTime").val();

            params.options = {};
            let optionStr = $("#options").val().split(/[(\r\n)\r\n]+/);
            let count = 1;
            optionStr.forEach((item, index) => {
                if(!item) {
                    optionStr.splice(index, 1);
                } else {
                    params.options[count] = item;
                    count += 1;
                }
            });

            return params;
        }
    }
</script>
</body>
</html>
