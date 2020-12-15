<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %><html>
<head>
    <title>登录</title>
    <%@include file="head.jsp" %>
</head>
<body>
    <div class="container">
        <h1 class="text-center">登录</h1>

        <form>
            <div class="form-group">
                <label for="userName">用户名</label>
                <input type="text" class="form-control" id="userName" placeholder="请填写用户名">
            </div>

            <div class="form-group">
                <label for="password">密码</label>
                <input type="password" class="form-control" id="password" placeholder="请填写密码">
            </div>

            <div class="form-group">
                <button type="button" class="btn btn-primary" onclick="handleLogin()">登录</button>
            </div>
        </form>
    </div>

<script>
    function handleLogin() {
        const data = handleParams();

        $.ajax({
            url: "${ctx}/user/login",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            dataType: "json",
            success: function (res) {
                if(res.code === 200) {
                    alert("登录成功！");
                    window.history.back();
                } else if(res.code === 501) {
                    alert(res.data.join("\r\n"));
                } else if(res.code === 516) {
                    alert(res.data);
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

    function handleParams() {
        let params = {};

        params.userName = $("#userName").val();
        params.password = $("#password").val();

        return params;
    }
</script>
</body>
</html>
