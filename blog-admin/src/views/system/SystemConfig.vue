<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        type="primary"
        size="small"
        icon="el-icon-plus"
        @click="openModel(null)"
      >
        新增
      </el-button>
      <div style="margin-left:auto">
        <el-input
          v-model="keywords"
          ref="input"
          size="small"
          style="width: 200px"
          prefix-icon="el-icon-search"
          placeholder="请输入配置名或描述"
          clearable
          @keyup.enter.native="getSystemConfigs(true)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getSystemConfigs(true)"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table v-loading="loading" :data="systemConfigList" border>
      <el-table-column
        v-if="showColumnConfig.configName"
        prop="configName"
        label="配置名"
        align="center"
        width="200"
      />
      <el-table-column
        v-if="showColumnConfig.configValue"
        prop="configValue"
        label="配置值"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.configDesc"
        prop="configDesc"
        label="配置描述"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.createTime"
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
        v-if="showColumnConfig.updateTime"
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
        <template slot="header">
          <el-popover placement="bottom" title="选择显示列" width="160">
            <div>
              <el-checkbox v-model="showColumnConfig.configName"
                >配置名</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.configValue"
                >配置值</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.configDesc"
                >配置描述</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.createTime"
                >创建日期</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.updateTime"
                >更新日期</el-checkbox
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
          <el-button
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> 编辑
          </el-button>
          <el-popconfirm
            title="确定彻底删除吗？"
            style="margin-left:10px"
            @confirm="deleteSystemConfigs(scope.row.id)"
          >
            <el-button
              :disabled="!scope.row.deletableFlag"
              size="mini"
              type="danger"
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
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div
        class="dialog-title-container"
        slot="title"
        ref="systemConfigTitle"
      />
      <el-form :model="systemConfig" size="medium" label-width="80">
        <el-form-item label="配 置 名">
          <el-input
            :disabled="systemConfig.id != null"
            v-model="systemConfig.configName"
            :ref="systemConfig.id ? '' : 'input'"
            class="word-limit-input form-input-width"
            maxlength="50"
            placeholder="请输入配置名"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="配 置 值">
          <el-input
            v-model="systemConfig.configValue"
            :ref="systemConfig.id ? 'input' : ''"
            class="word-limit-input2 form-input-width"
            maxlength="255"
            placeholder="请输入配置值"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="配置描述">
          <el-input
            v-model="systemConfig.configDesc"
            class="word-limit-input2 form-input-width"
            maxlength="255"
            placeholder="请输入配置描述"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEditStatus = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditSystemConfig">
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
    this.getSystemConfigs();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      systemConfigList: [],
      systemConfig: {},
      showColumnConfig: {},
      systemConfigOrigin: {},
      keywords: null,
      oldKeywords: null,
      loading: true,
      addOrEditStatus: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    openModel(systemConfig) {
      if (systemConfig != null) {
        this.systemConfig = {
          id: systemConfig.id,
          configName: systemConfig.configName,
          configValue: systemConfig.configValue,
          configDesc: systemConfig.configDesc
        };
        this.$refs.systemConfigTitle.innerHTML = "修改配置";
      } else {
        this.systemConfig = {
          configName: "",
          configValue: "",
          configDesc: ""
        };
        this.$refs.systemConfigTitle.innerHTML = "添加配置";
      }
      this.systemConfigOrigin = JSON.parse(JSON.stringify(this.systemConfig));
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.getSystemConfigs(true);
    },
    currentChange(current) {
      this.current = current;
      this.getSystemConfigs();
    },
    saveColumnConfig() {
      localStorage.setItem(
        "SystemConfigColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("SystemConfigColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("SystemConfigColumnSet")
        );
      } else {
        this.showColumnConfig = {
          configName: true,
          configValue: true,
          configDesc: true,
          createTime: true,
          updateTime: true
        };
      }
    },
    getSystemConfigs(resetCurrentPage) {
      if (resetCurrentPage || this.keywords !== this.oldKeywords) {
        this.current = 1;
        this.oldKeywords = this.keywords;
      }
      let params = {
        size: this.size,
        current: this.current,
        keywords: this.keywords
      };
      params = this.$commonMethod.skipEmptyValue(params);
      this.axios
        .get("/api/back/systemConfigs", {
          params
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.systemConfigList = data.data.pageList;
          this.loading = false;
        });
    },
    addOrEditSystemConfig() {
      if (this.systemConfig.configName.trim() === "") {
        this.$message.error("配置名不能为空");
        return false;
      }
      if (this.systemConfig.configValue.trim() === "") {
        this.$message.error("配置值不能为空");
        return false;
      }
      let param = this.$commonMethod.skipIdenticalValue(
        this.systemConfig,
        this.systemConfigOrigin
      );
      if (Object.keys(param).length === 0) {
        return false;
      }
      if (this.systemConfig.id != null) {
        param.id = this.systemConfig.id;
      }
      this.axios.post("/api/back/systemConfig", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.getSystemConfigs();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.addOrEditStatus = false;
    },
    deleteSystemConfigs(id) {
      let param = { data: [id] };
      this.axios.delete("/api/back/systemConfigs", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          if (this.systemConfigList.length === 1) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getSystemConfigs();
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
