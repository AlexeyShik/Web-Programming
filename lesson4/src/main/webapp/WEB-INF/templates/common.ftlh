<#macro page>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/style.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro header>
    <header>
        <a href="#" class="logo"><img alt="Codeforces" title="Codeforces"
                                      src="/img/codeforces-vs-coronavirus-65.png"></a>
        <div class="languages">
            <a href="#"><img alt="In English" title="In English"
                             src="/img/gb.png"></a>
            <a href="#"><img alt="In Russian" title="In Russian"
                             src="/img/ru.png"></a>
        </div>
        <div class="enter-or-register-box">
            <#if user??>
                <@userlink user false/>
                |
                <a href="/logout">Logout</a>
            <#else>
                <a href="/enter">Enter</a>
                |
                <a href="/register">Register</a>
            </#if>
        </div>
        <nav>
            <#assign items=[
            initMenuItem("/index", "Home"),
            initMenuItem("/misc/help", "Help"),
            initMenuItem("/contests", "Contests"),
            initMenuItem("/users", "Users")
            ]/>
            <ul>
                <#list items as item>
                    <li>
                        <@initMenuHref (item.href == URI), item!/>
                    </li>
                </#list>
            </ul>
        </nav>
    </header>
</#macro>

<#macro sidebar>
    <aside>
        <@printAsidePosts/>
    </aside>
</#macro>

<#macro footer>
    <footer>
        2010-2020 &copy; by Mike Mirzayanov
    </footer>
</#macro>

<#macro userColoredRef handle color>
    <a class="${color}" href="user?handle=${handle}">${handle}</a>
</#macro>

<#macro userNameOnlyRef handle name>
    <a href="user?handle=${handle}">${name}</a>
</#macro>

<#macro userlink user colored>
    <#if colored>
        <@userColoredRef user.handle, user.color/>
    <#else>
        <@userNameOnlyRef user.handle user.handle/>
    </#if>
</#macro>

<#macro processArrow index delta forbiddenIndex name text>
    <div class=${name}>
        <#if index != forbiddenIndex>
            <@userNameOnlyRef findByIndex(users, index + delta).handle text/>
        <#else>
            <p>${text}</p>
        </#if>
    </div>
</#macro>

<#macro initMenuHref isUnderlined item>
    <#if isUnderlined>
        <a class="current" href="${item.href}">${item.name}</a>
    <#else>
        <a href="${item.href}">${item.name}</a>
    </#if>
</#macro>

<#macro printAsidePosts>
    <#list posts?reverse as post>
        <section>
            <div class="header">
                Post #${post.id}
            </div>
            <div class="body">
                <@getText post false/>
            </div>
            <div class="footer">
                <a href="/post?post_id=${post.id}">View all</a>
            </div>
        </section>
    </#list>
</#macro>

<#macro printAllPosts>
    <#list posts?reverse as post>
        <@printPost post false/>
    </#list>
</#macro>

<#macro printUserPosts user_id>
    <#list posts?reverse as post>
        <#if post.userId == user_id>
            <@printPost post true/>
        </#if>
    </#list>
</#macro>

<#macro printPost post full_text>
    <article>
        <div class="header">${post.title}</div>
        <#assign user = findById(users, post.userId)!/>
        <div class="information">By
            <@userlink user true/>, 2 days ago, translation
        </div>
        <div class="body">
            <@getText post full_text/>
        </div>
        <ul class="attachment">
            <li>Announcement of <a href="#">Codeforces Round #510 (Div. 1)</a></li>
            <li>Announcement of <a href="#">Codeforces Round #510 (Div. 2)</a></li>
        </ul>
        <div class="footer">
            <div class="left">
                <img src="img/voteup.png" title="Vote Up" alt="Vote Up"/>
                <span class="positive-score">+173</span>
                <img src="img/votedown.png" title="Vote Down" alt="Vote Down"/>
            </div>
            <div class="right">
                <img src="img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                2 days ago
                <img src="img/comments_16x16.png" title="Comments" alt="Comments"/>
                <a href="#">68</a>
            </div>
        </div>
    </article>
</#macro>

<#macro getText post additional>
    <#if post.text?length <= 250 || additional>
        <p>${post.text}</p>
    <#else>
        <p>${post.text?substring(0, 250) + "..."}</p>
    </#if>
</#macro>

<#function initMenuItem href name>
    <#return {"href": href, "name": name}/>
</#function>

<#function findBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item/>
        </#if>
    </#list>
</#function>

<#function getIndex items value>
    <#list items as item>
        <#if item == value>
            <#return item?index/>
        </#if>
    </#list>
</#function>

<#function findByIndex items index>
    <#list items as item>
        <#if item?index == index>
            <#return item>
        </#if>
    </#list>
</#function>

<#function findById items id>
    <#list items as item>
        <#if item.id == id>
            <#return item>
        </#if>
    </#list>
</#function>