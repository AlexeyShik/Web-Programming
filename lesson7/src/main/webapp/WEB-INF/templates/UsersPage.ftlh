<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="user" type="ru.itmo.wp.model.domain.User" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <#if user?? && user.admin>
                    <th>Admin</th>
                </#if>
            </tr>
            </thead>
            <tbody>
            <template>
                <tr>
                    <td class="user_id"></td>
                    <td class="user_login"></td>
                    <#if user?? && user.admin>
                        <td>
                            <p class="admin"></p>
                            <a class="enable" type="link" href="#"></a>
                        </td>
                    </#if>
                </tr>
            </template>
            <tr class="noData">
                <td colspan="3">
                    <span>Loading...</span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <script>
        $(function () {
            const $noData = $(".users .noData");

            $.ajax({
                dataType: "json",
                data: {
                    action: "findAll"
                },
                success: function (response) {
                    $noData.find("span").text("No Data");

                    response["users"].forEach(function (user) {
                        $noData.hide();
                        const $tr = $($(".users template").prop("content")).clone();
                        $tr.find(".user_id").text(user["id"]);
                        $tr.find(".user_login").text(user["login"]);
                        $tr.find(".admin").text(user["admin"]);
                        $tr.find(".enable").text(user["admin"] ? "disable" : "enable");
                        if (response["user"] !== null && response["user"] !== undefined && user["id"] === response["user"]["id"]) {
                            $tr.find(".enable").remove();
                        }
                        $tr.find("tr").attr("data-userId", user["id"]);
                        $tr.find(".enable").attr({"id": user["id"]});
                        $tr.find(".admin").attr({"itemid": user["id"]});
                        $(".users tbody").append($tr);
                    });

                    $(".users tr").click(function () {
                        $.ajax({
                            dataType: "json",
                            data: {
                                action: "findUser",
                                userId: $(this).attr("data-userId")
                            },
                            success: function (response) {
                                notify(response["user"].creationTime);
                            },
                            type: "POST"
                        })
                    });

                    $(".users").on("click", ".enable", function () {
                        let $val = $(this);
                        $.ajax({
                            dataType: "json",
                            data: {
                                action: $val.text() === "enable" ? "setAdmin" : "unsetAdmin",
                                id: $(this).attr("id")
                            },
                            success: function (response) {
                                $val.parent().find(".admin").text(response["admin"]);
                                $val.text(response["admin"] ? "disable" : "enable");
                                notify("User with id = " + (response["id"]) + " is" + (response["admin"] ? "" : "n't") + " admin now");
                            },
                            type: "POST"
                        });
                    });
                },
                type: "POST"
            });
        })
    </script>
</@c.page>
