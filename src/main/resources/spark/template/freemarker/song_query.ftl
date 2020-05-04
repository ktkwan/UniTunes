<#assign content>
<link rel="stylesheet" href="css/song.css">
<link rel="stylesheet" href="css/main.css">

<link rel="icon" href="/css/images/record.png">
    <title>UniTunes</title>

<div class=first>
<div class=nav-bar>
<img class=music src="https://media.giphy.com/media/cMWU282WTIdihQ6F1X/giphy.gif">
<p class="t">U n i T u n e s</p>
</div>
<div class=buttons>
<div class=home>
<a href=/unitunes>Home</a>
</div>
<div class=browse>
<a href=/songs>Browse</a>
</div>
<div class=lib>
<a href=/songs>Library</a>
</div>
<div class=log>
<a href=/unitunes>Logout</a>
</div>
</div>


<div class=container>
<div class=col>
<h1 class=mainHeader> Recommended Songs</h1>

<form method="GET" action="/library"> 

<button type="submit" id="library"> My Library </button>
</form>


</div>
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
  <#list items as item>
    <form method="POST" action="/songs">
	<li >${item}
	</form>

</#list>


</p>

</div>
</div>


    <script type="text/javascript" src="js/paper-full.js"></script>
    <script type="text/paperscript" canvas = "canvas" src="js/home-background.js"> </script>



</#assign>
<#include "main.ftl">