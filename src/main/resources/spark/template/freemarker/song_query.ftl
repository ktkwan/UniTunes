<#assign content>
<link rel="stylesheet" href="css/song.css">

<link rel="icon" href="/css/images/record.png">
    <title>UniTunes</title>
    
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js" type="text/javascript" charset="utf-8"></script>




<script>
 $(function(){
	$(document).one('click', '.like-review', function(e) {
		
	});
});





</script>



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
<a method="GET" href=/library>Library</a>
</div>
<div class=log>
<a href=/unitunes>Logout</a>
</div>
</div>


<div class=container>
<div class=col>
<h1 class=mainHeader> Recommended Songs</h1>


</div>
</div>

<p>


<#assign hash=songs, hash2=display>
	 <#list hash as key, value>
	 
	<div class="row">
	<li class="songs">
	${key} 
	${value}
	<form method="POST" action="/songs">
	<div class="like-content">
	${hash2[key]}
	<div>
	</form>
	


  
  
  

	
	
	
	</#list>


	

	</tr>
	
	
	 
	 

	
	


	
	
	<div>

	

</#assign>



</p>





</div>
</div>





<#include "main.ftl">