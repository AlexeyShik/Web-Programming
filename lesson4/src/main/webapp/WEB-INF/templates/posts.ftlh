<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#assign user = c.findById(users, user_id)!>
    <#if user?has_content>
        <@c.printUserPosts user_id/>
    <#else>
        no such user
    </#if>
</@c.page>