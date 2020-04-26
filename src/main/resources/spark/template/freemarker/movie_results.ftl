<#assign movie_results>

<h1>${movie_title}</h1>
<p>Here are the actors from this movie!</p>
<p> 

<#assign items=result?split(", ")>
  <#list items as item> 
	<li>${item}</tr>
</#list>
</p>


</#assign>
<#include "movie_results_main.ftl">