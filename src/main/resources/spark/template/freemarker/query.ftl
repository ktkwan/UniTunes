<#assign content>

<h1> Query by Ambooty and Smashed Toes</h1>

<p> neighbors and radius are both valid
    <form method="GET" action="/neighborsradius">
    <label for="text">Enter command here: </label><br>
    <textarea name="text" id="text"></textarea><br>
    <input type="submit">
</form>
</p>

${submitOutput}

</#assign>
<#include "main.ftl">