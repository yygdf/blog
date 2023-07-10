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
      <el-button
        type="danger"
        size="small"
        icon="el-icon-minus"
        :disabled="this.categoryIdList.length === 0"
        @click="remove = true"
      >
        批量删除
      </el-button>
      <div style="margin-left:auto">
        <el-input
          v-model="keywords"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入分类名"
          style="width:200px"
          @keyup.enter.native="listCategories"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listCategories"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table
      border
      :data="categoryList"
      @selection-change="selectionChange"
      v-loading="loading"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="categoryName" label="分类名" align="center" />
      <el-table-column prop="createTime" label="创建时间" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="更新时间" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.updateTime | date }}
        </template>
      </el-table-column>
      <el-table-column
        prop="hiddenFlag"
        label="隐藏"
        width="100"
        align="center"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.hiddenFlag"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="true"
            :inactive-value="false"
            @change="changeHidden(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="publicFlag"
        label="公开"
        width="100"
        align="center"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.publicFlag"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="true"
            :inactive-value="false"
            @change="changePublic(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openModel(scope.row)">
            编辑
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left:1rem"
            @confirm="deleteCategory(scope.row.id)"
          >
            <el-button size="mini" type="danger" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
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
    <el-dialog :visible.sync="remove" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="remove = false">取 消</el-button>
        <el-button type="primary" @click="deleteCategory(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEdit" width="30%">
      <div class="dialog-title-container" slot="title" ref="categoryTitle" />
      <el-form label-width="80px" size="medium" :model="category">
        <el-form-item label="分类名">
          <el-input v-model="category.categoryName" style="width:220px" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEdit = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditCategory">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.listCategories();
  },
  data: function() {
    return {
      remove: false,
      loading: true,
      addOrEdit: false,
      keywords: null,
      categoryIdList: [],
      categoryList: [],
      category: {
        id: null,
        categoryName: "",
        publicFlag: true,
        hiddenFlag: false
      },
      current: 1,
      size: 10,
      count: 0
    };
  },
  methods: {
    selectionChange(categoryList) {
      this.categoryIdList = [];
      categoryList.forEach(item => {
        this.categoryIdList.push(item.id);
      });
    },
    sizeChange(size) {
      this.size = size;
      this.listCategories();
    },
    currentChange(current) {
      this.current = current;
      this.listCategories();
    },
    changePublic(category) {
      let param = new URLSearchParams();
      param.append("publicFlag", category.publicFlag);
      this.axios.put("/api/back/category/public/" + category.id, param);
    },
    changeHidden(category) {
      let param = new URLSearchParams();
      param.append("hiddenFlag", category.hiddenFlag);
      this.axios.put("/api/back/category/hidden/" + category.id, param);
    },
    deleteCategory(id) {
      var param = {};
      if (id == null) {
        param = { data: this.categoryIdList };
      } else {
        param = { data: [id] };
      }
      this.axios.delete("/api/back/categories", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listCategories();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.remove = false;
      });
    },
    listCategories() {
      this.axios
        .get("/api/back/categories", {
          params: {
            size: this.size,
            current: this.current,
            keywords: this.keywords
          }
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.categoryList = data.data.pageList;
          this.loading = false;
        });
    },
    openModel(category) {
      if (category != null) {
        this.category = JSON.parse(JSON.stringify(category));
        this.$refs.categoryTitle.innerHTML = "修改分类";
      } else {
        this.category.id = null;
        this.category.categoryName = "";
        this.category.publicFlag = true;
        this.category.hiddenFlag = false;
        this.$refs.categoryTitle.innerHTML = "添加分类";
      }
      this.addOrEdit = true;
    },
    addOrEditCategory() {
      if (this.category.categoryName.trim() === "") {
        this.$message.error("分类名不能为空");
        return false;
      }
      this.axios
        .post("/api/back/categories", this.category)
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: "成功",
              message: data.message
            });
            this.listCategories();
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
