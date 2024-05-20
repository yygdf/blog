<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        v-if="checkWeight(200) && type !== 7"
        :disabled="userConfigIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatusBatch = true"
      >
        批量删除
      </el-button>
      <el-button
        v-if="type === 7"
        :disabled="userConfigIdList.length === 0"
        type="success"
        size="small"
        icon="el-icon-minus"
        @click="editStatusBatch = true"
      >
        批量恢复
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-if="checkWeight(200)"
          v-model="userId"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择用户"
          remote
          clearable
          filterable
          :remote-method="getUsernames"
        >
          <el-option
            v-for="item in usernameList"
            :key="item.id"
            :value="item.id"
            :label="item.label"
          />
        </el-select>
        <el-select
          v-if="checkWeight(100)"
          v-model="type"
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
          style="width: 200px"
          prefix-icon="el-icon-search"
          placeholder="请输入配置名或描述"
          clearable
          @keyup.enter.native="getUserConfigs(true)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getUserConfigs(true)"
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
        v-if="checkWeight(200)"
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
        width="200"
      />
      <el-table-column prop="configValue" label="配置值" align="center" />
      <el-table-column prop="configDesc" label="配置描述" align="center" width="160" />
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
          <el-button
            :disabled="!checkSelectable(scope.row)"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> 编辑
          </el-button>
          <el-popconfirm
            v-if="type === 7"
            title="确定恢复吗？"
            style="margin-left:10px"
            @confirm="updateUserConfigsStatus(scope.row.id)"
          >
            <el-button
              :disabled="!checkSelectable(scope.row)"
              type="success"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-refresh-left" /> 恢复
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="checkWeight(200) && type !== 7"
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="updateUserConfigsStatus(scope.row.id)"
          >
            <el-button
              :disabled="!checkSelectable(scope.row)"
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-delete" /> 删除
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
    <el-dialog :visible.sync="editStatusBatch" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否{{ type ? "恢复" : "删除" }}选中项？</div>
      <div slot="footer">
        <el-button @click="editStatusBatch = false">取 消</el-button>
        <el-button type="primary" @click="updateUserConfigsStatus(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="editStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="userConfigTitle" />
      <el-form :model="userConfig" size="medium" label-width="80">
        <el-form-item label="配 置 名">
          <el-input
            :disabled="true"
            v-model="userConfig.configName"
            class="word-limit-input"
            style="width: 360px"
            maxlength="50"
            placeholder="请输入配置名"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="配 置 值">
          <el-input
            v-model="userConfig.configValue"
            ref="input"
            class="word-limit-input2"
            style="width: 360px"
            maxlength="255"
            placeholder="请输入配置值"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="配置描述">
          <el-input
            v-model="userConfig.configDesc"
            class="word-limit-input2"
            style="width: 360px"
            maxlength="255"
            placeholder="请输入配置描述"
            show-word-limit
          />
        </el-form-item>
        <el-form-item v-if="assimilateStatus" label="开启同步">
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
        <el-button @click="editStatus = false">取 消</el-button>
        <el-button type="primary" @click="editUserConfig">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.getUserConfigs();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      options: [
        {
          value: null,
          label: "未删除"
        },
        {
          value: 7,
          label: "已删除"
        }
      ],
      usernameList: [],
      rootUserIdList: [],
      userConfigList: [],
      userConfigIdList: [],
      userConfig: {},
      userConfigOrigin: {},
      type: null,
      userId: null,
      keywords: null,
      rootUserId: null,
      oldKeywords: null,
      loading: true,
      editStatus: false,
      editStatusBatch: false,
      assimilateStatus: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    openModel(userConfig) {
      this.userConfig = {
        id: userConfig.id,
        configName: userConfig.configName,
        configValue: userConfig.configValue,
        configDesc: userConfig.configDesc
      };
      this.$refs.userConfigTitle.innerHTML = "修改配置";
      this.assimilateStatus =
        userConfig.userId === this.rootUserId &&
        this.$store.state.userId === this.rootUserId;
      this.userConfigOrigin = JSON.parse(JSON.stringify(this.userConfig));
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.editStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.getUserConfigs(true);
    },
    checkWeight(weight) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.getUserConfigs();
    },
    checkSelectable(row) {
      return (
        this.checkWeight(100) ||
        !this.rootUserIdList.some(e => e === row.userId)
      );
    },
    selectionChange(selection) {
      this.userConfigIdList = [];
      selection.forEach(item => {
        this.userConfigIdList.push(item.id);
      });
    },
    getUserConfigs(resetCurrentPage) {
      if (resetCurrentPage || this.keywords !== this.oldKeywords) {
        this.current = 1;
        this.oldKeywords = this.keywords;
      }
      let params = {
        size: this.size,
        type: this.type,
        userId: this.userId,
        current: this.current,
        keywords: this.keywords
      };
      params = this.$commonMethod.skipEmptyValue(params);
      this.axios
        .get("/api/back/userConfigs", {
          params
        })
        .then(({ data }) => {
          this.rootUserId = data.data.rootUserId;
          this.rootUserIdList = data.data.rootUserIdList;
          this.count = data.data.pagePojo.count;
          this.userConfigList = data.data.pagePojo.pageList;
          this.loading = false;
        });
    },
    getUsernames(keywords) {
      if (keywords.trim() === "") {
        return;
      }
      this.axios
        .get("/api/back/userAuth/usernames", { params: { keywords } })
        .then(({ data }) => {
          this.usernameList = data.data;
        });
    },
    editUserConfig() {
      let param = this.$commonMethod.skipIdenticalValue(
        this.userConfig,
        this.userConfigOrigin
      );
      if (Object.keys(param).length === 0) {
        return false;
      }
      param.id = this.userConfig.id;
      this.axios.put("/api/back/userConfig", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.getUserConfigs();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.editStatus = false;
    },
    updateUserConfigsStatus(id) {
      let param = {};
      if (id != null) {
        param.idList = [id];
      } else {
        param.idList = this.userConfigIdList;
      }
      if (this.type != null) {
        param.type = this.type;
      }
      this.axios.put("/api/back/userConfigs/status", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          if (param.idList.length === this.userConfigList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getUserConfigs();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.editStatusBatch = false;
    }
  },
  watch: {
    type() {
      this.getUserConfigs(true);
    },
    userId() {
      this.getUserConfigs(true);
    }
  }
};
</script>
