<template>
  <div>
    <article v-for="post in viewPosts" :key="post.id">
      <div class="title">
        <a href="#" @click.prevent="showPost(post.id)">{{ post.title }}</a>
      </div>
      <div class="information">By {{ users[post.userId].login }}</div>
      <div class="body">{{ post.text }}</div>
      <div class="footer">
        <div class="left">
          <img src="../../assets/img/voteup.png" alt="Vote Up" title="Vote Up"/>
          <span class="positive-score">+173</span>
          <img src="../../assets/img/votedown.png" alt="Vote Down" title="Vote Down"/>
        </div>
        <div class="right">
          <img src="../../assets/img/date_16x16.png" alt="Publish Time" title="Publish Time"/>
          <img src="../../assets/img/comments_16x16.png" class="comments" alt="Comments" title="Comments"/>
          <a href="#">{{ commentsSizes[post.id].count }}</a>
        </div>
      </div>
    </article>
  </div>
</template>

<script>
export default {
  name: "Index",
  props: ["posts", "users", "commentsSizes"],
  computed: {
    viewPosts: function () {
      this.$root.$emit("countCommentsSizes");
      return Object.values(this.posts).sort((a, b) => b.id - a.id);
    }
  },
  methods: {
    showPost: function (id) {
        this.$root.$emit("setPostId", id)
        this.$root.$emit("onChangePage", 'Post');
      }
  }
}
</script>

<style scoped>
article {
  margin-bottom: 2em;
}

article .title {
  color: var(--caption-color);
  font-weight: bold;
  font-size: 1.25rem;
}

article .information {
  margin-top: 0.25rem;
  font-size: 0.85rem;
  color: #888;
  margin-bottom: 0.25rem;
}

article .body {
  border-left: 4px solid var(--border-color);
  padding-left: 0.75rem;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}

article .body p:last-child {
  margin: 0;
}

article .attachment {
  padding: 0;
  margin: 0.5rem 0 0 0;
}

article .attachment li {
  list-style: none;
  padding: 0.25rem 20px;
  margin: 0;
  background: url("../../assets/img/paperclip-16x16.png") 0 2px no-repeat;
  font-size: 0.75rem;
  color: #888;
}

article .footer {
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  overflow: hidden;
  padding: 0.1rem;
  margin-top: 0.25rem;
}

article .footer .left {
  float: left;
  padding-left: 0.5rem;
}

article .footer .left img {
  position: relative;
  top: 5px;
}

article .footer .right img {
  position: relative;
  margin-left: 0.5rem;
  top: 2px;
}

article .footer .right {
  float: right;
  font-size: 0.85rem;
  line-height: 2rem;
  padding-right: 0.5rem;
}

article .footer .right .comments {
  padding-right: 0.2rem;
}

article .footer .positive-score {
  color: green;
  font-weight: bold;
  line-height: 1.75rem;
}
</style>