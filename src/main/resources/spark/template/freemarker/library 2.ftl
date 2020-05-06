<#assign content>

<h1> library </h1>
   ${username} 
		
<p>

<#assign items=library>

  <#list items as item>
	<li >${item}
	</form>

</#list>

</p>

</#assign>
<#include "main.ftl">