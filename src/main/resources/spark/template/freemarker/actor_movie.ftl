<#assign content>

<h1>${name}</h1>
    <link rel="stylesheet" href="static/css/main.css">

    <p>
   <form class="label" method="POST" action="/timdb/:name"></form>
    </p>
    <p>
    <button> <a href="/stars"> Go to Stars! </href> </button>
    <button> <a href="/timdb"> Go back to search! </href> </button>
    ${output}
    </p>

</#assign>
<#include "main.ftl">