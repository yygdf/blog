<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        :disabled="userOnlineIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        批量下线
      </el-button>
      <div style="margin-left:auto">
        <el-input
          v-model="keywords"
          ref="input"
          size="small"
          prefix-icon="el-icon-search"
          placeholder="请输入用户名或昵称"
          style="width:200px"
          @keyup.enter.native="listUserOnlines"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listUserOnlines"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table v-loading="loading" :data="userOnlineList" border>
      <el-table-column type="selection" width="40" align="center" />
      <el-table-column
        prop="username"
        label="用户"
        align="center"
        width="120"
      />
      <el-table-column prop="avatar" label="头像" align="center" width="80">
        <template slot-scope="scope">
          <el-image
            :src="scope.row.avatar"
            style="width: 40px;height: 40px;"
            :preview-src-list="[scope.row.avatar]"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="nickname"
        label="昵称"
        align="center"
        width="120"
      />
      <el-table-column
        prop="loginDevice"
        label="登陆设备"
        align="center"
        width="120"
      />
      <el-table-column
        prop="loginMethod"
        label="登录方式"
        align="center"
        width="120"
      />
      <el-table-column
        prop="ipAddress"
        label="ip地址"
        align="center"
        width="120"
      />
      <el-table-column
        prop="ipSource"
        label="ip来源"
        align="center"
        width="120"
      />
      <el-table-column
        prop="loginTime"
        label="登录时间"
        align="center"
        width="200"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.loginTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150">
        <template slot-scope="scope">
          <el-popconfirm
            title="确定下线吗？"
            style="margin-left:10px"
            @confirm="removeOnlineUser(scope.row)"
          >
            <el-button size="mini" type="danger" slot="reference">
              下线
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :total="count"
      :page-size="size"
      :current-page="current"
      :page-sizes="[10, 20]"
      class="pagination-container"
      layout="total, sizes, prev, pager, next, jumper"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
    />
    <el-dialog :visible.sync="removeStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否强制下线选中用户？</div>
      <div slot="footer">
        <el-button @click="removeStatus = false">取 消</el-button>
        <el-button type="primary" @click="removeOnlineUser(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.listUserOnlines();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data() {
    return {
      userOnlineList: [],
      userOnlineIdList: [],
      keywords: null,
      loading: true,
      removeStatus: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    sizeChange(size) {
      this.size = size;
      this.listUserOnlines();
    },
    currentChange(current) {
      this.current = current;
      this.listUserOnlines();
    },
    listUserOnlines() {
      this.axios
        .get("/api/back/user/onlines", {
          params: {
            size: this.size,
            current: this.current,
            keywords: this.keywords
          }
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.userOnlineList = data.data.pageList;
          this.loading = false;
        });
    },
    removeOnlineUser(userOnline) {
      this.axios
        .delete("/api/admin/user/online/" + userOnline.id)
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: "成功",
              message: data.message
            });
            this.listUserOnlines();
          } else {
            this.$notify.error({
              title: "失败",
              message: data.message
            });
          }
        });
    }
  }
};
</script>

<style scoped>
label {
  font-weight: bold !important;
}
</style>
