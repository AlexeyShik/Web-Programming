<#import "commons.ftlh" as c>

<@c.page>
<#list user.posts as post>
    <@c.makePost post/>
</#list>
</@c.page>
