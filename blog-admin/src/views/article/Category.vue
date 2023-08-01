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
        :disabled="this.categoryIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        批量删除
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-if="checkWeight(300)"
          v-model="userId"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择用户"
          clearable
          filterable
        >
          <el-option
            v-for="item in usernameList"
            :key="item.userId"
            :value="item.userId"
            :label="item.username"
          />
        </el-select>
        <el-input
          v-model="keywords"
          size="small"
          style="width:200px"
          prefix-icon="el-icon-search"
          placeholder="请输入分类名"
          clearable
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
      v-loading="loading"
      :data="categoryList"
      border
      @selection-change="selectionChange"
    >
      <el-table-column
        type="selection"
        align="center"
        width="40"
        :selectable="checkSelectable"
      />
      <el-table-column
        v-if="checkWeight(300)"
        prop="username"
        label="用户"
        align="center"
        width="120"
      />
      <el-table-column prop="categoryName" label="分类名" align="center" />
      <el-table-column prop="articleCount" label="文章数" align="center" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.articleCount">
            {{ scope.row.articleCount }}
          </span>
          <span v-else>0</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center" width="120">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="更新时间" align="center" width="120">
        <template slot-scope="scope" v-if="scope.row.updateTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.updateTime | date }}
        </template>
      </el-table-column>
      <el-table-column
        prop="hiddenFlag"
        label="隐藏"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.hiddenFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="changeCategoryStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="publicFlag"
        label="公开"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.publicFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="changeCategoryStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openModel(scope.row)">
            编辑
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left:1rem"
            @confirm="deleteCategory(scope.row.id)"
          >
            <el-button
              :disabled="scope.row.articleCount !== 0"
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
      :current-page="current"
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
      <div style="font-size:1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="editStatus = false">取 消</el-button>
        <el-button type="primary" @click="deleteCategory(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="categoryTitle" />
      <el-form :model="category" size="medium" label-width="80">
        <el-form-item label="分类名">
          <el-input v-model="category.categoryName" style="width:200px" :maxLength="50" />
        </el-form-item>
      </el-form>
      <el-form
        :model="category"
        :inline="true"
        size="medium"
        label-width="80"
      >
        <el-form-item label="隐藏">
          <el-switch
            v-model="category.hiddenFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item label="公开">
          <el-switch
            v-model="category.publicFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEditStatus = false">取 消</el-button>
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
    this.userId = this.$store.state.userId;
    this.listCategories();
    this.listAllUsername();
  },
  data: function() {
    return {
      category: {
        id: null,
        categoryName: "",
        publicFlag: true,
        hiddenFlag: false
      },
      usernameList: [],
      categoryList: [],
      categoryIdList: [],
      userId: null,
      keywords: null,
      loading: true,
      editStatus: false,
      addOrEditStatus: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
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
      this.addOrEditStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.listCategories();
    },
    checkWeight(weight = 200) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.listCategories();
    },
    checkSelectable(row) {
      return !row.articleCount;
    },
    selectionChange(categoryList) {
      this.categoryIdList = [];
      categoryList.forEach(item => {
        this.categoryIdList.push(item.id);
      });
    },
    listCategories() {
      this.axios
        .get("/api/back/categories", {
          params: {
            size: this.size,
            userId: this.userId,
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
    listAllUsername() {
      if (this.checkWeight(300)) {
        this.axios.get("/api/back/user/username").then(({ data }) => {
          this.usernameList = data.data;
        });
      }
    },
    deleteCategory(id) {
      let param = {};
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
        this.editStatus = false;
      });
    },
    addOrEditCategory() {
      if (this.category.categoryName.trim() === "") {
        this.$message.error("分类名不能为空");
        return false;
      }
      this.axios.post("/api/back/category", this.category).then(({ data }) => {
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
        this.addOrEditStatus = false;
      });
    },
    changeCategoryStatus(category) {
      let param = {
        id: category.id,
        publicFlag: category.publicFlag,
        hiddenFlag: category.hiddenFlag
      };
      this.axios.put("/api/back/category/status", param);
    }
  },
  watch: {
    userId(newVal, oldVal) {
      if (oldVal != null) {
        this.listCategories();
      }
    }
  }
};
</script>
