<template>
  <v-dialog
    v-model="searchFlag"
    max-width="600"
    :fullscreen="this.$store.state.mobileFlag"
  >
    <v-card class="search-wrapper" style="border-radius:4px">
      <div class="mb-3">
        <span class="search-title"
          >{{ status ? $t("search.allSite") : $t("search.theSite") }}
          <v-switch
            v-model="status"
            style="display: inline-block;margin: -2rem 0;padding-left: 1rem"
          ></v-switch
        ></span>
        <v-icon class="float-right" @click="searchFlag = false">
          mdi-close
        </v-icon>
      </div>
      <div class="search-input-wrapper">
        <v-icon>mdi-magnify</v-icon>
        <input
          v-model="keywords"
          ref="inputRef"
          :placeholder="$t('search.input')"
          @keyup="keywordsInputChange($event)"
          @keyup.enter="getArticles"
        />
      </div>
      <div class="search-result-wrapper">
        <hr class="divider" />
        <ul>
          <li class="search-result" v-for="item of articleList" :key="item.id">
            <a @click="goTo(item.id, item.userId)" v-html="item.articleTitle" />
            <a
              @click="goTo(null, item.userId)"
              style="float: right"
              v-html="item.username"
            />
            <p
              class="search-result-content text-justify"
              v-html="item.articleContent"
            />
          </li>
        </ul>
        <div
          v-show="flag && articleList.length === 0"
          style="font-size:0.875rem"
        >
          {{ $t("search.tip1") + ": " + keywords }}
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data: function() {
    return {
      keywords: "",
      articleList: [],
      flag: false,
      status: false,
      lastTimeStamp: 0
    };
  },
  updated() {
    this.$nextTick(() => {
      if (this.$refs.inputRef) {
        this.$refs.inputRef.focus();
      }
    });
  },
  computed: {
    searchFlag: {
      set(value) {
        this.$store.commit("updateSearchFlag", value);
      },
      get() {
        return this.$store.state.searchFlag;
      }
    }
  },
  methods: {
    goTo(id, userId) {
      this.$store.commit("updateSearchFlag", false);
      if (id == null) {
        if (userId !== this.$store.state.bloggerId) {
          window.open(this.$store.state.rootUrl + "/" + userId, "_blank");
        }
      } else {
        if (userId !== this.$store.state.bloggerId) {
          window.open(
            this.$store.state.rootUrl + "/" + userId + "/article/" + id,
            "_blank"
          );
        } else {
          this.$router.push({
            path: this.$store.state.rootUri + "/article/" + id
          });
        }
      }
    },
    keywordsInputChange(event) {
      if (event.key !== "Enter") {
        this.lastTimeStamp = event.timeStamp;
        setTimeout(() => {
          if (this.lastTimeStamp === event.timeStamp) {
            this.getArticles();
          }
        }, 1000);
      } else {
        this.lastTimeStamp = 0;
      }
    },
    getArticles() {
      const keywords = this.keywords.trim();
      if (keywords === "") {
        this.flag = false;
        return;
      }
      this.flag = true;
      let params = {
        keywords: keywords
      };
      if (this.status) {
        params.status = true;
      }
      this.axios.get("/api/articles/search", { params }).then(({ data }) => {
        if (data.data) {
          this.articleList = data.data;
        }
      });
    }
  },
  watch: {
    status() {
      this.getArticles();
    }
  }
};
</script>

<style scoped>
.search-wrapper {
  padding: 1.25rem;
  height: 100%;
  background: #fff !important;
}
.theme--dark .search-wrapper {
  background-color: #1b1f23 !important;
}
.theme--dark input {
  color: #fff;
}
.search-title {
  color: #49b1f5;
  font-size: 1.25rem;
  line-height: 1;
}
.search-input-wrapper {
  display: flex;
  padding: 5px;
  height: 35px;
  width: 100%;
  border: 2px solid #8e8cd8;
  border-radius: 2rem;
}
.search-input-wrapper input {
  width: 100%;
  margin-left: 5px;
  outline: none;
}
@media (min-width: 960px) {
  .search-result-wrapper {
    padding-right: 5px;
    height: 450px;
    overflow: auto;
  }
}
@media (max-width: 959px) {
  .search-result-wrapper {
    height: calc(100vh - 110px);
    overflow: auto;
  }
}
.search-result a {
  color: #555;
  font-weight: bold;
  border-bottom: 1px solid #999;
  text-decoration: none;
}
.search-result-content {
  color: #555;
  cursor: pointer;
  border-bottom: 1px dashed #ccc;
  padding: 5px 0;
  line-height: 2;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
.divider {
  margin: 20px 0;
  border: 2px dashed #d2ebfd;
}
</style>
