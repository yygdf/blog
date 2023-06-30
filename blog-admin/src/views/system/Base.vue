<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <!-- 表格操作 -->
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
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入内容"
          style="width:200px"
          @keyup.enter.native="listBaseInfo"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listBaseInfo"
        >
          搜索
        </el-button>
      </div>
    </div>
    <!-- 表格展示 -->
    <el-table
      border
      :data="baseInfoList"
      @selection-change="selectionChange"
      v-loading="loading"
    >
      <!-- 表格列 -->
      <el-table-column type="selection" width="55" />
      <!-- 属性名 -->
      <el-table-column prop="baseKey" label="属性名" align="center" />
      <!-- 属性值 -->
      <el-table-column prop="baseValue" label="属性值" align="center" />
      <!-- 详细描述 -->
      <el-table-column prop="description" label="详细描述" align="center" />
      <!-- 创建时间 -->
      <el-table-column prop="createTime" label="创建时间" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <!-- 更新时间 -->
      <el-table-column prop="updateTime" label="更新时间" align="center">
        <template slot-scope="scope" v-if="scope.row.updateTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.updateTime | date }}
        </template>
      </el-table-column>
      <!-- 列操作 -->
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openModel(scope.row)">
            编辑
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left:1rem"
            @confirm="deleteBaseInfo(scope.row.id)"
          >
            <el-button size="mini" type="danger" slot="reference" :disabled="scope.row.isDelete == 1">
              删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
      class="pagination-container"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
      :current-page="current"
      :page-size="size"
      :total="count"
      :page-sizes="[10, 20]"
      layout="total, sizes, prev, pager, next, jumper"
    />
    <!-- 添加编辑对话框 -->
    <el-dialog :visible.sync="addOrEdit" width="30%">
      <div class="dialog-title-container" slot="title" ref="baseInfoTitle" />
      <el-form label-width="80px" size="medium" :model="baseInfoForm">
        <el-form-item label="属性名">
          <el-input v-model="baseInfoForm.baseKey" style="width:220px" :disabled="baseInfoForm.id" />
        </el-form-item>
        <el-form-item label="属性值">
          <el-input v-model="baseInfoForm.baseValue" style="width:220px" />
        </el-form-item>
        <el-form-item label="详细描述">
          <el-input v-model="baseInfoForm.description" style="width:220px" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEdit = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditBaseInfo">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.listBaseInfo();
  },
  data: function() {
    return {
      isDelete: false,
      loading: true,
      addOrEdit: false,
      keywords: null,
      baseInfoIdList: [],
      baseInfoList: [],
      baseInfoForm: {
        id: null,
        baseKey: "",
        baseValue: "",
        description: ""
      },
      current: 1,
      size: 10,
      count: 0
    };
  },
  methods: {
    selectionChange(baseInfoList) {
      this.baseInfoIdList = [];
      baseInfoList.forEach(item => {
        this.baseInfoIdList.push(item.id);
      });
    },
    sizeChange(size) {
      this.size = size;
      this.listBaseInfo();
    },
    currentChange(current) {
      this.current = current;
      this.listBaseInfo();
    },
    deleteBaseInfo(id) {
      this.axios.delete("/api/admin/base/" + id).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listBaseInfo();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.isDelete = false;
      });
    },
    listBaseInfo() {
      this.axios
        .get("/api/admin/base", {
          params: {
            current: this.current,
            size: this.size,
            keywords: this.keywords
          }
        })
        .then(({ data }) => {
          this.baseInfoList = data.data.recordList;
          this.count = data.data.count;
          this.loading = false;
        });
    },
    openModel(baseInfo) {
      if (baseInfo != null) {
        this.baseInfoForm = JSON.parse(JSON.stringify(baseInfo));
        this.$refs.baseInfoTitle.innerHTML = "修改基础信息";
      } else {
        this.baseInfoForm.id = null;
        this.baseInfoForm.baseKey = "";
        this.baseInfoForm.baseValue = "";
        this.baseInfoForm.description = "";
        this.$refs.baseInfoTitle.innerHTML = "添加信息";
      }
      this.addOrEdit = true;
    },
    addOrEditBaseInfo() {
      if (this.baseInfoForm.baseKey.trim() == "") {
        this.$message.error("属性名不能为空");
        return false;
      }
      if (this.baseInfoForm.baseValue.trim() == "") {
        this.$message.error("属性值不能为空");
        return false;
      }
      this.axios
        .post("/api/admin/base", this.baseInfoForm)
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: "成功",
              message: data.message
            });
            this.listBaseInfo();
          } else {
            this.$notify.error({
              title: "失败",
              message: data.message
            });
          }
          this.addOrEdit = false;
        });
    }
  }
};
</script>
