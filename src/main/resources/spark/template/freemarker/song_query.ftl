<#assign content>
<link rel="stylesheet" href="css/song.css">
<link rel="icon" href="/css/images/record.png">
    <title>UniTunes</title>
<div class=first>
<div class=nav-bar>
<img class=music src="https://media.giphy.com/media/cMWU282WTIdihQ6F1X/giphy.gif">
<p class="t">U n i T u n e s</p>
</div>
<div class=buttons>

</div>

<div class=container>
<div class=col>
<h1 class=mainHeader> Recommended Songs</h1>


</div>
<p>



<#assign hash=songs>
	 <#list hash as key, value>
	<div class="row">
	<li class="songs">
	${key}
	${value}
	
	</tr>
	<div>

</#list>


</p>

</div>
</div>






</#assign>
<#include "main.ftl">