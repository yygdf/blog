<template>
  <div>
    <div class="category-banner banner" :style="cover">
      <h1 class="banner-title">{{ $t("navBar.category") }}</h1>
    </div>
    <v-card class="blog-container">
      <div class="category-title">
        {{ $t("navBar.category") }} - {{ count }}
      </div>
      <ul class="category-list">
        <li
          class="category-list-item"
          v-for="item of categoryList"
          :key="item.id"
        >
          <router-link :to="rootUri + '/category/' + item.id">
            {{ item.categoryName }}
            <span class="category-count">({{ item.articleCount }})</span>
          </router-link>
        </li>
      </ul>
    </v-card>
  </div>
</template>

<script>
export default {
  created() {
    document.title = this.$t("navBar.category");
    this.getCategories();
  },
  data: function() {
    return {
      categoryList: [],
      count: 0
    };
  },
  methods: {
    getCategories() {
      this.axios.get("/api/categories").then(({ data }) => {
        this.categoryList = data.data.pageList;
        this.count = data.data.count;
      });
    }
  },
  computed: {
    cover() {
      return (
        "background: url(" +
        this.$store.state.blogConfig.category_banner_cover +
        ") center center / cover no-repeat"
      );
    },
    rootUri() {
      return this.$store.state.rootUri;
    }
  },
  watch: {
    "$i18n.locale"() {
      document.title = this.$t("navBar.category");
    }
  }
};
</script>

<style scoped>
.category-banner {
  background: #49b1f5;
}
.category-title {
  text-align: center;
  font-size: 36px;
  line-height: 2;
}
@media (max-width: 759px) {
  .category-title {
    font-size: 28px;
  }
}
.category-list {
  margin: 0 1.8rem;
  list-style: none;
}
.category-list-item {
  padding: 8px 1.8rem 8px 0;
}
.category-list-item:before {
  display: inline-block;
  position: relative;
  left: -0.75rem;
  width: 12px;
  height: 12px;
  border: 0.2rem solid #49b1f5;
  border-radius: 50%;
  background: #fff;
  content: "";
  transition-duration: 0.3s;
}
.category-list-item:hover:before {
  border: 0.2rem solid #ff7242;
}
.category-list-item a:hover {
  transition: all 0.3s;
  color: #8e8cd8;
}
.category-list-item a:not(:hover) {
  transition: all 0.3s;
}
.category-count {
  margin-left: 0.5rem;
  font-size: 0.75rem;
  color: #858585;
}
</style>
