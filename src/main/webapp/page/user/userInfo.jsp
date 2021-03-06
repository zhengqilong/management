<%--
  Created by IntelliJ IDEA.
  User: zhengqilong
  Date: 2018/5/22
  Time: 15:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>个人资料--layui后台管理模板</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <link rel="stylesheet" href="${ctx}/layui-admin/layui/css/layui.css" media="all" />
    <link rel="stylesheet" href="${ctx}/layui-admin/css/user.css" media="all" />
</head>
<body class="childrenBody">
<form class="layui-form" id="userUpdateForm" action="${ctx}/userUpdate">
    <div class="user_left">
        <div class="layui-form-item">
            <label class="layui-form-label">账号</label>
            <div class="layui-input-block">
                <input type="text" name="userCode" value="${user.userCode}" disabled class="layui-input layui-disabled">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">角色</label>
            <div class="layui-input-block">

                <input type="text" name="userType"
                <c:choose>
                <c:when test="${user.userType eq 0}">
                       value="超级管理员"
                </c:when>
                        <c:when test="${user.userType eq 1}">
                       value="管理员"
                </c:when>
                        <c:when test="${user.userType eq 2}">
                       value="社团管理员"
                </c:when>
                <c:when test="${user.userType eq 3}">
                       value="普通用户"
                </c:when>
                </c:choose>

                       disabled class="layui-input layui-disabled">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">真实姓名</label>
            <div class="layui-input-block">
                <input type="text" value="${user.userName}" name="userName" placeholder="请输入真实姓名" lay-verify="required" class="layui-input">
                <input type="hidden" value="${user.id}" name="id"  class="layui-input">
            </div>
        </div>
        <div class="layui-form-item" pane="">
            <label class="layui-form-label">性别</label>
            <div class="layui-input-block">
                <c:choose>
                    <c:when test="${user.userSex eq 1}">
                        <input type="radio" name="userSex" value="1" title="男" checked="">
                        <input type="radio" name="userSex" value="2" title="女">
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="userSex" value="1" title="男" >
                        <input type="radio" name="userSex" value="2" title="女" checked="">
                    </c:otherwise>
                </c:choose>

            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">手机号码</label>
            <div class="layui-input-block">
                <input type="tel" name="userPhone" value="${user.userPhone}" placeholder="请输入手机号码" lay-verify="required|phone" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">出生年月</label>
            <div class="layui-input-block">
                <input type="text" name="userBirthday" value="${user.userBirthday}" placeholder="请输入出生年月" <%--lay-verify="required|date"--%> onclick="layui.laydate({elem: this,max: laydate.now()})" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">邮箱</label>
            <div class="layui-input-block">
                <input type="text" name="userMail" value="${user.userMail}" placeholder="请输入邮箱" lay-verify="required|email" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">自我评价</label>
            <div class="layui-input-block">
                <textarea placeholder="请输入内容" name="userEvaluate" class="layui-textarea">${user.userEvaluate}</textarea>
            </div>
        </div>
    </div>

    <div class="layui-form-item" style="margin-left: 5%;">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit="" lay-filter="changeUser">立即提交</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
</form>

<script type="text/javascript" src="${ctx}/layui-admin/layui/layui.js"></script>
<script type="text/javascript" src="${ctx}/layui-admin/page/user/address.js"></script>

<script type="text/javascript" src="${ctx}/layui-admin/page/user/user.js"></script>
</body>
</html>
