<template>
  <div>
    <div class="link-banner banner" :style="cover">
      <h1 class="banner-title">{{ $t("navBar.friendLink") }}</h1>
    </div>
    <v-card class="blog-container">
      <div class="link-title mb-1">
        <v-icon color="blue">mdi-link-variant</v-icon>
        {{ $t("friendLink.links") }}
      </div>
      <v-row class="link-container">
        <v-col
          class="link-wrapper"
          md="4"
          cols="12"
          v-for="item of friendLinkList"
          :key="item.id"
        >
          <a :href="item.linkUrl" target="_blank">
            <v-avatar size="65" class="link-avatar">
              <img :src="item.linkLogo" alt="" />
            </v-avatar>
            <div style="width:100%;z-index:10;">
              <div class="link-name">{{ item.linkName }}</div>
              <div class="link-intro">{{ item.linkDesc }}</div>
            </div>
          </a>
        </v-col>
      </v-row>
      <div class="link-title mt-4 mb-4">
        <v-icon color="blue">mdi-dots-horizontal-circle</v-icon>
        {{ $t("friendLink.add") }}
      </div>
      <blockquote>
        <div>{{ $t("friendLink.name") }}: 有一个地方, 只有你知道</div>
        <div>{{ $t("friendLink.desc") }}: 溺水三千, 只救一个</div>
        <div>
          {{ $t("friendLink.logo") }}: https://iksling.com/static/img/logo.png
        </div>
        <div>{{ $t("friendLink.link") }}: https://iksling.com</div>
      </blockquote>
      <div class="mt-5 mb-5">{{ $t("friendLink.text1") }}💖</div>
      <Comment
        :commentList="commentList"
        :count="count"
        @reloadComment="getComments"
      />
    </v-card>
  </div>
</template>

<script>
import Comment from "../../components/Comment";
export default {
  components: {
    Comment
  },
  created() {
    document.title = this.$t("navBar.friendLink");
    this.getFriendLinks();
    this.getComments();
  },
  data: function() {
    return {
      friendLinkList: [],
      commentList: [],
      count: 0
    };
  },
  methods: {
    getFriendLinks() {
      this.axios.get("/api/friendLinks").then(({ data }) => {
        this.friendLinkList = data.data;
      });
    },
    getComments() {
      this.axios
        .get("/api/comments", {
          params: { current: 1 }
        })
        .then(({ data }) => {
          this.commentList = data.data.pageList;
          this.count = data.data.count;
        });
    }
  },
  computed: {
    cover() {
      return (
        "background: url(" +
        this.$store.state.blogConfig.link_banner_cover +
        ") center center / cover no-repeat"
      );
    }
  },
  watch: {
    "$i18n.locale"() {
      document.title = this.$t("navBar.friendLink");
    }
  }
};
</script>

<style scoped>
blockquote {
  line-height: 2;
  margin: 0;
  font-size: 15px;
  border-left: 0.2rem solid #49b1f5;
  padding: 10px 1rem !important;
  background-color: #ecf7fe;
  border-radius: 4px;
}
.theme--dark blockquote {
  background-color: #000;
}
.link-banner {
  background: #49b1f5;
}
.link-title {
  color: #344c67;
  font-size: 21px;
  font-weight: bold;
  line-height: 2;
}
.link-container {
  margin: 10px 10px 0;
}
.link-wrapper {
  position: relative;
  transition: all 0.3s;
  border-radius: 8px;
}
.link-avatar {
  margin-top: 5px;
  margin-left: 10px;
  transition: all 0.5s;
}
@media (max-width: 759px) {
  .link-avatar {
    margin-left: 30px;
  }
}
.link-name {
  text-align: center;
  font-size: 1.25rem;
  font-weight: bold;
  z-index: 1000;
}
.link-intro {
  text-align: center;
  padding: 16px 10px;
  height: 50px;
  font-size: 13px;
  color: #1f2d3d;
  width: 100%;
}
.theme--dark .link-intro {
  color: #aaa;
}
.link-wrapper:hover a {
  color: #fff;
}
.link-wrapper:hover .link-intro {
  color: #fff;
}
.link-wrapper:hover .link-avatar {
  transform: rotate(360deg);
}
.link-wrapper a {
  color: #333;
  text-decoration: none;
  display: flex;
  height: 100%;
  width: 100%;
}
.link-wrapper:hover {
  box-shadow: 0 2px 20px #49b1f5;
}
.link-wrapper:hover:before {
  transform: scale(1);
}
.link-wrapper:before {
  position: absolute;
  border-radius: 8px;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background: #49b1f5 !important;
  content: "";
  transition-timing-function: ease-out;
  transition-duration: 0.3s;
  transition-property: transform;
  transform: scale(0);
}
</style>
