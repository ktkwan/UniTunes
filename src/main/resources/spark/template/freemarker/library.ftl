<#assign content>
<link rel="stylesheet" href="/css/library.css">

<div class=first>
<div class=nav-bar>
<img class=music src="https://media.giphy.com/media/cMWU282WTIdihQ6F1X/giphy.gif">
<p class="t">U n i T u n e s</p>
</div>
<div class=buttons>
<div class=browse>
<a href=/songs>Recommendations</a>
</div>
<div class=lib>
<a method="GET" href=/library>Library</a>
</div>
<div class=log>
<a href=/unitunes>Logout</a>
</div>
</div>
<div class=user>
<p>
<#assign s = username>
<#assign s += "'s Library">

${s}
</p>
</div>



		
<div>		
<p>

<#assign items=library, n=names, a=art>

  <#list items as item>
  <div class=lib>
  ${n[item?index]}
 
  ${a[item?index]}
  </div>




</#list>
</div>

</p>

</#assign>
<#include "main.ftl">