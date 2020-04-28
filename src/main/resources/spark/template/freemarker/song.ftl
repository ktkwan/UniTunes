<#assign song>
<link rel="stylesheet" href="/css/song_page.css">
<div class=container>
<div class=mainDiv>
<div class=col>
<h1 class=mainHeader>${song_name}</h1>
<img class=record src=/css/images/record.png>
</div>
<p class=song_name>by ${artist_name}</p>



<a class="gradient-button gradient-button-1" onclick=${display}>P l a y</a><br />


</div>
</div>


</#assign>
<#include "song_main.ftl">