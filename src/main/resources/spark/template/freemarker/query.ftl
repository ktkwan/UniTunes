<#assign content>

<h1> uniTunes </h1>

    <div id="base">
        <canvas id="canvas" width="500%" height="500%"></canvas>
        <div class = "sign-in-form-pop-up" id="sign-in">
            <button id = "sign-in-button"> Sign in! </button>
            <form id="sign-in-form">
                <b>Email: </b><input type="text" name="email_login">
                <b>Password: </b><input type="text" name="password_login">
                <input type="submit" id="submit"></input>
            </form>
        </div>
        <div class = "create-form-pop-up" id = "create-account">
            <button id = "create-account-button"> Join the club! </button>
            <form id="create-account-form" method="POST" action="/create-account">
                <b>First Name:</b> <input type="text" name="firstName">
                <b>Last Name: </b><input type="text" name="lastName">
                <b>Email: </b><input type="text" name="email">
                <b>Password: </b><input type="text" name="password">
                <b>Confirm Password: </b><input type="text" name="confirm-password">
                <input type="submit" id="submit"></input>
            </form>
        </div>
    </div>

<#--    <script type="text/css" src="css/login.css"></script>-->
    <script type="text/javascript" src="js/paper-full.js"></script>
    <script type="text/paperscript" canvas = "canvas" src="js/home-background.js"> </script>


</#assign>
<#include "main.ftl">