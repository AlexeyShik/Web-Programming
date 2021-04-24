<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="text" type="java.lang.String" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#-- @ftlvariable name="user" type="ru.itmo.wp.model.domain.User" -->
<#import "commons.ftlh" as c>

<@c.page>
    <#if user??>
    <div class="form-box">
        <div class="header">Create article</div>
        <div class="body">
            <form method="post" action="">
                <input type="hidden" name="action" value="create"/>
                <div class="field">
                    <div class="name">
                        <label for="title">Title</label>
                    </div>
                    <div class="value">
                        <input id="title" name="title" value="${title!}"/>
                    </div>
                </div>
                <p>
                    <label>
                        <textarea class="text-area" name="text" cols="44" rows="16"></textarea>
                    </label>
                </p>
                <div class="error">
                    ${error!}
                </div>
                <div class="button-field">
                    <input type="submit" value="Create">
                </div>
            </form>
        </div>
    </div>
    <script>
        $(function () {
            $(".create form").submit(function () {
                const title = $(this).find("input[name='title']").val();
                const text = $(this).find("input[name='text']").val();
                const $error = $(this).find(".error");

                ajax({
                    action: "create",
                    title,
                    text,
                    user: ${user}
                }, $error);

                return false;
            });
        })
    </script>
    <#else>
        <p>You must be logged, to create article</p>
    </#if>
</@c.page>
