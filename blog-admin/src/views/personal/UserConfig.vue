<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        v-if="checkCurrentUserId"
        type="primary"
        size="small"
        icon="el-icon-plus"
        @click="openModel(null)"
      >
        新增
      </el-button>
      <el-button
        v-if="checkWeight && !deletedFlag"
        :disabled="userConfigIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        批量删除
      </el-button>
      <el-button
        v-if="deletedFlag"
        :disabled="userConfigIdList.length === 0"
        type="success"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        批量恢复
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-if="checkWeight"
          v-model="userId"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择用户"
          remote
          clearable
          filterable
          :remote-method="listAllUsername"
        >
          <el-option
            v-for="item in usernameList"
            :key="item.userId"
            :value="item.userId"
            :label="item.label"
          />
        </el-select>
        <el-select
          v-if="checkWeight(100)"
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
          style="width:200px"
          prefix-icon="el-icon-search"
          placeholder="请输入配置名或描述"
          clearable
          @keyup.enter.native="listUserConfigs(true, true)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listUserConfigs(true, true)"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="userConfigList"
      border
      @selection-change="selectionChange"
    >
      <el-table-column
        v-if="checkWeight"
        type="selection"
        align="center"
        width="40"
        :selectable="checkSelectable"
      />
      <el-table-column
        prop="username"
        label="用户"
        align="center"
        width="120"
      />
      <el-table-column
        prop="configName"
        label="配置名"
        align="center"
        width="120"
      />
      <el-table-column prop="configValue" label="配置值" align="center" />
      <el-table-column prop="configDesc" label="配置描述" align="center" />
      <el-table-column
        prop="createTime"
        label="创建日期"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="更新日期"
        align="center"
        width="120"
      >
        <template slot-scope="scope" v-if="scope.row.updateTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.updateTime | date }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template slot-scope="scope">
          <el-popconfirm
            v-if="deletedFlag"
            :disabled="!checkSelectable(scope.row)"
            title="确定恢复吗？"
            style="margin-left:10px"
            @confirm="updateUserConfigsStatus(scope.row.id)"
          >
            <el-button type="success" size="mini" slot="reference">
              恢复
            </el-button>
          </el-popconfirm>
          <el-button
            v-else
            :disabled="!checkSelectable(scope.row)"
            type="primary"
            size="mini"
            @click="openModel(scope.row)"
          >
            编辑
          </el-button>
          <el-popconfirm
            v-if="deletedFlag"
            :disabled="!checkCurrentUserId"
            title="确定彻底删除吗？"
            style="margin-left:10px"
            @confirm="deleteUserConfig(scope.row.configName)"
          >
            <el-button type="danger" size="mini" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="checkWeight && !deletedFlag"
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="updateUserConfigsStatus(scope.row.id)"
          >
            <el-button
              :disabled="!checkSelectable(scope.row)"
              type="danger"
              size="mini"
              slot="reference"
            >
              删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :total="count"
      :page-size="size"
      :page-sizes="[10, 20]"
      :current-page.sync="current"
      class="pagination-container"
      layout="total, sizes, prev, pager, next, jumper"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
    />
    <el-dialog :visible.sync="editStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">
        是否{{ deletedFlag ? "恢复" : "删除" }}选中项？
      </div>
      <div slot="footer">
        <el-button @click="editStatus = false">取 消</el-button>
        <el-button type="primary" @click="updateUserConfigsStatus(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="userConfigTitle" />
      <el-form :model="userConfig" size="medium" label-width="80">
        <el-form-item label="配 置 名">
          <el-input
            :disabled="userConfig.id != null"
            v-model="userConfig.configName"
            :ref="userConfig.id ? '' : 'input'"
            style="width:250px"
            :maxLength="50"
          />
          <span style="color: red;"> *</span>
        </el-form-item>
        <el-form-item label="配 置 值">
          <el-input
            v-model="userConfig.configValue"
            :ref="userConfig.id ? 'input' : ''"
            style="width:250px"
            :maxLength="255"
          />
          <span style="color: red;"> *</span>
        </el-form-item>
        <el-form-item label="配置描述">
          <el-input
            v-model="userConfig.configDesc"
            style="width:250px"
            :maxLength="255"
          />
        </el-form-item>
        <el-form-item
          v-if="checkCurrentUserId && userConfigUserId === rootUserId"
          label="开启同步"
        >
          <el-switch
            v-model="userConfig.assimilateFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEditStatus = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditUserConfig">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.listUserConfigs();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      options: [
        {
          value: false,
          label: "未删除"
        },
        {
          value: true,
          label: "已删除"
        }
      ],
      userConfig: {},
      usernameList: [],
      rootUserIdList: [],
      userConfigList: [],
      userConfigIdList: [],
      userId: null,
      keywords: null,
      rootUserId: null,
      oldKeywords: null,
      userConfigUserId: null,
      loading: true,
      editStatus: false,
      deletedFlag: false,
      addOrEditStatus: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    openModel(userConfig) {
      if (userConfig != null) {
        this.userConfig = {
          id: userConfig.id,
          configName: userConfig.configName,
          configValue: userConfig.configValue,
          configDesc: userConfig.configDesc
        };
        this.userConfigUserId = userConfig.userId;
        this.$refs.userConfigTitle.innerHTML = "修改配置";
      } else {
        this.userConfig = {
          id: null,
          configName: "",
          configValue: "",
          configDesc: ""
        };
        this.$refs.userConfigTitle.innerHTML = "添加配置";
      }
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.listUserConfigs(true);
    },
    checkWeight(weight = 200) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.listUserConfigs(this.keywords !== this.oldKeywords);
    },
    checkSelectable(row) {
      return (
        this.checkWeight(100) ||
        !this.rootUserIdList.some(e => e === row.userId)
      );
    },
    selectionChange(userConfigList) {
      this.userConfigIdList = [];
      userConfigList.forEach(item => {
        this.userConfigIdList.push(item.id);
      });
    },
    listUserConfigs(resetPageFlag = false, searchFlag = false) {
      if (resetPageFlag) {
        this.current = 1;
      }
      if (searchFlag) {
        this.oldKeywords = this.keywords;
      }
      this.axios
        .get("/api/back/userConfigs", {
          params: {
            size: this.size,
            userId: this.userId,
            current: this.current,
            keywords: this.keywords,
            deletedFlag: this.deletedFlag
          }
        })
        .then(({ data }) => {
          this.rootUserId = data.data.rootUserId;
          this.rootUserIdList = data.data.rootUserIdList;
          this.count = data.data.pagePojo.count;
          this.userConfigList = data.data.pagePojo.pageList;
          this.loading = false;
        });
    },
    listAllUsername(keywords) {
      if (keywords.trim() === "") {
        return;
      }
      this.axios
        .get("/api/back/userAuth/usernames", { params: { keywords } })
        .then(({ data }) => {
          this.usernameList = data.data;
        });
    },
    deleteUserConfig(configName) {
      let param = { data: configName };
      this.current = 1;
      this.axios.delete("/api/back/userConfig", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listMenus();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
    },
    addOrEditUserConfig() {
      if (this.userConfig.configName.trim() === "") {
        this.$message.error("配置名不能为空");
        return false;
      }
      if (this.userConfig.configValue.trim() === "") {
        this.$message.error("配置值不能为空");
        return false;
      }
      this.axios
        .post("/api/back/userConfig", this.userConfig)
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: "成功",
              message: data.message
            });
            this.listUserConfigs();
          } else {
            this.$notify.error({
              title: "失败",
              message: data.message
            });
          }
          this.addOrEditStatus = false;
        });
    },
    updateUserConfigsStatus(id) {
      let param = new URLSearchParams();
      if (id != null) {
        param.append("idList", [id]);
      } else {
        param.append("idList", this.userConfigIdList);
      }
      param.append("deletedFlag", !this.deletedFlag);
      if (param.get("idList").length === this.userConfigList.length) {
        this.current = --this.current > 1 ? this.current : 1;
      }
      this.axios.put("/api/back/userConfigs", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listUserConfigs();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.editStatus = false;
      });
    }
  },
  computed: {
    checkCurrentUserId() {
      return this.$store.state.userId === this.rootUserId;
    }
  },
  watch: {
    userId() {
      this.listUserConfigs(true);
    },
    deletedFlag() {
      this.listUserConfigs(true);
    }
  }
};
</script>
