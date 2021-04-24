<#-- @ftlvariable name="post" type="ru.itmo.wp.domain.Post" -->
<#-- @ftlvariable name="user" type="ru.itmo.wp.domain.User" -->
<#-- @ftlvariable name="comment" type="ru.itmo.wp.domain.Comment" -->
<#import "commons.ftlh" as c>

<@c.page>
    <#if post??>
        <@c.makePost post/>
        <#if user??>
            <div class="form">
                <div class="header">Write Comment</div>
                <div class="body">
                    <form method="post" action="">
                        <input type="hidden" name="action" value="writeComment"/>
                        <div class="field">
                            <div class="name">
                                <label for="text"></label>
                            </div>
                            <div class="value">
                                <textarea id="text" name="text" maxlength="65000" required>${comment.text!}</textarea>
                            </div>
                        </div>
                        <div class="button-field">
                            <input type="submit" value="Write">
                        </div>
                    </form>
                </div>
            </div>
        </#if>
        <#list post.comments as comment>
            <article>
                <div class="title">Comment</div>
                <div class="information">By ${comment.user.login}</div>
                <div class="body">${comment.text}</div>
            </article>
        </#list>
    <#else>
        No such Post
    </#if>
</@c.page>
