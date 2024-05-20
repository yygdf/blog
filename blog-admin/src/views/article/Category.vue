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
        v-if="type !== 7"
        :disabled="categoryIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        :disabled="categoryIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        批量删除
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
          placeholder="请输入分类名"
          clearable
          @keyup.enter.native="getCategories(false)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getCategories(false)"
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
        v-if="checkWeight(200) && showColumnConfig.username"
        prop="username"
        label="用户"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.categoryName"
        prop="categoryName"
        label="分类名"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.articleCount"
        prop="articleCount"
        label="文章数"
        align="center"
        width="80"
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
      <el-table-column
        v-if="showColumnConfig.publicFlag"
        prop="publicFlag"
        label="公开"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.publicFlag"
            :disabled="type != null"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="updateCategoryStatus(scope.row, 2)"
          />
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.hiddenFlag"
        prop="hiddenFlag"
        label="隐藏"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.hiddenFlag"
            :disabled="type != null"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="updateCategoryStatus(scope.row, 3)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template slot="header">
          <el-popover placement="bottom" title="选择显示列" width="160">
            <div>
              <el-checkbox v-model="showColumnConfig.username"
                >用户</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.categoryName"
                >分类名</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.articleCount"
                >文章数</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.createTime"
                >创建日期</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.updateTime"
                >更新日期</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.publicFlag"
                >公开</el-checkbox
              >
              <div />
              <el-checkbox v-model="showColumnConfig.hiddenFlag"
                >隐藏</el-checkbox
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
            :disabled="type != null"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> 编辑
          </el-button>
          <el-popconfirm
            v-if="type !== 7"
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="updateCategoriesStatus(scope.row.id)"
          >
            <el-button
              :disabled="scope.row.articleCount !== 0"
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-delete" /> 删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            title="确定彻底删除吗？"
            style="margin-left:10px"
            @confirm="deleteCategories(scope.row.id)"
          >
            <el-button
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
    <el-dialog :visible.sync="editStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="editStatus = false">取 消</el-button>
        <el-button type="primary" @click="updateCategoriesStatus(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="removeStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否彻底删除选中项？</div>
      <div slot="footer">
        <el-button @click="removeStatus = false">取 消</el-button>
        <el-button type="primary" @click="deleteCategories(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="categoryTitle" />
      <el-form :model="category" size="medium" label-width="80">
        <el-form-item label="名称">
          <el-input
            v-model="category.categoryName"
            ref="input"
            class="word-limit-input"
            style="width: 200px"
            maxlength="50"
            placeholder="请输入分类名"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <el-form :model="category" :inline="true" size="medium" label-width="80">
        <el-form-item label="公开">
          <el-switch
            v-model="category.publicFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item label="隐藏">
          <el-switch
            v-model="category.hiddenFlag"
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
    this.loadColumnConfig();
    this.getCategories();
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
      categoryList: [],
      categoryIdList: [],
      category: {},
      categoryOrigin: {},
      showColumnConfig: {},
      type: null,
      userId: null,
      keywords: null,
      oldKeywords: null,
      loading: true,
      editStatus: false,
      removeStatus: false,
      addOrEditStatus: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    openModel(category) {
      if (category != null) {
        this.category = {
          id: category.id,
          categoryName: category.categoryName,
          publicFlag: category.publicFlag,
          hiddenFlag: category.hiddenFlag
        };
        this.$refs.categoryTitle.innerHTML = "修改分类";
      } else {
        this.category = {
          categoryName: "",
          publicFlag: true,
          hiddenFlag: false
        };
        this.$refs.categoryTitle.innerHTML = "添加分类";
      }
      this.categoryOrigin = JSON.parse(JSON.stringify(this.category));
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.getCategories(true);
    },
    checkWeight(weight) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.getCategories();
    },
    checkSelectable(row) {
      return !row.articleCount;
    },
    selectionChange(selection) {
      this.categoryIdList = [];
      selection.forEach(item => {
        this.categoryIdList.push(item.id);
      });
    },
    saveColumnConfig() {
      localStorage.setItem(
        "CategoryColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("CategoryColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("CategoryColumnSet")
        );
      } else {
        this.showColumnConfig = {
          username: true,
          categoryName: true,
          articleCount: true,
          createTime: true,
          updateTime: true,
          publicFlag: true,
          hiddenFlag: true
        };
      }
    },
    getCategories(resetCurrentPage) {
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
      this.axios.get("/api/back/categories", { params }).then(({ data }) => {
        this.count = data.data.count;
        this.categoryList = data.data.pageList;
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
    deleteCategories(id) {
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
          if (param.data.length === this.categoryList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getCategories();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.removeStatus = false;
    },
    addOrEditCategory() {
      if (this.category.categoryName.trim() === "") {
        this.$message.error("分类名不能为空");
        return false;
      }
      let param = this.$commonMethod.skipIdenticalValue(
        this.category,
        this.categoryOrigin
      );
      if (Object.keys(param).length === 0) {
        return false;
      }
      if (this.category.id != null) {
        param.id = this.category.id;
      }
      this.axios.post("/api/back/category", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.getCategories();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.addOrEditStatus = false;
    },
    updateCategoryStatus(category, type) {
      let param = {
        idList: [category.id],
        type: type
      };
      this.axios.put("/api/back/category/status", param).then(({ data }) => {
        if (!data.flag) {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
          if (type === 2) {
            category.publicFlag = !category.publicFlag;
          } else if (type === 3) {
            category.hiddenFlag = !category.hiddenFlag;
          }
        }
      });
    },
    updateCategoriesStatus(id) {
      let param = {};
      if (id != null) {
        param.idList = [id];
      } else {
        param.idList = this.articleIdList;
      }
      this.axios.put("/api/back/categories/status", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          if (param.idList.length === this.categoryList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getCategories();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.editStatus = false;
    }
  },
  watch: {
    type() {
      this.getCategories(true);
    },
    userId() {
      this.getCategories(true);
    }
  }
};
</script>
