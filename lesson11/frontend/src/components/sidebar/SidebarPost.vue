<template>
  <section>
    <div class="header">
      {{ post.title }}
    </div>
    <div class="body">
      {{ post.text }}
    </div>
    <div class="footer">
      <a href="#" @click.prevent="showPost(post)">View all</a>
    </div>
  </section>
</template>

<script>
export default {
  name: "SidebarPost",
  props: ["post"],
  methods: {
    showPost: function (post) {
      this.$root.$emit("setPost", post)
      this.$root.$emit("onChangePage", 'Post');
    }
  }
}
</script>

<style scoped>
section {
  margin-bottom: 1rem;
}
</style>