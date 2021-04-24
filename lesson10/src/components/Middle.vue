<template>
    <div class="middle">
        <Sidebar :posts="viewPosts"/>
        <main>
            <Index v-if="page === 'Index'" :posts="posts" :users="users" :commentsSizes="commentsSizes"/>
            <Post v-if="page === 'Post'" :post="posts[postId]" :users="users" :comments="comments"/>
            <Enter v-if="page === 'Enter'"/>
            <Register v-if="page === 'Register'"/>
            <WritePost v-if="page === 'WritePost'"/>
            <EditPost v-if="page === 'EditPost'"/>
            <Users v-if="page === 'Users'" :users="users"/>
        </main>
    </div>
</template>

<script>
import Sidebar from "@/components/sidebar/Sidebar";
import Index from "@/components/middle/Index";
import Enter from "@/components/middle/Enter";
import WritePost from "@/components/middle/WritePost";
import EditPost from "@/components/middle/EditPost";
import Register from "@/components/middle/Register";
import Users from "@/components/middle/Users";
import Post from "@/components/middle/Post";

export default {
    name: "Middle",
    data: function () {
        return {
            page: "Index",
        }
    },
    components: {
        Users,
        WritePost,
        Post,
        Enter,
        Register,
        Index,
        Sidebar,
        EditPost
    },
    props: ["posts", "users", "commentsSizes", "comments", "postId"],
    computed: {
        viewPosts: function () {
            return Object.values(this.posts).sort((a, b) => b.id - a.id).slice(0, 2);
        }
    }, beforeCreate() {
        this.$root.$on("onChangePage", (page) => this.page = page)
    },
    methods: {

    }
}
</script>

<style scoped>

</style>