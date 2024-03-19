<template>
  <div>
    <div class="archive-banner banner" :style="cover">
      <h1 class="banner-title">归档</h1>
    </div>
    <v-card class="blog-container">
      <timeline>
        <timeline-title> 目前共计{{ count }}篇文章，继续加油 </timeline-title>
        <timeline-item v-for="item of archiveList" :key="item.id">
          <span class="time">{{ item.publicTime | date }}</span>
          <router-link
            :to="rootUri + '/article/' + item.id"
            style="color:#666;text-decoration: none"
          >
            {{ item.articleTitle }}
          </router-link>
        </timeline-item>
      </timeline>
      <v-pagination
        color="#00C4B6"
        v-model="current"
        :length="Math.ceil(count / 10)"
        total-visible="7"
      />
    </v-card>
  </div>
</template>

<script>
import { Timeline, TimelineItem, TimelineTitle } from "vue-cute-timeline";
export default {
  created() {
    this.getArchives();
  },
  components: {
    Timeline,
    TimelineItem,
    TimelineTitle
  },
  data: function() {
    return {
      current: 1,
      count: 0,
      archiveList: []
    };
  },
  methods: {
    getArchives() {
      this.axios
        .get("/api/articles/archive", {
          params: { current: this.current }
        })
        .then(({ data }) => {
          this.archiveList = data.data.pageList;
          this.count = data.data.count;
        });
    }
  },
  computed: {
    cover() {
      return (
        "background: url(" +
        this.$store.state.blogConfig.archive_banner_cover +
        ") center center / cover no-repeat"
      );
    },
    rootUri() {
      return this.$store.state.rootUri;
    }
  },
  watch: {
    current(value) {
      this.axios
        .get("/api/articles/archive", {
          params: { current: value }
        })
        .then(({ data }) => {
          this.archiveList = data.data.pageList;
          this.count = data.data.count;
        });
    }
  }
};
</script>

<style scoped>
.archive-banner {
  background: #49b1f5;
}
.time {
  font-size: 0.75rem;
  color: #555;
  margin-right: 1rem;
}
</style>
