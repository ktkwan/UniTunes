<#assign song>
<link rel="stylesheet" href="/css/song_page.css">
<link rel="icon" href="/css/images/record.png">
<div class=container>
<div class=mainDiv>
<div class=with-artist>

<div class=col>
<h1 class=mainHeader>${song_name}</h1>
<img class=record src=/css/images/record.png>
</div>
<p class=artist_name>by ${artist_name}</p>

<img class=album_art src=${album_art}>
</div>



<a class="gradient-button gradient-button-1" onclick=${display}>P l a y</a><br />


</div>
</div>


</#assign>
<#include "song_main.ftl">