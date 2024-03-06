<template>
  <div>
    欢迎回来!
  </div>
</template>

<script>
export default {
  created() {
    this.getData();
  },
  data: function() {
    return {};
  },
  methods: {
    getData() {
      this.axios.get("/api/back/blog/home").then();
    }
  },
  computed: {
    userId() {
      return this.$store.state.userId;
    }
  }
};
</script>

<style scoped></style>
