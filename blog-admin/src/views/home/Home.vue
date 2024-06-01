<template>
  <el-card class="main-card">
    <el-tabs tab-position="left" style="height:calc(100vh - 180px)">
      <el-tab-pane>
        <span slot="label"><i class="el-icon-s-comment"></i> 回复我的</span>
        我的行程
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label"><i class="el-icon-s-comment"></i> &nbsp;@ 我的</span>
        我的行程
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label"><i class="el-icon-star-on"></i> 收到的赞</span>
        我的行程
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label"><i class="el-icon-message-solid"></i> 系统通知</span>
        我的行程
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label"><i class="el-icon-message-solid"></i> 我的消息</span>
        我的行程
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label"><i class="el-icon-setting"></i> 消息设置</span>
        我的行程
      </el-tab-pane>
    </el-tabs>
  </el-card>
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
