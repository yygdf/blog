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
          v-model="flag"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择"
          clearable
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
          style="width: 200px"
          @keyup.enter.native="getUserOnlines(true)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getUserOnlines(true)"
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
      <el-table-column
        type="selection"
        width="40"
        align="center"
        :selectable="checkSelectable"
      />
      <el-table-column
        v-if="showColumnConfig.username"
        prop="username"
        label="用户"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.avatar"
        prop="avatar"
        label="头像"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <el-image
            :src="scope.row.avatar === '' ? defaultAvatar : scope.row.avatar"
            style="width: 40px;height: 40px;"
            :preview-src-list="[
              scope.row.avatar === '' ? defaultAvatar : scope.row.avatar
            ]"
          />
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.nickname"
        prop="nickname"
        label="昵称"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.loginDevice"
        prop="loginDevice"
        label="登录设备"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <el-tag style="margin-right:4px;margin-top:4px">
            {{ scope.row.loginDevice }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.loginMethod"
        prop="loginMethod"
        label="登录方式"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <el-tag style="margin-right:4px;margin-top:4px">
            {{ loginMethod(scope.row.loginMethod) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.loginPlatform"
        prop="loginPlatform"
        label="登录平台"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <el-tag style="margin-right:4px;margin-top:4px">
            {{ scope.row.loginPlatform ? "后台" : "前台" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.ipAddress"
        prop="ipAddress"
        label="ip地址"
        align="center"
        width="120"
      />
      <el-table-column
        v-if="showColumnConfig.ipSource"
        prop="ipSource"
        label="ip来源"
        align="center"
        width="120"
      />
      <el-table-column
        v-if="showColumnConfig.loginTime"
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
      <el-table-column label="操作" align="center" width="80">
        <template slot="header">
          <el-popover placement="bottom" title="选择显示列" width="160">
            <div>
              <el-checkbox v-model="showColumnConfig.username"
                >用户</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.avatar">头像</el-checkbox>
              <el-checkbox v-model="showColumnConfig.nickname"
                >昵称</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.loginDevice"
                >登录设备</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.loginMethod"
                >登录方式</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.loginPlatform"
                >登录平台</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.ipAddress"
                >ip地址</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.ipSource"
                >ip来源</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.loginTime"
                >登录时间</el-checkbox
              >
              <div>
                <el-button
                  type="primary"
                  size="mini"
                  style="float: right"
                  plain
                  @click="saveColumnConfig"
                >
                  保存
                </el-button>
              </div>
            </div>
            <i slot="reference" class="el-icon-setting table-setting-icon"></i>
          </el-popover>
        </template>
        <template slot-scope="scope">
          <el-popconfirm
            title="确定强制下线该用户吗？"
            @confirm="deleteUserOnlines(scope.row.id)"
          >
            <el-button
              :disabled="rootUserIdList.some(e => e === scope.row.id)"
              size="mini"
              type="danger"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-warning-outline" /> 下线
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :total="count"
      :page-size="size"
      :current-page.sync="current"
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
    this.loadColumnConfig();
    this.getUserOnlines();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data() {
    return {
      options: [
        {
          value: false,
          label: "前台"
        },
        {
          value: true,
          label: "后台"
        }
      ],
      rootUserIdList: [],
      userOnlineList: [],
      userOnlineIdList: [],
      showColumnConfig: {},
      flag: null,
      keywords: null,
      oldKeywords: null,
      loading: true,
      removeStatus: false,
      size: 10,
      count: 0,
      current: 1,
      defaultAvatar: require("../../assets/img/default/avatar.png")
    };
  },
  methods: {
    sizeChange(size) {
      this.size = size;
      this.getUserOnlines(true);
    },
    currentChange(current) {
      this.current = current;
      this.getUserOnlines();
    },
    checkSelectable(row) {
      return !this.rootUserIdList.some(e => e === row.id);
    },
    selectionChange(selection) {
      this.userOnlineIdList = [];
      selection.forEach(item => {
        this.userOnlineIdList.push(item.id);
      });
    },
    saveColumnConfig() {
      localStorage.setItem(
        "UserOnlineColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("UserOnlineColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("UserOnlineColumnSet")
        );
      } else {
        this.showColumnConfig = {
          username: true,
          avatar: true,
          nickname: true,
          loginDevice: true,
          loginMethod: true,
          loginPlatform: true,
          ipAddress: true,
          ipSource: true,
          loginTime: true
        };
      }
    },
    getUserOnlines(resetCurrentPage) {
      if (resetCurrentPage || this.keywords !== this.oldKeywords) {
        this.current = 1;
        this.oldKeywords = this.keywords;
      }
      let params = {
        size: this.size,
        flag: this.flag,
        current: this.current,
        keywords: this.keywords
      };
      params = this.$commonMethod.skipEmptyValue(params);
      this.axios
        .get("/api/back/user/onlines", {
          params
        })
        .then(({ data }) => {
          this.rootUserIdList = data.data.rootUserIdList;
          this.count = data.data.pagePojo.count;
          this.userOnlineList = data.data.pagePojo.pageList;
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
          if (param.data.length === this.userOnlineList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getUserOnlines();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.removeStatus = false;
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
    flag() {
      this.getUserOnlines(true);
    }
  }
};
</script>

<style scoped>
label {
  font-weight: bold !important;
}
</style>
