<template>
  <v-footer app padless absolute v-if="!this.messageFlag">
    <div class="footer-wrap">
      <div>©{{ 2022 + " - " + new Date().getFullYear() + " by KS" }}</div>
      <a href="https://beian.miit.gov.cn/" target="_blank">
        {{ "赣ICP备2024028116号-1" }}
      </a>
    </div>
  </v-footer>
</template>

<script>
export default {
  computed: {
    messageFlag() {
      return this.$route.path === this.$store.state.rootUri + "/messages";
    }
  }
};
</script>

<style scoped>
.footer-wrap {
  width: 100%;
  line-height: 2;
  position: relative;
  padding: 22px 20px;
  color: #eee;
  font-size: 14px;
  text-align: center;
  background: linear-gradient(-45deg, #ee7752, #ce3e75, #23a6d5, #23d5ab);
  background-size: 400% 400%;
  animation: Gradient 10s ease infinite;
}
.footer-wrap a {
  color: #eee !important;
}
@keyframes Gradient {
  0% {
    background-position: 0 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0 50%;
  }
}
</style>
