<#assign content>

    <h1> ${status} </h1>
    <div id="welcome-page">
        <canvas id="canvas" width="1000%" height="200%" style="background: #1b262c"></canvas>
        <p>Let's find new student music!</p>
        <p>Select your favorite song from the options below to get started. </p>

        <div id="discover">
               ${songs}
        </div>
    </div>

<#--    <script type="text/css" src="css/login.css"></script>-->
    <script type="text/javascript" src="js/paper-full.js"></script>
    <script type="text/paperscript" canvas = "canvas" src="js/home-background.js"> </script>


</#assign>
<#include "main.ftl">