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
          style="width:200px"
          prefix-icon="el-icon-search"
          placeholder="请输入配置名或描述"
          clearable
          @keyup.enter.native="listSystemConfigs(true, true)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listSystemConfigs(true, true)"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table v-loading="loading" :data="systemConfigList" border>
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
          <el-button type="primary" size="mini" @click="openModel(scope.row)">
            编辑
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="deleteSystemConfig(scope.row.id)"
          >
            <el-button
              :disabled="!scope.row.deletableFlag"
              size="mini"
              type="danger"
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
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div
        class="dialog-title-container"
        slot="title"
        ref="systemConfigTitle"
      />
      <el-form :model="systemConfig" size="medium" label-width="80">
        <el-form-item label="配 置 名">
          <el-input
            :disabled="systemConfig.id !== undefined"
            v-model="systemConfig.configName"
            :ref="systemConfig.id ? '' : 'input'"
            style="width:250px"
            :maxLength="50"
          />
        </el-form-item>
        <el-form-item label="配 置 值">
          <el-input
            v-model="systemConfig.configValue"
            :ref="systemConfig.id ? 'input' : ''"
            style="width:250px"
            :maxLength="255"
          />
        </el-form-item>
        <el-form-item label="配置描述">
          <el-input
            v-model="systemConfig.configDesc"
            style="width:250px"
            :maxLength="255"
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
    this.listSystemConfigs();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      systemConfig: {},
      systemConfigList: [],
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
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.listSystemConfigs(true);
    },
    currentChange(current) {
      this.current = current;
      this.listSystemConfigs(this.keywords !== this.oldKeywords);
    },
    listSystemConfigs(resetPageFlag = false, searchFlag = false) {
      if (resetPageFlag) {
        this.current = 1;
      }
      if (searchFlag) {
        this.oldKeywords = this.keywords;
      }
      this.axios
        .get("/api/back/systemConfigs", {
          params: {
            size: this.size,
            current: this.current,
            keywords: this.keywords
          }
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
      this.axios
        .post("/api/back/systemConfig", this.systemConfig)
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: "成功",
              message: data.message
            });
            this.listSystemConfigs();
          } else {
            this.$notify.error({
              title: "失败",
              message: data.message
            });
          }
          this.addOrEditStatus = false;
        });
    },
    deleteSystemConfig(id) {
      let param = { data: id };
      if (this.messageList.length === 1) {
        this.current = --this.current > 1 ? this.current : 1;
      }
      this.axios.delete("/api/back/systemConfig", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listSystemConfigs();
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
