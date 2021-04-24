<template>
    <div class="middle">
        <Sidebar :posts="viewPosts"/>
        <main>
            <Index v-if="page === 'Index'" :posts="posts" :users="users" :commentsSizes="commentsSizes"/>
            <Enter v-if="page === 'Enter'"/>
            <Register v-if="page === 'Register'"/>
            <Users v-if="page === 'Users'" :users="users"/>
            <WritePost v-if="page === 'WritePost'"/>
            <Post v-if="page === 'Post'" :post="post" :comments="comments"/>
        </main>
    </div>
</template>

<script>
import Sidebar from "@/components/sidebar/Sidebar";
import Index from "@/components/middle/Index";
import Users from "@/components/middle/Users";
import Enter from "@/components/middle/Enter";
import Register from "@/components/middle/Register";
import WritePost from "@/components/middle/WritePost";
import Post from "@/components/middle/Post";

export default {
    name: "Middle",
    data: function () {
        return {
            page: "Index"
        }
    },
    components: {
      WritePost,
        Post,
        Register,
        Enter,
        Index,
        Users,
        Sidebar
    },
    props: ["posts", "users", "comments", "commentsSizes", "post"],
    computed: {
        viewPosts: function () {
            return Object.values(this.posts).sort((a, b) => b.id - a.id).slice(0, 2);
        }
    }, beforeCreate() {
        this.$root.$on("onChangePage", (page) => this.page = page)
    }
}
</script>

<style scoped>

</style>