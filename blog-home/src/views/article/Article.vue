<template>
  <div>
    <div class="banner" :style="articleCover">
      <div class="article-info-container">
        <div class="article-title">{{ article.articleTitle }}</div>
        <div class="article-info">
          <div class="first-line">
            <span>
              <i class="iconfont my-icon-calendar" />
              {{ $t("article.publish") }} {{ article.publishTime | date }}
            </span>
            <span class="separator">|</span>
            <span>
              <i class="iconfont my-icon-update-time" />
              {{ $t("article.update") }}
              <template v-if="article.updateTime">
                {{ article.updateTime | date }}
              </template>
              <template v-else>
                {{ article.publishTime | date }}
              </template>
            </span>
            <span class="separator">|</span>
            <span class="article-category">
              <i class="iconfont my-icon-sort-1" />
              <router-link :to="rootUri + '/category/' + article.categoryId">
                {{ article.categoryName }}
              </router-link>
            </span>
          </div>
          <div class="second-line">
            <span>
              <i class="iconfont my-icon-word" />
              {{ $t("article.word") }}: {{ wordNum | num }}
            </span>
            <span class="separator">|</span>
            <span>
              <i class="iconfont my-icon-time" />
              {{ $t("article.read") }}: {{ readTime }}
            </span>
          </div>
          <div class="third-line">
            <span class="separator">|</span>
            <span>
              <i class="iconfont my-icon-open-eye" /> {{ $t("article.view") }}:
              {{ article.viewCount }}
            </span>
            <span class="separator">|</span>
            <span>
              <i class="iconfont my-icon-comment-group-1" />{{
                $t("article.comment")
              }}:
              <template v-if="count">{{ count }}</template>
              <template v-else>0</template>
            </span>
          </div>
        </div>
      </div>
    </div>
    <v-row class="article-container">
      <v-col md="9" cols="12">
        <v-card class="article-wrapper">
          <article
            id="write"
            class="article-content markdown-body"
            v-html="article.articleContent"
            ref="article"
          />
          <div class="article-copyright">
            <div>
              <span>{{ $t("article.author") }}: </span>
              <a :href="bloggerHref" target="_blank">{{
                bloggerInfo.nickname
              }}</a>
            </div>
            <div>
              <span>{{ $t("article.link") }}: </span>
              <a :href="articleHref" target="_blank">{{ articleHref }} </a>
            </div>
            <div>
              <span>{{ $t("article.copyright") }}: </span
              >{{ $t("article.text1") }}
              <a
                href="https://creativecommons.org/licenses/by-nc-sa/4.0/"
                target="_blank"
              >
                CC BY-NC-SA 4.0
              </a>
              {{ $t("article.text2") }}
            </div>
          </div>
          <div class="article-operation">
            <div class="tag-container">
              <router-link
                v-for="tag of article.tagList == null
                  ? []
                  : article.tagList.split(',').map(e => {
                      let tagArr = e.split('=');
                      return {
                        id: tagArr[0],
                        tagName: tagArr[1]
                      };
                    })"
                :key="tag.id"
                :to="rootUri + '/tag/' + tag.id"
              >
                {{ tag.tagName }}
              </router-link>
            </div>
            <share style="margin-left:auto" :config="config" />
          </div>
          <div class="article-reward">
            <a :class="isLike" @click="like">
              <v-icon size="14" color="#fff">mdi-thumb-up</v-icon>
              {{ $t("button.like") }}
              <span v-show="article.likeCount > 0">{{
                article.likeCount
              }}</span>
            </a>
            <a class="reward-btn">
              <i class="iconfont my-icon-QR-code" /> {{ $t("button.reward") }}
              <div class="animated fadeIn reward-main">
                <ul class="reward-all">
                  <li class="reward-item">
                    <img
                      class="reward-img"
                      :src="blogConfig.wx_pay_code"
                      alt=""
                    />
                    <div class="reward-desc">{{ $t("article.wx") }}</div>
                  </li>
                  <li class="reward-item">
                    <img
                      class="reward-img"
                      :src="blogConfig.ali_pay_code"
                      alt=""
                    />
                    <div class="reward-desc">{{ $t("article.pay") }}</div>
                  </li>
                </ul>
              </div>
            </a>
            <a
              v-if="article.permitFlag === false"
              class="unlock-btn"
              @click="openUnlockModel"
            >
              <v-icon size="16" color="#fff">mdi-archive-lock-open</v-icon>
              {{ $t("button.unlock") }}
            </a>
          </div>
          <div class="pagination-post">
            <div
              :class="isFull(article.lastArticle.id)"
              v-if="article.lastArticle"
            >
              <router-link :to="rootUri + '/article/' + article.lastArticle.id">
                <img
                  class="post-cover"
                  :src="
                    article.lastArticle.articleCover
                      ? article.lastArticle.articleCover
                      : defaultArticleCover
                  "
                  alt=""
                />
                <div class="post-info">
                  <div class="label">{{ $t("article.prev") }}</div>
                  <div class="post-title">
                    {{ article.lastArticle.articleTitle }}
                  </div>
                </div>
              </router-link>
            </div>
            <div
              :class="isFull(article.nextArticle.id)"
              v-if="article.nextArticle"
            >
              <router-link :to="rootUri + '/article/' + article.nextArticle.id">
                <img
                  class="post-cover"
                  :src="
                    article.nextArticle.articleCover
                      ? article.nextArticle.articleCover
                      : defaultArticleCover
                  "
                  alt=""
                />
                <div class="post-info" style="text-align: right">
                  <div class="label">{{ $t("article.next") }}</div>
                  <div class="post-title">
                    {{ article.nextArticle.articleTitle }}
                  </div>
                </div>
              </router-link>
            </div>
          </div>
          <div
            class="recommend-container"
            v-if="article.articlesRecommendDTOList"
          >
            <div class="recommend-title">
              <v-icon size="20" color="#4c4948">mdi-thumb-up</v-icon>
              {{ $t("article.recommendation") }}
            </div>
            <div class="recommend-list">
              <div
                class="recommend-item"
                v-for="item of article.articlesRecommendDTOList"
                :key="item.id"
              >
                <router-link :to="rootUri + '/article/' + item.id">
                  <img
                    class="recommend-cover"
                    :src="
                      item.articleCover
                        ? item.articleCover
                        : defaultArticleCover
                    "
                    alt=""
                  />
                  <div class="recommend-info">
                    <div class="recommend-date">
                      <i class="iconfont my-icon-calendar" />
                      {{ item.publishTime | date }}
                    </div>
                    <div>{{ item.articleTitle }}</div>
                  </div>
                </router-link>
              </div>
            </div>
          </div>
          <hr />
          <comment
            :commentList="commentList"
            :count="count"
            @reloadComment="getComments"
          />
        </v-card>
      </v-col>
      <v-col md="3" cols="12" class="d-md-block d-none">
        <div style="position: sticky;top: 20px;">
          <v-card class="right-container">
            <div class="right-title">
              <i class="iconfont my-icon-hamburger" style="font-size:16px" />
              <span style="margin-left:10px">{{ $t("article.contents") }}</span>
            </div>
            <div id="toc" />
          </v-card>
          <v-card class="right-container" style="margin-top:20px">
            <div class="right-title">
              <i class="iconfont my-icon-update-time" style="font-size:16px" />
              <span style="margin-left:10px">{{ $t("article.latest") }}</span>
            </div>
            <div class="article-list">
              <div
                class="article-item"
                v-for="item of articleNewestList"
                :key="item.id"
              >
                <router-link
                  :to="rootUri + '/article/' + item.id"
                  class="content-cover"
                >
                  <img
                    :src="
                      item.articleCover
                        ? item.articleCover
                        : defaultArticleCover
                    "
                    alt=""
                  />
                </router-link>
                <div class="content">
                  <div class="content-title">
                    <router-link :to="rootUri + '/article/' + item.id">
                      {{ item.articleTitle }}
                    </router-link>
                  </div>
                  <div class="content-time">{{ item.publishTime | date }}</div>
                </div>
              </div>
            </div>
          </v-card>
        </div>
      </v-col>
    </v-row>
    <v-dialog
      v-model="unlockModelFlag"
      :fullscreen="this.$store.state.mobileFlag"
      max-width="460"
    >
      <v-card class="login-container" style="border-radius:4px">
        <v-icon class="float-right" @click="unlockModelFlag = false">
          mdi-close
        </v-icon>
        <div class="login-wrapper">
          <v-text-field
            v-model="token"
            :label="$t('article.key')"
            autofocus="autofocus"
            :placeholder="$t('article.inputKey')"
            @keyup.enter="unlockArticle"
            clearable
          />
          <v-btn
            class="mt-7"
            color="red"
            style="color:#fff"
            @click="unlockArticle"
            :disabled="token == null || !token.trim()"
            block
          >
            {{ $t("button.unlock") }}
          </v-btn>
        </div>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import Clipboard from "clipboard";
import Comment from "../../components/Comment";
import tocbot from "tocbot";
export default {
  components: {
    Comment
  },
  created() {
    this.getArticle();
    this.getComments();
    this.getArticlesNewest();
  },
  destroyed() {
    if (this.clipboard) {
      this.clipboard.destroy();
    }
    tocbot.destroy();
  },
  data: function() {
    return {
      config: {
        sites: ["qzone", "wechat", "qq"]
      },
      imgList: [],
      article: {},
      articleNewestList: [],
      commentList: [],
      count: 0,
      wordNum: "",
      readTime: "",
      articleHref: location.href,
      bloggerHref: this.$store.state.rootUrl + this.$store.state.rootUri,
      clipboard: null,
      defaultArticleCover: this.$store.state.blogConfig.article_default_cover,
      unlockModelFlag: false,
      token: ""
    };
  },
  methods: {
    openUnlockModel() {
      if (this.$store.state.userId == null) {
        this.$store.state.loginFlag = true;
        return false;
      }
      this.token = "";
      this.unlockModelFlag = true;
    },
    getArticle() {
      let pathArr = this.$route.path.split("/");
      this.axios
        .get("/api/article/" + pathArr[pathArr.length - 1])
        .then(({ data }) => {
          if (data.data) {
            this.article = data.data;
            document.title = this.article.articleTitle;
            if (this.article.permitFlag !== false && data.data.articleContent) {
              this.markdownToHtml();
              this.articleRender();
            }
          } else {
            this.$router.push({
              path: this.rootUri === "" ? "/" : this.rootUri
            });
          }
        });
    },
    getComments() {
      let pathArr = this.$route.path.split("/");
      let params = {
        categoryId: pathArr[pathArr.length - 1]
      };
      this.axios
        .get("/api/comments", {
          params
        })
        .then(({ data }) => {
          this.commentList = data.data.pageList;
          this.count = data.data.count;
        });
    },
    getArticlesNewest() {
      this.axios.get("/api/articles/newest").then(({ data }) => {
        this.articleNewestList = data.data;
      });
    },
    like() {
      if (this.$store.state.userId == null) {
        this.$store.state.loginFlag = true;
        return false;
      }
      this.axios
        .post("/api/article/like/" + this.article.id)
        .then(({ data }) => {
          if (data.flag) {
            if (
              this.$store.state.articleLikeSet.indexOf(this.article.id) !== -1
            ) {
              this.$set(this.article, "likeCount", this.article.likeCount - 1);
              this.$store.commit("articleUnLike", this.article.id);
            } else {
              this.$set(this.article, "likeCount", this.article.likeCount + 1);
              this.$store.commit("articleLike", this.article.id);
            }
          }
        });
    },
    markdownToHtml() {
      const MarkdownIt = require("markdown-it");
      const hljs = require("highlight.js");
      const md = new MarkdownIt({
        html: true,
        linkify: true,
        typographer: true,
        highlight: function(str, lang) {
          let d = new Date().getTime();
          if (
            window.performance &&
            typeof window.performance.now === "function"
          ) {
            d += performance.now();
          }
          const codeIndex = "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(
            /[xy]/g,
            function(c) {
              let r = (d + Math.random() * 16) % 16 | 0;
              d = Math.floor(d / 16);
              return (c === "x" ? r : (r & 0x3) | 0x8).toString(16);
            }
          );
          let html = `<button class="copy-btn iconfont my-icon-copy" type="button" data-clipboard-action="copy" data-clipboard-target="#copy${codeIndex}"></button>`;
          str = str.substring(0, str.length - 1);
          const linesLength = str.split(/\n/).length;
          let linesNum = '<span aria-hidden="true" class="line-numbers-rows">';
          for (let index = 0; index < linesLength; index++) {
            linesNum = linesNum + "<span></span>";
          }
          linesNum += "</span>";
          if (lang && hljs.getLanguage(lang)) {
            const preCode = hljs.highlight(lang, str, true).value;
            html = html + preCode;
            if (linesLength) {
              html += '<b class="name">' + lang + "</b>";
            }
            return `<pre class="hljs"><code>${html}</code>${linesNum}</pre><textarea style="position: absolute;left: -1000px;z-index: -1000" id="copy${codeIndex}">${str
              .replace(/<\/textarea>/g, "</textarea>")
              .trim()}</textarea>`;
          }
        }
      });
      this.article.articleContent = md.render(this.article.articleContent);
    },
    articleRender() {
      this.$nextTick(() => {
        this.wordNum = this.deleteHTMLTag(this.article.articleContent).length;
        this.readTime =
          Math.round(this.wordNum / 400) + this.$t("article.minutes");
        this.clipboard = new Clipboard(".copy-btn");
        this.clipboard.on("success", () => {
          this.$toast({ type: "success", message: this.$t("article.copy") });
        });
        this.clipboard.on("error", () => {
          this.$toast({
            type: "error",
            message: this.$t("article.text3")
          });
        });
        let nodes = this.$refs.article.children;
        if (nodes.length) {
          for (let i = 0; i < nodes.length; i++) {
            let node = nodes[i];
            let reg = /^H[1-4]$/;
            if (reg.exec(node.tagName)) {
              node.id = i;
            }
          }
        }
        tocbot.init({
          tocSelector: "#toc",
          contentSelector: ".article-content",
          headingSelector: "h1, h2, h3",
          hasInnerContainers: true,
          onClick: function(e) {
            e.preventDefault();
          }
        });
        const imgList = this.$refs.article.getElementsByTagName("img");
        const that = this;
        for (let i = 0; i < imgList.length; i++) {
          this.imgList.push(imgList[i].src);
          imgList[i].addEventListener("click", function(e) {
            that.previewImg(e.target.currentSrc);
          });
        }
      });
    },
    previewImg(img) {
      this.$imagePreview({
        images: this.imgList,
        index: this.imgList.indexOf(img)
      });
    },
    deleteHTMLTag(content) {
      return content
        .replace(/<\/?[^>]*>/g, "")
        .replace(/[|]*\n/, "")
        .replace(/&npsp;/gi, "");
    },
    unlockArticle() {
      let param = {
        id: this.article.id,
        accessToken: this.token
      };
      this.axios.post("/api/blog/token", param).then(({ data }) => {
        if (data.flag) {
          this.article.articleContent = data.data.toString();
          this.markdownToHtml();
          this.articleRender();
          this.$toast({ type: "success", message: this.$t("article.text4") });
          this.unlockModelFlag = false;
          this.article.permitFlag = true;
        }
      });
    }
  },
  computed: {
    articleCover() {
      let cover =
        this.article.articleCover === ""
          ? this.blogConfig.article_default_cover
          : this.article.articleCover;
      return "background: url(" + cover + ") center center / cover no-repeat";
    },
    isLike() {
      let articleLikeSet = this.$store.state.articleLikeSet;
      return articleLikeSet.indexOf(this.article.id) !== -1
        ? "like-btn-active"
        : "like-btn";
    },
    isFull() {
      return function(id) {
        return id ? "post full" : "post";
      };
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
  }
};
</script>

<style scoped>
.banner:before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}
.article-info i {
  font-size: 14px;
}
.article-info {
  font-size: 14px;
  line-height: 1.9;
  display: inline-block;
}
@media (min-width: 760px) {
  .banner {
    color: #eee !important;
  }
  .article-info span {
    font-size: 95%;
  }
  .article-info-container {
    position: absolute;
    bottom: 6.25rem;
    padding: 0 8%;
    width: 100%;
    text-align: center;
  }
  .second-line,
  .third-line {
    display: inline;
  }
  .article-title {
    font-size: 35px;
    margin: 20px 0 8px;
  }
  .pagination-post {
    display: flex;
  }
  .post {
    width: 50%;
  }
  .recommend-item {
    position: relative;
    display: inline-block;
    overflow: hidden;
    margin: 3px;
    width: calc(33.333% - 6px);
    height: 200px;
    background: #000;
    vertical-align: bottom;
  }
}
@media (max-width: 759px) {
  .banner {
    color: #eee !important;
    height: 360px;
  }
  .article-info span {
    font-size: 90%;
  }
  .separator:first-child {
    display: none;
  }
  .blog-container {
    margin: 322px 5px 0 5px;
  }
  .article-info-container {
    position: absolute;
    bottom: 1.3rem;
    padding: 0 5%;
    width: 100%;
    color: #eee;
    text-align: left;
  }
  .article-title {
    font-size: 1.5rem;
    margin-bottom: 0.4rem;
  }
  .post {
    width: 100%;
  }
  .pagination-post {
    display: block;
  }
  .recommend-item {
    position: relative;
    display: inline-block;
    overflow: hidden;
    width: calc(100% - 4px);
    height: 150px;
    margin: 2px;
    background: #000;
    vertical-align: bottom;
  }
}
.article-content {
  word-break: break-word;
  font-size: 14px;
  line-height: 2;
}
.article-operation {
  display: flex;
  align-items: center;
}
.article-category a {
  color: #fff !important;
}
.tag-container a {
  display: inline-block;
  margin: 0.5rem 0.5rem 0.5rem 0;
  padding: 0 0.75rem;
  width: fit-content;
  border: 1px solid #49b1f5;
  border-radius: 1rem;
  color: #49b1f5 !important;
  font-size: 12px;
  line-height: 2;
}
.tag-container a:hover {
  color: #fff !important;
  background: #49b1f5;
  transition: all 0.5s;
}
.article-copyright {
  position: relative;
  margin-top: 40px;
  margin-bottom: 10px;
  font-size: 0.875rem;
  line-height: 2;
  padding: 0.625rem 1rem;
  border: 1px solid #eee;
}
.article-copyright span {
  color: #49b1f5;
  font-weight: bold;
}
.article-copyright a {
  text-decoration: underline !important;
  color: #99a9bf !important;
}
.article-copyright:before {
  position: absolute;
  top: 0.7rem;
  right: 0.7rem;
  width: 1rem;
  height: 1rem;
  border-radius: 1rem;
  background: #49b1f5;
  content: "";
}
.article-copyright:after {
  position: absolute;
  top: 0.95rem;
  right: 0.95rem;
  width: 0.5rem;
  height: 0.5rem;
  border-radius: 0.5em;
  background: #fff;
  content: "";
}
.article-reward {
  margin-top: 2rem;
  display: flex;
  justify-content: center;
  align-items: center;
}
.reward-btn {
  position: relative;
  display: inline-block;
  width: 100px;
  background: #49b1f5;
  margin: 0 1rem;
  color: #fff !important;
  text-align: center;
  line-height: 36px;
  font-size: 0.875rem;
}
.reward-btn:hover .reward-main {
  display: block;
}
.reward-main {
  display: none;
  position: absolute;
  bottom: 40px;
  left: 0;
  margin: 0;
  padding: 0 0 15px;
  width: 100%;
}
.reward-all {
  display: inline-block;
  margin: 0 0 0 -110px;
  padding: 20px 10px 8px !important;
  width: 320px;
  border-radius: 4px;
  background: #f5f5f5;
}
@media (max-width: 759px) {
  .reward-all {
    display: inline-block;
    margin: 0 0 0 -50px;
    padding: 20px 10px 8px !important;
    width: 200px;
    border-radius: 4px;
    background: #f5f5f5;
  }
}
.reward-all:before {
  position: absolute;
  bottom: -10px;
  left: 0;
  width: 100%;
  height: 20px;
  content: "";
}
.reward-all:after {
  content: "";
  position: absolute;
  right: 0;
  bottom: 2px;
  left: 0;
  margin: 0 auto;
  width: 0;
  height: 0;
  border-top: 13px solid #f5f5f5;
  border-right: 13px solid transparent;
  border-left: 13px solid transparent;
}
.reward-item {
  display: inline-block;
  padding: 0 8px;
  list-style-type: none;
}
.reward-img {
  width: 130px;
  height: 130px;
  display: block;
}
.reward-desc {
  margin: -5px 0;
  color: #858585;
  text-align: center;
}
.like-btn {
  display: inline-block;
  width: 100px;
  background: #969696;
  color: #fff !important;
  text-align: center;
  line-height: 36px;
  font-size: 0.875rem;
}
.like-btn-active {
  display: inline-block;
  width: 100px;
  background: #ec7259;
  color: #fff !important;
  text-align: center;
  line-height: 36px;
  font-size: 0.875rem;
}
.unlock-btn {
  display: inline-block;
  width: 100px;
  background: #ff5555;
  color: #fff !important;
  text-align: center;
  line-height: 36px;
  font-size: 0.875rem;
}
.pagination-post {
  margin-top: 40px;
  overflow: hidden;
  width: 100%;
  background: #000;
}
.post {
  position: relative;
  height: 150px;
  overflow: hidden;
}
.post-info {
  position: absolute;
  top: 50%;
  padding: 20px 40px;
  width: 100%;
  transform: translate(0, -50%);
  line-height: 2;
  font-size: 14px;
}
.post-cover {
  position: absolute;
  width: 100%;
  height: 100%;
  opacity: 0.4;
  transition: all 0.6s;
  object-fit: cover;
}
.post a {
  position: relative;
  display: block;
  overflow: hidden;
  height: 150px;
}
.post:hover .post-cover {
  opacity: 0.8;
  transform: scale(1.1);
}
.label {
  font-size: 90%;
  color: #eee;
}
.post-title {
  font-weight: 500;
  color: #fff;
}
hr {
  position: relative;
  margin: 40px auto;
  border: 2px dashed #d2ebfd;
  width: calc(100% - 4px);
}
.full {
  width: 100% !important;
}
.right-container {
  padding: 20px 24px;
  font-size: 14px;
}
.right-title {
  display: flex;
  align-items: center;
  line-height: 2;
  font-size: 16px;
  margin-bottom: 6px;
}
.right-title i {
  font-weight: bold;
}
.recommend-container {
  margin-top: 40px;
}
.recommend-title {
  font-size: 20px;
  line-height: 2;
  font-weight: bold;
  margin-bottom: 5px;
}
.recommend-cover {
  width: 100%;
  height: 100%;
  opacity: 0.4;
  transition: all 0.6s;
  object-fit: cover;
}
.recommend-info {
  line-height: 2;
  color: #fff;
  position: absolute;
  top: 50%;
  padding: 0 20px;
  width: 100%;
  transform: translate(0, -50%);
  text-align: center;
  font-size: 14px;
}
.recommend-date {
  font-size: 90%;
}
.recommend-item:hover .recommend-cover {
  opacity: 0.8;
  transform: scale(1.1);
}
.article-item {
  display: flex;
  align-items: center;
  padding: 6px 0;
}
.article-item:first-child {
  padding-top: 0;
}
.article-item:last-child {
  padding-bottom: 0;
}
.article-item:not(:last-child) {
  border-bottom: 1px dashed #f5f5f5;
}
.article-item img {
  width: 100%;
  height: 100%;
  transition: all 0.6s;
  object-fit: cover;
}
.article-item img:hover {
  transform: scale(1.1);
}
.content {
  flex: 1;
  padding-left: 10px;
  word-break: break-all;
  display: -webkit-box;
  overflow: hidden;
  -webkit-box-orient: vertical;
}
.content-cover {
  width: 58px;
  height: 58px;
  overflow: hidden;
}
.content-title a {
  transition: all 0.2s;
  font-size: 95%;
}
.content-title a:hover {
  color: #2ba1d1;
}
.content-time {
  color: #858585;
  font-size: 85%;
  line-height: 2;
}
</style>

<style lang="scss">
pre.hljs {
  padding: 12px 2px 12px 40px !important;
  border-radius: 5px !important;
  position: relative;
  font-size: 14px !important;
  line-height: 22px !important;
  overflow: hidden !important;
  &:hover .copy-btn {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  code {
    display: block !important;
    margin: 0 10px !important;
    overflow-x: auto !important;
    &::-webkit-scrollbar {
      z-index: 11;
      width: 6px;
    }
    &::-webkit-scrollbar:horizontal {
      height: 6px;
    }
    &::-webkit-scrollbar-thumb {
      border-radius: 5px;
      width: 6px;
      background: #666;
    }
    &::-webkit-scrollbar-corner,
    &::-webkit-scrollbar-track {
      background: #1e1e1e;
    }
    &::-webkit-scrollbar-track-piece {
      background: #1e1e1e;
      width: 6px;
    }
  }
  .line-numbers-rows {
    position: absolute;
    pointer-events: none;
    top: 12px;
    bottom: 12px;
    left: 0;
    font-size: 100%;
    width: 40px;
    text-align: center;
    letter-spacing: -1px;
    border-right: 1px solid rgba(0, 0, 0, 0.66);
    user-select: none;
    counter-reset: linenumber;
    span {
      pointer-events: none;
      display: block;
      counter-increment: linenumber;
      &:before {
        content: counter(linenumber);
        color: #999;
        display: block;
        text-align: center;
      }
    }
  }
  b.name {
    position: absolute;
    top: 7px;
    right: 45px;
    z-index: 1;
    color: #999;
    pointer-events: none;
  }
  .copy-btn {
    position: absolute;
    top: 6px;
    right: 6px;
    z-index: 1;
    color: #ccc;
    background-color: #525252;
    border-radius: 6px;
    display: none;
    font-size: 14px;
    width: 32px;
    height: 24px;
    outline: none;
  }
}
</style>
