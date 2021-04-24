<#import "commons.ftlh" as c>

<@c.page>
    <div class="form-box">
        <div class="header">Add Notice</div>
        <div class="body">
            <form method="post" action="">
                <input type="hidden" name="action" id ="action" value="addNotice"/>
                <p>
                    <label>
                        <textarea class="content-area" name="content" cols="44" rows="16" required></textarea>
                    </label>
                </p>
                <div class="button-field">
                    <input type="submit" value="Add">
                </div>
            </form>
        </div>
    </div>
</@c.page>
