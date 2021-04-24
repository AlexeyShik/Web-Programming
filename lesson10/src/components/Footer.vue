<template>
    <footer>
        <a href="/">Codeforces</a> &copy; 2020 by Mike Mirzayanov
        <div class="stats">
            <span>{{usersSize}} users ans {{postsSize}} posts</span>
        </div>
    </footer>
</template>

<script>
export default {
    name: "Footer",
    props: ["usersSize", "postsSize"]
}
</script>

<style scoped>

</style>