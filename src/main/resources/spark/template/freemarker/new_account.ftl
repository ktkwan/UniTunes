<#assign content>

    <h1> ${status} </h1>
    <div id="welcome-page">
        <canvas id="canvas" width="500%" height="100%"></canvas>
        <p>Let's find new student music!</p>
        <div id="discover-background">
            <input type="checkbox"id="song-rec">song1</input>
            <input type="checkbox"id="song-rec">song2</input>
            <input type="checkbox" id="song-rec">song3</input>
            <input type="checkbox"id="song-rec">song4</input>
            <input type="checkbox"id="song-rec">song5</input>
            <input type="checkbox" id="song-rec">song6</input>
            <input type="checkbox"id="song-rec">song7</input>
            <input type="checkbox"id="song-rec">song8</input>
            <input type="checkbox" id="song-rec">song9</input>
            <input type="submit" id="submit-pref">Find Student Musicians</input>
        </div>
    </div>

<#--    <script type="text/css" src="css/login.css"></script>-->
    <script type="text/javascript" src="js/paper-full.js"></script>
    <script type="text/paperscript" canvas = "canvas" src="js/home-background.js"> </script>


</#assign>
<#include "main.ftl">