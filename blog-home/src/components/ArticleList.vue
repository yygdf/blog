<template>
  <div>
    <div :class="categoryOrTag + ' banner'" :style="cover">
      <h1 class="banner-title animated fadeInDown">{{ title }} - {{ name }}</h1>
    </div>
    <div class="article-list-wrapper">
      <v-row>
        <v-col md="4" cols="12" v-for="item of articleList" :key="item.id">
          <v-card class="animated zoomIn article-item-card">
            <div class="article-item-cover">
              <router-link :to="'/article/' + item.id">
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
            <div class="article-item-info">
              <div>
                <router-link :to="'/article/' + item.id">
                  {{ item.articleTitle }}
                </router-link>
              </div>
              <div style="margin-top:0.375rem">
                <v-icon size="20">mdi-clock-outline</v-icon>
                {{ item.publicTime | date }}
                <router-link
                  :to="'/category/' + item.categoryId"
                  class="float-right"
                >
                  <v-icon>mdi-bookmark</v-icon>{{ item.categoryName }}
                </router-link>
              </div>
            </div>
            <v-divider></v-divider>
            <div class="tag-wrapper">
              <router-link
                :to="'/tag/' + tag.id"
                class="tag-btn"
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
                {{ tag.tagName }}
              </router-link>
            </div>
          </v-card>
        </v-col>
      </v-row>
      <infinite-loading @infinite="infiniteHandler">
        <div slot="no-more" />
      </infinite-loading>
    </div>
  </div>
</template>

<script>
export default {
  created() {
    if (this.$route.path.indexOf("/category") !== -1) {
      this.type = 1;
      this.title = "分类";
      this.categoryOrTag = "category-banner";
      this.cover =
        "background: url(" +
        this.$store.state.blogConfig.category_banner_cover +
        ") center center / cover no-repeat";
    } else {
      this.title = "标签";
      this.categoryOrTag = "tag-banner";
      this.cover =
        "background: url(" +
        this.$store.state.blogConfig.tag_banner_cover +
        ") center center / cover no-repeat";
    }
  },
  data: function() {
    return {
      current: 1,
      articleList: [],
      name: "",
      title: "",
      categoryOrTag: "",
      cover: "",
      type: null,
      defaultArticleCover: require("../assets/img/default/article.jpg")
    };
  },
  methods: {
    infiniteHandler($state) {
      let pathArr = this.$route.path.split("/");
      let params = {
        size: 9,
        current: this.current,
        categoryId: pathArr[pathArr.length - 1]
      };
      if (this.type) {
        params.type = 1;
      }
      this.axios
        .get("/api/articles/preview", {
          params
        })
        .then(({ data }) => {
          if (data.data.articlesPreviewDTOList.length) {
            this.current++;
            this.name = data.data.name;
            document.title = this.title + " - " + this.name;
            this.articleList.push(...data.data.articlesPreviewDTOList);
            $state.loaded();
          } else {
            $state.complete();
          }
        });
    }
  }
};
</script>

<style scoped>
.tag-banner {
  background: #49b1f5;
}
.category-banner {
  background: #49b1f5;
}
@media (min-width: 760px) {
  .article-list-wrapper {
    max-width: 1106px;
    margin: 370px auto 1rem auto;
  }
  .article-item-card:hover {
    transition: all 0.3s;
    box-shadow: 0 4px 12px 12px rgba(7, 17, 27, 0.15);
  }
  .article-item-card:not(:hover) {
    transition: all 0.3s;
  }
  .article-item-card:hover .on-hover {
    transition: all 0.6s;
    transform: scale(1.1);
  }
  .article-item-card:not(:hover) .on-hover {
    transition: all 0.6s;
  }
  .article-item-info {
    line-height: 1.7;
    padding: 15px 15px 12px 18px;
    font-size: 15px;
  }
}
@media (max-width: 759px) {
  .article-list-wrapper {
    margin-top: 230px;
    padding: 0 12px;
  }
  .article-item-info {
    line-height: 1.7;
    padding: 15px 15px 12px 18px;
  }
}
.article-item-card {
  border-radius: 8px !important;
  box-shadow: 0 4px 8px 6px rgba(7, 17, 27, 0.06);
}
.article-item-card a {
  transition: all 0.3s;
}
.article-item-cover {
  height: 220px;
  overflow: hidden;
}
.article-item-card a:hover {
  color: #8e8cd8;
}
.tag-wrapper {
  padding: 10px 15px 10px 18px;
}
.tag-wrapper a {
  color: #fff !important;
}
.tag-btn {
  display: inline-block;
  font-size: 0.725rem;
  line-height: 22px;
  height: 22px;
  border-radius: 10px;
  padding: 0 12px !important;
  background: linear-gradient(to right, #bf4643 0%, #6c9d8f 100%);
  opacity: 0.6;
  margin-right: 0.5rem;
}
</style>
