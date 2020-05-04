<#assign content>
<link rel="stylesheet" href="css/song.css">
<link rel="stylesheet" href="css/main.css">

<link rel="icon" href="/css/images/record.png">
    <title>UniTunes</title>
<div class=first>
<img class=music src="https://media.giphy.com/media/cMWU282WTIdihQ6F1X/giphy.gif">
<div class=container>
<div class=col>
<h1 class=mainHeader> Recommended Songs</h1>

<form method="GET" action="/library"> 

<button type="submit" id="library"> My Library </button>
</form>


</div>
<p>

<#assign items=display>

  <#list items as item>
    <form method="POST" action="/songs">

	<li >${item}
	</form>

</#list>

</p>

</div>
</div>






</#assign>
<#include "main.ftl">