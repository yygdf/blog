<template>
  <div>
    <div class="home-banner" :style="cover">
      <div class="banner-container">
        <h1 class="blog-title animated zoomIn">
          {{ blogConfig.home_banner_title }}
        </h1>
        <div class="blog-intro">
          {{ obj.output }} <span class="typed-cursor">|</span>
        </div>
        <div class="blog-contact">
          <a
            target="_blank"
            class="iconfont my-icon-qq"
            :href="blogConfig.home_contact_qq"
          />
          <a
            target="_blank"
            :href="blogConfig.home_github"
            class="ml-5 mr-5 iconfont my-icon-github"
          />
          <a
            target="_blank"
            :href="blogConfig.home_gitee"
            class="iconfont my-icon-gitee"
          />
        </div>
      </div>
      <div class="scroll-down" @click="scrollDown">
        <v-icon color="#fff" class="scroll-down-effects">
          mdi-chevron-down
        </v-icon>
      </div>
    </div>
    <v-row class="home-container">
      <v-col md="9" cols="12">
        <v-card
          class="animated zoomIn article-card"
          style="border-radius: 12px 8px 8px 12px"
          v-for="(item, index) of articleList"
          :key="item.id"
        >
          <div :class="isRight(index)">
            <router-link :to="rootUri + '/article/' + item.id">
              <v-img
                class="on-hover"
                width="100%"
                height="100%"
                :src="
                  item.articleCover ? item.articleCover : defaultArticleCover
                "
              />
            </router-link>
          </div>
          <div class="article-wrapper">
            <div style="line-height:1.4">
              <router-link :to="rootUri + '/article/' + item.id">
                {{ item.articleTitle }}
              </router-link>
            </div>
            <div class="article-info">
              <span v-if="item.topFlag">
                <span style="color:#ff7242">
                  <i class="iconfont my-icon-top" /> {{ $t("home.top") }}
                </span>
                <span class="separator">|</span>
              </span>
              <span v-if="item.hiddenFlag">
                <span style="color:#444444">
                  <i class="iconfont my-icon-open-eye" />
                  {{ $t("home.hidden") }}
                </span>
                <span class="separator">|</span>
              </span>
              <span v-else-if="!item.publicFlag">
                <span style="color:#555555">
                  <i class="iconfont my-icon-open-eye" />
                  {{ $t("home.locked") }}
                </span>
                <span class="separator">|</span>
              </span>
              <v-icon size="14">mdi-calendar-month-outline</v-icon>
              {{ item.publishTime | date }}
              <span class="separator">|</span>
              <router-link :to="rootUri + '/category/' + item.categoryId">
                <v-icon size="14">mdi-inbox-full</v-icon>
                {{ item.categoryName }}
              </router-link>
              <span v-if="item.tagList != null" class="separator">|</span>
              <router-link
                style="display:inline-block"
                :to="rootUri + '/tag/' + tag.id"
                class="mr-1"
                v-for="tag of item.tagList == null
                  ? []
                  : item.tagList.split(',').map(e => {
                      let tagArr = e.split('=');
                      return {
                        id: tagArr[0],
                        tagName: tagArr[1]
                      };
                    })"
                :key="tag.id"
              >
                <v-icon size="14">mdi-tag-multiple</v-icon>{{ tag.tagName }}
              </router-link>
            </div>
            <div class="article-content">
              {{ item.articleContent }}
            </div>
          </div>
        </v-card>
        <infinite-loading @infinite="infiniteHandler">
          <div slot="no-more" />
        </infinite-loading>
      </v-col>
      <v-col md="3" cols="12" class="d-md-block d-none">
        <div class="blog-wrapper">
          <v-card class="animated zoomIn blog-card mt-5">
            <div class="author-wrapper">
              <v-avatar size="110">
                <img class="author-avatar" :src="bloggerInfo.avatar" alt="" />
              </v-avatar>
              <div style="font-size: 1.375rem">{{ bloggerInfo.nickname }}</div>
              <div style="font-size: 0.875rem;">{{ bloggerInfo.intro }}</div>
            </div>
            <div class="blog-info-wrapper">
              <div class="blog-info-data">
                <router-link :to="rootUri + '/archives'">
                  <div style="font-size: 0.875rem">
                    {{ $t("navBar.article") }}
                  </div>
                  <div style="font-size: 1.25rem">
                    {{ bloggerInfo.articleCount }}
                  </div>
                </router-link>
              </div>
              <div class="blog-info-data">
                <router-link :to="rootUri + '/categories'">
                  <div style="font-size: 0.875rem">
                    {{ $t("navBar.category") }}
                  </div>
                  <div style="font-size: 1.25rem">
                    {{ bloggerInfo.categoryCount }}
                  </div>
                </router-link>
              </div>
              <div class="blog-info-data">
                <router-link :to="rootUri + '/tags'">
                  <div style="font-size: 0.875rem">{{ $t("navBar.tag") }}</div>
                  <div style="font-size: 1.25rem">
                    {{ bloggerInfo.tagCount }}
                  </div>
                </router-link>
              </div>
            </div>
            <a class="collection-btn" @click="tip = true">
              <v-icon color="#fff" size="18" class="mr-1">mdi-bookmark</v-icon>
              {{ $t("home.bookmark") }}
            </a>
            <div class="card-info-social">
              <a
                target="_blank"
                class="iconfont my-icon-qq"
                :href="blogConfig.home_contact_qq"
              />
              <a
                target="_blank"
                class="ml-5 mr-5 iconfont my-icon-github"
                :href="blogConfig.home_github"
              />
              <a
                target="_blank"
                class="iconfont my-icon-gitee"
                :href="blogConfig.home_gitee"
              />
            </div>
          </v-card>
          <v-card class="blog-card animated zoomIn mt-5 big">
            <div class="web-info-title">
              <v-icon size="18">mdi-bell</v-icon>
              {{ $t("home.notice") }}
            </div>
            <div style="font-size:0.875rem">
              {{ blogConfig.home_notice }}
            </div>
          </v-card>
          <v-card class="blog-card animated zoomIn mt-5">
            <div class="web-info-title">
              <v-icon size="18">mdi-chart-line</v-icon>
              {{ $t("home.websiteInfo") }}
            </div>
            <div class="web-info">
              <div style="padding:4px 0 0">
                {{ $t("home.runtime") }}:<span class="float-right">{{
                  time
                }}</span>
              </div>
              <div style="padding:4px 0 0">
                {{ $t("home.totalVisits") }}:<span class="float-right">
                  {{ bloggerInfo.viewCount }}
                </span>
              </div>
            </div>
          </v-card>
        </div>
      </v-col>
    </v-row>
    <v-snackbar v-model="tip" top color="#49b1f5" :timeout="2000">
      {{ $t("home.tip") }}
    </v-snackbar>
  </div>
</template>

<script>
import EasyTyper from "easy-typer-js";
import english from "../../assets/js/english";
export default {
  created() {
    this.init();
    document.title = this.$t("navBar.home");
    setInterval(this.runTime, 1000);
  },
  data: function() {
    return {
      tip: false,
      time: "",
      obj: {
        output: "",
        isEnd: false,
        speed: 300,
        singleBack: false,
        sleep: 0,
        type: "rollback",
        backSpeed: 40,
        sentencePause: true
      },
      articleList: [],
      current: 1,
      defaultArticleCover: this.$store.state.blogConfig.article_default_cover
    };
  },
  methods: {
    init() {
      document.title = this.$route.meta.title;
      if (this.$i18n.locale === "en_US") {
        this.obj.speed = 100;
        this.initTyped(
          english.series[Math.floor(Math.random() * english.series.length)]
        );
      } else {
        this.obj.speed = 300;
        fetch("https://v1.hitokoto.cn?c=i")
          .then(res => {
            return res.json();
          })
          .then(({ hitokoto }) => {
            this.initTyped(hitokoto);
          });
      }
    },
    initTyped(input, fn, hooks) {
      const obj = this.obj;
      // eslint-disable-next-line no-unused-vars
      const typed = new EasyTyper(obj, input, fn, hooks);
    },
    scrollDown() {
      window.scrollTo({
        behavior: "smooth",
        top: document.documentElement.clientHeight
      });
    },
    runTime() {
      let timeOld =
        new Date().getTime() - new Date("December 12,2022").getTime();
      let msPerDay = 24 * 60 * 60 * 1000;
      let daysOld = Math.floor(timeOld / msPerDay);
      let str = "";
      let day = new Date();
      str += daysOld + this.$t("home.day") + " ";
      str += day.getHours() + this.$t("home.hour") + " ";
      str += day.getMinutes() + this.$t("home.minute") + " ";
      str += day.getSeconds() + this.$t("home.second");
      this.time = str;
    },
    infiniteHandler($state) {
      let md = require("markdown-it")();
      this.axios
        .get("/api/articles", {
          params: {
            current: this.current
          }
        })
        .then(({ data }) => {
          if (data.data.length) {
            data.data.forEach(item => {
              item.articleContent = md
                .render(item.articleContent)
                .replace(/<\/?[^>]*>/g, "")
                .replace(/[|]*\n/, "")
                .replace(/&npsp;/gi, "");
            });
            this.articleList.push(...data.data);
            this.current++;
            $state.loaded();
          } else {
            $state.complete();
          }
        });
    }
  },
  computed: {
    isRight() {
      return function(index) {
        if (index % 2 === 0) {
          return "article-cover left-radius";
        }
        return "article-cover right-radius";
      };
    },
    cover() {
      return (
        "background: url(" +
        this.blogConfig.home_banner_cover +
        ") center center / cover no-repeat"
      );
    },
    blogConfig() {
      return this.$store.state.blogConfig;
    },
    bloggerInfo() {
      return this.$store.state.bloggerInfo;
    },
    rootUri() {
      return this.$store.state.rootUri;
    }
  },
  watch: {
    "$i18n.locale"() {
      document.title = this.$t("navBar.home");
    }
  }
};
</script>

<style lang="stylus">
.typed-cursor
  opacity: 1
  animation: blink 0.7s infinite
@keyframes blink
  0%
    opacity: 1
  50%
    opacity: 0
  100%
    opacity: 1
</style>

<style scoped>
.home-banner {
  position: absolute;
  top: -60px;
  left: 0;
  right: 0;
  height: 100vh;
  background: #49b1f5;
  text-align: center;
  color: #fff !important;
  animation: header-effect 1s;
}
.banner-container {
  margin-top: 43vh;
  line-height: 1.5;
  color: #eee;
}
.blog-contact a {
  color: #fff !important;
}
.card-info-social {
  line-height: 40px;
  text-align: center;
  font-size: 1.5rem;
  margin: 6px 0 -6px;
}
.left-radius {
  border-radius: 8px 0 0 8px !important;
  order: 0;
}
.right-radius {
  border-radius: 0 8px 8px 0 !important;
  order: 1;
}
.article-wrapper {
  font-size: 14px;
}
@media (min-width: 760px) {
  .blog-title {
    font-size: 2.5rem;
  }
  .blog-intro {
    font-size: 1.5rem;
  }
  .blog-contact {
    display: none;
  }
  .home-container {
    max-width: 1200px;
    margin: calc(100vh - 48px) auto 28px auto;
    padding: 0 5px;
  }
  .article-card {
    display: flex;
    align-items: center;
    height: 280px;
    width: 100%;
    margin-top: 20px;
  }
  .article-cover {
    overflow: hidden;
    height: 100%;
    width: 45%;
  }
  .on-hover {
    transition: all 0.6s;
  }
  .article-card:hover .on-hover {
    transform: scale(1.1);
  }
  .article-wrapper {
    padding: 0 2.5rem;
    width: 55%;
  }
  .article-wrapper a {
    font-size: 1.5rem;
    transition: all 0.3s;
  }
}
@media (max-width: 759px) {
  .blog-title {
    font-size: 26px;
  }
  .blog-contact {
    font-size: 1.25rem;
    line-height: 2;
  }
  .home-container {
    width: 100%;
    margin: calc(100vh - 66px) auto 0 auto;
  }
  .article-card {
    margin-top: 1rem;
  }
  .article-cover {
    border-radius: 8px 8px 0 0 !important;
    height: 230px !important;
    width: 100%;
  }
  .article-cover div {
    border-radius: 8px 8px 0 0 !important;
  }
  .article-wrapper {
    padding: 1.25rem 1.25rem 1.875rem;
  }
  .article-wrapper a {
    font-size: 1.25rem;
    transition: all 0.3s;
  }
}
.scroll-down {
  cursor: pointer;
  position: absolute;
  bottom: 0;
  width: 100%;
}
.scroll-down i {
  font-size: 2rem;
}
.article-wrapper a:hover {
  color: #8e8cd8;
}
.article-info {
  font-size: 95%;
  color: #858585;
  line-height: 2;
  margin: 0.375rem 0;
}
.article-info a {
  font-size: 95%;
  color: #858585 !important;
}
.article-content {
  line-height: 2;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
.blog-wrapper {
  position: sticky;
  top: 10px;
}
.blog-card {
  line-height: 2;
  padding: 1.25rem 1.5rem;
}
.author-wrapper {
  text-align: center;
}
.blog-info-wrapper {
  display: flex;
  justify-self: center;
  padding: 0.875rem 0;
}
.blog-info-data {
  flex: 1;
  text-align: center;
}
.blog-info-data a {
  text-decoration: none;
}
.collection-btn {
  text-align: center;
  z-index: 1;
  font-size: 14px;
  position: relative;
  display: block;
  background-color: #49b1f5;
  color: #fff !important;
  height: 32px;
  line-height: 32px;
  transition-duration: 1s;
  transition-property: color;
}
.collection-btn:before {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: -1;
  background: #ff7242;
  content: "";
  transition-timing-function: ease-out;
  transition-duration: 0.5s;
  transition-property: transform;
  transform: scaleX(0);
  transform-origin: 0 50%;
}
.collection-btn:hover:before {
  transition-timing-function: cubic-bezier(0.45, 1.64, 0.47, 0.66);
  transform: scaleX(1);
}
.author-avatar {
  transition: all 0.5s;
}
.author-avatar:hover {
  transform: rotate(360deg);
}
.web-info {
  padding: 0.25rem;
  font-size: 0.875rem;
}
.scroll-down-effects {
  color: #eee !important;
  text-align: center;
  text-shadow: 0.1rem 0.1rem 0.2rem rgba(0, 0, 0, 0.15);
  line-height: 1.5;
  display: inline-block;
  text-rendering: auto;
  -webkit-font-smoothing: antialiased;
  animation: scroll-down-effect 1.5s infinite;
}
@keyframes scroll-down-effect {
  0% {
    top: 0;
    opacity: 0.4;
    filter: alpha(opacity=40);
  }
  50% {
    top: -16px;
    opacity: 1;
    filter: none;
  }
  100% {
    top: 0;
    opacity: 0.4;
    filter: alpha(opacity=40);
  }
}
.big i {
  color: #f00;
  animation: big 0.8s linear infinite;
}
@keyframes big {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
}
</style>
