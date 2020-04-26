<#assign content>

<h1 class=mainHeader> Movie Database!</h1>
<p>Enter two actors names below and see how they are connected!</p>
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