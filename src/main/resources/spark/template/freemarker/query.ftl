<#assign content>
<link rel="icon" href="/css/images/record.png">
<div class=title2>
<h1> uniTunes </h1>
</div>
    <link rel="stylesheet" href="css/main.css">
    <div id="base" style="color:white; ">
        <canvas id="canvas" width="1800%" height="300%"></canvas>
        <div class = "sign-in-form-pop-up" id="sign-in">
            <button id = "sign-in-button" class = gradient-button sign-in-button> Sign in! </button>
            <form id="sign-in-form">
                <div class=inputs>
                <b >Email: </b><input type="text" name="email_login" class=email>
                <b >Password: </b><input type="text" name="password_login" class=pass>
                <input class=sub type="submit" id="submit"></input>
                </div>
            </form>
        </div>
        <div class = "create-form-pop-up" id = "create-account">
            <button id = "create-account-button" class = gradient-button sign-in-button> Join the club! </button>
            <form id="create-account-form" method="POST" action="/create-account">
            <div class=inputs>
                <b class=first1>First Name:</b> <input type="text" name="firstName" class=email>
                <b class=last1>Last Name: </b><input type="text" name="lastName" class=email>
                <b>Email: </b><input type="text" name="email" class=email>
                <b>Password: </b><input type="text" name="password" class=email>
                <b>Confirm Password: </b><input type="text" name="confirm-password" class=email>
                <input type="submit" id="submit" class=sub></input>
            </div>
            </form>
        </div>
    </div>


<#--    <script type="text/css" src="css/login.css"></script>-->
    <script type="text/javascript" src="js/paper-full.js"></script>
    <script type="text/paperscript" canvas = "canvas" src="js/home-background.js"> </script>


</#assign>
<#include "main.ftl">