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
        <el-select
          v-model="deletedFlag"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择"
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
        <el-input
          v-model="keywords"
          ref="input"
          size="small"
          prefix-icon="el-icon-search"
          placeholder="请输入用户名"
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
    <el-table
      v-loading="loading"
      :data="userOnlineList"
      border
      @selection-change="selectionChange"
    >
      <el-table-column type="selection" width="40" align="center" />
      <el-table-column
        prop="username"
        label="用户"
        align="center"
        width="160"
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
      <el-table-column prop="nickname" label="昵称" align="center" />
      <el-table-column
        prop="loginDevice"
        label="登陆设备"
        align="center"
        width="160"
      >
        <template slot-scope="scope">
          <el-tag style="margin-right:4px;margin-top:4px">
            {{ scope.row.loginDevice }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="loginMethod"
        label="登录方式"
        align="center"
        width="160"
      >
        <template slot-scope="scope">
          <el-tag style="margin-right:4px;margin-top:4px">
            {{ loginMethod(scope.row.loginMethod) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="loginPlatform"
        label="登录平台"
        align="center"
        width="160"
      >
        <template slot-scope="scope">
          <el-tag style="margin-right:4px;margin-top:4px">
            {{ scope.row.loginPlatform ? "后台" : "前台" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="ipAddress"
        label="ip地址"
        align="center"
        width="160"
      />
      <el-table-column
        prop="ipSource"
        label="ip来源"
        align="center"
        width="160"
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
      <el-table-column label="操作" align="center" width="120">
        <template slot-scope="scope">
          <el-popconfirm
            title="确定强制下线该用户吗？"
            @confirm="deleteUserOnlines(scope.row.id)"
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
        <el-button type="primary" @click="deleteUserOnlines(null)">
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
      options: [
        {
          value: null,
          label: ""
        },
        {
          value: false,
          label: "前台"
        },
        {
          value: true,
          label: "后台"
        }
      ],
      userOnlineList: [],
      userOnlineIdList: [],
      keywords: null,
      deletedFlag: null,
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
    selectionChange(userOnlineList) {
      this.userOnlineIdList = [];
      userOnlineList.forEach(item => {
        this.userOnlineIdList.push(item.id);
      });
    },
    listUserOnlines() {
      this.axios
        .get("/api/back/user/onlines", {
          params: {
            size: this.size,
            current: this.current,
            keywords: this.keywords,
            deletedFlag: this.deletedFlag
          }
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.userOnlineList = data.data.pageList;
          this.loading = false;
        });
    },
    deleteUserOnlines(id) {
      let param = {};
      if (id == null) {
        param = { data: this.userOnlineIdList };
      } else {
        param = { data: [id] };
      }
      this.axios.delete("/api/back/user/onlines", param).then(({ data }) => {
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
  },
  computed: {
    loginMethod() {
      return function(method) {
        switch (method) {
          case 1:
            return "邮箱";
          case 2:
            return "QQ";
          case 3:
            return "微信";
          case 4:
            return "手机号";
        }
      };
    }
  },
  watch: {
    deletedFlag() {
      this.listUserOnlines();
    }
  }
};
</script>

<style scoped>
label {
  font-weight: bold !important;
}
</style>
