<template>
  <div>
    <div class="about-banner banner" :style="cover">
      <h1 class="banner-title">{{ $t("navBar.about") }}</h1>
    </div>
    <v-card class="blog-container">
      <div class="my-wrapper">
        <v-avatar size="110">
          <img
            class="author-avatar"
            :src="this.$store.state.bloggerInfo.avatar"
            alt=""
          />
        </v-avatar>
      </div>
      <div class="about-content markdown-body" v-html="aboutContent" />
    </v-card>
  </div>
</template>

<script>
export default {
  created() {
    document.title = this.$t("navBar.about");
    this.getAboutContent();
  },
  data: function() {
    return {
      aboutContent: ""
    };
  },
  methods: {
    getAboutContent() {
      this.axios.get("/api/about").then(({ data }) => {
        const MarkdownIt = require("markdown-it");
        const md = new MarkdownIt();
        this.aboutContent = md.render(data.data);
      });
    }
  },
  computed: {
    cover() {
      return (
        "background: url(" +
        this.$store.state.blogConfig.about_banner_cover +
        ") center center / cover no-repeat"
      );
    }
  },
  watch: {
    "$i18n.locale"() {
      document.title = this.$t("navBar.about");
    }
  }
};
</script>

<style scoped>
.about-banner {
  background: #49b1f5;
}
.about-content {
  word-break: break-word;
  line-height: 1.8;
  font-size: 14px;
}
.my-wrapper {
  text-align: center;
}
.author-avatar {
  transition: all 0.5s;
}
.author-avatar:hover {
  transform: rotate(360deg);
}
.theme--dark .blog-container {
  background-color: #1b1f23;
}
</style>
