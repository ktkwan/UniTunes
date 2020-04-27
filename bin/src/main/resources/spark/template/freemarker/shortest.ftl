<#assign content>

<h1>TIMDb by Ambu and Smashed Toes</h1>
<p>
    <form method="GET" action="/timdb"></form>

    <form class="label" method="POST" action="/shortest_results">
    <label for="text">Enter two actors of ur choice here (without quotes): </label><br>
    <div id="sourceText">
        <textarea name="text1" id="text1"></textarea><br>
        <textarea name="text2" id="text2"></textarea><br>
    <input type="submit"></div>
    </form>
</p>
    ${output}


</#assign>
<#include "main.ftl">