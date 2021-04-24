<#-- @ftlvariable name="users" type="ru.itmo.wp.domain.User[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <#if users??>
        <@c.datatable/>
    <#else>
        No such user
    </#if>
</@c.page>
