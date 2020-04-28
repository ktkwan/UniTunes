<#assign content>
<link rel="stylesheet" href="css/song.css">
<link rel="icon" href="/css/images/record.png">
<h1 class=mainHeader> Songs</h1>



<p> 
<#assign items=display?split(", ")>
  <#list items as item> 
	<li >${item}</tr>
</#list>
</p>






</#assign>
<#include "main.ftl">