<#assign content>
    <div class=nav-bar>
<img class=music src="https://media.giphy.com/media/cMWU282WTIdihQ6F1X/giphy.gif">
<div class=unitunes>
<p >U n i T u n e s</p>
</div>
</div>
<div class=buttons>
</div>
    <div class=title>
    <h1> ${status} </h1>
    </div>

    <div id="welcome-page">
        <div class=subtitle>
        <p>Let's find new student music!</p>
        <p>Select your favorite song from the options below to get started. </p>
        </div>
        

        <div id="discover">
               <#assign items=songs, a=art>
               <#list items as item>
                ${item}
                <img class=album_art src=${a[item?index]}>
                </#list>
          

        </div>
    </div>

</#assign>

<#include "main.ftl">