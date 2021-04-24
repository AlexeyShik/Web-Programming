<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if handle?has_content>
        <#assign user = c.findBy(users, "handle", handle)!/>
    <#else>
        <#assign user = c.findBy(users, "id", user_id)!/>
    </#if>

    <#if user?has_content>
        <div class="user">
            <div class="data">
                <div class="info">
                    <header>
                        <@c.userlink user true/>
                    </header>
                    <ul>
                        <li>id: ${user.id}</li>
                        <li>handle: ${user.handle}</li>
                        <li>name: ${user.name}</li>
                        <li>
                            <a href="/posts?user_id=${user.id}">posts</a>: ${user.postsCount}
                        </li>
                    </ul>
                </div>
                <div class="aside">
                    <img src="/img/no-title.jpg" alt="no-title" class="icon">
                </div>
            </div>
            <div class="footer">
                <#assign index = c.getIndex(users, user)!>
                <@c.processArrow index, -1, 0, "left-arrow", "⟵"/>
                <@c.processArrow index, 1, users?size - 1, "right-arrow", "⟶"/>
            </div>
        </div>
    <#else>
        No such user
    </#if>
</@c.page>
