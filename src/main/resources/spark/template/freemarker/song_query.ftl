<#assign content>

<h1 class=mainHeader> Songs</h1>
<p> Songs:
</p>

<p> 
<#assign items=display?split(", ")>
  <#list items as item> 
	<li class="${item}">${item}</tr>
</#list>
</p>






</#assign>
<#include "main.ftl">