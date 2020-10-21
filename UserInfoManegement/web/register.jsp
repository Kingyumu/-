<%--
  Created by IntelliJ IDEA.
  User: 92961
  Date: 2020/5/21
  Time: 20:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>管理员注册</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="robots" content="all,follow">
    <!-- Bootstrap CSS-->
    <link href="css/bootstrap2.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/css.css">
    <link rel="stylesheet" href="css/style.default.css" id="theme-stylesheet">
    <script type="text/javascript">
        //切换验证码
        function refreshCode() {
            //获取验证码图片对象
            var vcode = document.getElementById("vcode");
            //设置src属性，加时间戳
            vcode.src = "${pageContext.request.contextPath}/checkCodeServlet?time=" + new Date().getTime();
        }
    </script>
    <script>
        if (${checkCodeFlag}){
            alert("验证码错误！");
        }
    </script>
    <script>
        if (${registerErrorFlag}){
            alert("用户名已存在！");
        }
    </script>
    <script>
        if (${emailSucc}){
            alert("验证码已发送至您的邮箱，请注意查收！");
        }
    </script>
    <script>
        if (${emailError}){
            alert("发送失败，请重新输入！");
        }
    </script>
    <script>
        if (${emailExits}){
            alert("此邮箱已注册！");
        }
    </script>
</head>
<body>
<div class="page login-page">
    <div class="container d-flex align-items-center">
        <div class="form-holder has-shadow">
            <div class="row">
                <!-- Logo & Information Panel-->
                <div class="col-lg-6">
                    <div class="info d-flex align-items-center">
                        <div class="content">
                            <div class="logo">
                                <h1>欢迎注册</h1>
                            </div>
                            <p>用户信息管理系统</p>
                        </div>
                    </div>
                </div>
                <!-- Form Panel    -->
                <div class="col-lg-6 bg-white">
                    <div class="form d-flex align-items-center">
                        <form id="form" action="${pageContext.request.contextPath}/registerServlet" method="post">
                            <div class="content">
                                <div class="form-group">
                                    <input id="register-username" class="input-material" type="text" value="${registerUsername}" name="registerUsername" placeholder="请输入用户名/姓名" >
                                    <div class="invalid-feedback">
                                        用户名必须在2~10位之间
                                    </div>
                                </div>
                                <div class="form-group">
                                    <input id="register-password" class="input-material" value="${registerPassword}" type="password" name="registerPassword" placeholder="请输入密码"   >
                                    <div class="invalid-feedback">
                                        密码必须在6~10位之间
                                    </div>
                                </div>
                                <div class="form-group">
                                    <input id="register-passwords" class="input-material" value="${registerPassword}" type="password" name="registerPasswords" placeholder="确认密码"   >
                                    <div class="invalid-feedback">
                                        两次密码必须相同 且在6~10位之间
                                    </div>
                                </div>
                                <div class="form-group">
                                    <input id="register-email" class="input-material" type="text" value="${registerEmail}" name="registerEmail" placeholder="电子邮箱" >
                                    <div class="invalid-feedback">
                                        电子邮箱必须符合格式，如 abc@163.com
                                    </div>
                                </div>
                                <div class="form-group">
                                    <input id="verifycode" class="input-material input-material-two" type="text" name="verifycode" placeholder="验证码" >
                                    <button id="regemail" type="button" name="registerEmailSubmit" class="btn btn-primary">发送验证码</button>
                                </div>
                                <!--<div class="form-group">
                                    <input id="verifycode" type="text" name="verifycode" required data-msg="请输入验证码" placeholder="验证码" class="input-material input-material-two">
                                    <a href="javascript:refreshCode();">
                                        <img src="${pageContext.request.contextPath}/checkCodeServlet" title="不会算？点击刷新" id="vcode"/>
                                    </a>
                                </div>-->
                                <div class="form-group">
                                    <button id="regbtn" type="button" name="registerSubmit" class="btn btn-primary">注册</button>
                                </div>
                                <small>已有账号?</small><a href="loginUser.jsp" class="signup">&nbsp;登录</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- JavaScript files-->
<script src="js/jquery-2.1.0.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script>
    document.getElementById("regemail").onclick = function () {
        document.getElementById("form").action = "${pageContext.request.contextPath}/sendEmailServlet";
        if (document.getElementById("register-email").value != null){
            document.getElementById("form").submit();
        }
    }
</script>
<script>
    $(function(){
        /*错误class  form-control is-invalid
        正确class  form-control is-valid*/
        var flagName=false;
        var flagPas=false;
        var flagPass=false;
        var flagEmail = false;

        var name,passWord,passWords,email;

        /*验证用户名*/
        $("#register-username").mousemove(function(){
            name = $("#register-username").val();
            if(name.length<2||name.length>10){
                $("#register-username").removeClass("form-control is-valid");
                $("#register-username").addClass("form-control is-invalid");
                flagName=false;
            }else{
                $("#register-username").removeClass("form-control is-invalid");
                $("#register-username").addClass("form-control is-valid");
                flagName=true;
            }
        })
        $("#register-username").mouseout(function () {
            name = $("#register-username").val();
            if (name == null || name.length == 0){
                $("#register-username").removeClass("form-control is-invalid");
            }
        })
        /*验证密码*/
        $("#register-password").mousemove(function(){
            passWord = $("#register-password").val();
            if(passWord.length<6||passWord.length>18){
                $("#register-password").removeClass("form-control is-valid");
                $("#register-password").addClass("form-control is-invalid");
                flagPas=false;
            }else{
                $("#register-password").removeClass("form-control is-invalid");
                $("#register-password").addClass("form-control is-valid");
                flagPas=true;
            }
        })
        $("#register-password").mouseout(function () {
            passWord = $("#register-password").val();
            if (passWord == null || passWord.length == 0){
                $("#register-password").removeClass("form-control is-invalid");
            }
        })
        /*验证确认密码*/
        $("#register-passwords").mousemove(function(){
            passWords = $("#register-passwords").val();
            if((passWord!=passWords)||(passWords.length<6||passWords.length>18)){
                $("#register-passwords").removeClass("form-control is-valid");
                $("#register-passwords").addClass("form-control is-invalid");
                flagPass=false;
            }else{
                $("#register-passwords").removeClass("form-control is-invalid");
                $("#register-passwords").addClass("form-control is-valid");
                flagPass=true;
            }
        })
        $("#register-passwords").mouseout(function () {
            passWords = $("#register-passwords").val();
            if (passWords == null || passWords.length == 0){
                $("#register-passwords").removeClass("form-control is-invalid");
            }
        })
        /*验证电子邮箱*/
        $("#register-email").mousemove(function(){
            email = $("#register-email").val();
            var regex = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
            if(regex.test(email)){
                $("#register-email").removeClass("form-control is-invalid");
                $("#register-email").addClass("form-control is-valid");
                flagEmail=true;
            }else{
                $("#register-email").removeClass("form-control is-valid");
                $("#register-email").addClass("form-control is-invalid");
                flagEmail=false;
            }
        })
        $("#register-email").mouseout(function () {
            email = $("#register-email").val();
            if (email == null || email.length == 0){
                $("#register-email").removeClass("form-control is-invalid");
            }
        })

        $("#regbtn").click(function(){
            if(flagName&&flagPas&&flagPass&&flagEmail){
                localStorage.setItem("name",name);
                localStorage.setItem("passWord",passWord);
                document.getElementById("form").action = "${pageContext.request.contextPath}/registerServlet";
                document.getElementById("form").submit();
            }else{
                if(!flagName){
                    $("#register-username").addClass("form-control is-invalid");
                }
                if(!flagPas){
                    $("#register-password").addClass("form-control is-invalid");
                }
                if(!flagPass){
                    $("#register-passwords").addClass("form-control is-invalid");
                }
                if(!flagEmail){
                    $("#register-email").addClass("form-control is-invalid");
                }
            }
        })
    })
</script>
</body>
</html>
