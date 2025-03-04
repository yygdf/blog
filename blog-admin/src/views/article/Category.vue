<template>
  <el-card class="main-card">
    <div class="title">
      {{ isEn ? this.$route.meta.nameEn : this.$route.name }}
    </div>
    <div class="operation-container">
      <el-button
        type="primary"
        size="small"
        icon="el-icon-plus"
        @click="openModel(null)"
      >
        {{ $t("button.add") }}
      </el-button>
      <el-button
        v-if="type !== 7"
        :disabled="categoryIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <el-button
        v-else
        :disabled="categoryIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-if="checkWeight(200)"
          v-model="userId"
          size="small"
          style="margin-right:1rem"
          :placeholder="$t('input.selectUser')"
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
          :placeholder="$t('input.select')"
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
          :placeholder="$t('category.inputName')"
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
          {{ $t("button.search") }}
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
        :label="$t('table.user')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.categoryName"
        prop="categoryName"
        :label="$t('category.name')"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.articleCount"
        prop="articleCount"
        :label="$t('category.articleCount')"
        align="center"
        width="120"
      />
      <el-table-column
        v-if="showColumnConfig.createTime"
        prop="createTime"
        :label="$t('table.createDate')"
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
        :label="$t('table.updateDate')"
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
        :label="$t('switch.public')"
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
        :label="$t('switch.hidden')"
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
      <el-table-column :label="$t('table.operate')" align="center" width="160">
        <template slot="header">
          <el-popover placement="bottom" width="160">
            <div>
              <el-checkbox
                :indeterminate="
                  columnCheckedCount > 0 && columnCheckedCount < columnCount
                "
                :value="columnCheckedCount === columnCount"
                @change="handleColumnCheckedAllChange"
                >{{ $t("table.showColumn") }}</el-checkbox
              >
              <el-divider></el-divider>
              <el-checkbox
                v-if="checkWeight(200)"
                v-model="showColumnConfig.username"
                @change="handleColumnCheckedChange"
                >{{ $t("table.user") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.categoryName"
                @change="handleColumnCheckedChange"
                >{{ $t("category.name") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.articleCount"
                @change="handleColumnCheckedChange"
                >{{ $t("category.articleCount") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.createTime"
                @change="handleColumnCheckedChange"
                >{{ $t("table.createDate") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.updateTime"
                @change="handleColumnCheckedChange"
                >{{ $t("table.updateDate") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.publicFlag"
                @change="handleColumnCheckedChange"
                >{{ $t("switch.public") }}</el-checkbox
              >
              <div />
              <el-checkbox
                v-model="showColumnConfig.hiddenFlag"
                @change="handleColumnCheckedChange"
                >{{ $t("switch.hidden") }}</el-checkbox
              >
              <div>
                <el-button
                  type="primary"
                  size="mini"
                  style="float: right"
                  plain
                  @click="saveColumnConfig"
                >
                  {{ $t("button.save") }}
                </el-button>
              </div>
            </div>
            <i slot="reference" class="el-icon-setting table-setting-icon"></i>
          </el-popover>
          <el-tooltip
            class="item"
            effect="dark"
            :content="$t('table.refresh')"
            placement="top"
          >
            <i
              class="el-icon-refresh table-refresh-icon"
              @click="getCategories(false)"
            ></i>
          </el-tooltip>
        </template>
        <template slot-scope="scope">
          <el-button
            :disabled="type != null"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> {{ $t("button.edit") }}
          </el-button>
          <el-popconfirm
            v-if="type !== 7"
            :title="$t('confirm.content3')"
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
              <i class="el-icon-delete" /> {{ $t("button.delete") }}
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            :title="$t('confirm.content4')"
            style="margin-left:10px"
            @confirm="deleteCategories(scope.row.id)"
          >
            <el-button
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-delete" /> {{ $t("button.delete") }}
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
        <i class="el-icon-warning" style="color:#ff9900" />{{
          $t("confirm.tip")
        }}
      </div>
      <div style="font-size:1rem">{{ $t("confirm.content5") }}</div>
      <div slot="footer">
        <el-button @click="editStatus = false">{{
          $t("confirm.no")
        }}</el-button>
        <el-button type="primary" @click="updateCategoriesStatus(null)">
          {{ $t("confirm.yes") }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="removeStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />{{
          $t("confirm.tip")
        }}
      </div>
      <div style="font-size:1rem">{{ $t("confirm.content6") }}</div>
      <div slot="footer">
        <el-button @click="removeStatus = false">{{
          $t("confirm.no")
        }}</el-button>
        <el-button type="primary" @click="deleteCategories(null)">
          {{ $t("confirm.yes") }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="categoryTitle" />
      <el-form :model="category" size="medium" label-width="90px">
        <el-form-item :label="$t('category.name')">
          <el-input
            v-model="category.categoryName"
            ref="input"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('category.inputName')"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <el-form
        :model="category"
        :inline="true"
        size="medium"
        label-width="90px"
      >
        <el-form-item :label="$t('switch.public')">
          <el-switch
            v-model="category.publicFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item :label="$t('switch.hidden')">
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
        <el-button @click="addOrEditStatus = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button type="primary" @click="addOrEditCategory">
          {{ $t("button.save") }}
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
      options: [],
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
      current: 1,
      columnCount: 7,
      columnCheckedCount: 0
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
        this.$refs.categoryTitle.innerHTML = this.$t("category.edit");
      } else {
        this.category = {
          categoryName: "",
          publicFlag: true,
          hiddenFlag: false
        };
        this.$refs.categoryTitle.innerHTML = this.$t("category.add");
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
    handleColumnCheckedChange(value) {
      if (value) {
        this.columnCheckedCount++;
      } else {
        this.columnCheckedCount--;
      }
    },
    handleColumnCheckedAllChange(value) {
      if (value) {
        this.initColumnConfig();
      } else {
        this.showColumnConfig = {
          username: false,
          categoryName: false,
          articleCount: false,
          createTime: false,
          updateTime: false,
          publicFlag: false,
          hiddenFlag: false
        };
        this.columnCheckedCount = 0;
      }
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
        this.columnCheckedCount = Object.values(this.showColumnConfig).reduce(
          (count, value) => {
            return value ? ++count : count;
          },
          0
        );
      } else {
        this.initColumnConfig();
      }
    },
    initColumnConfig() {
      this.showColumnConfig = {
        username: true,
        categoryName: true,
        articleCount: true,
        createTime: true,
        updateTime: true,
        publicFlag: true,
        hiddenFlag: true
      };
      this.columnCheckedCount = 7;
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
            title: this.$t("success"),
            message: data.message
          });
          if (param.data.length === this.categoryList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getCategories();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
      this.removeStatus = false;
    },
    addOrEditCategory() {
      if (this.category.categoryName.trim() === "") {
        this.$message.error(this.$t("category.nameRule1"));
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
            title: this.$t("success"),
            message: data.message
          });
          this.getCategories();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
            title: this.$t("failure"),
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
        param.idList = this.categoryIdList;
      }
      this.axios.put("/api/back/categories/status", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: this.$t("success"),
            message: data.message
          });
          if (param.idList.length === this.categoryList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getCategories();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
      this.editStatus = false;
    }
  },
  computed: {
    isEn() {
      return this.$i18n.locale === "en_US";
    }
  },
  watch: {
    type() {
      this.getCategories(true);
    },
    userId() {
      this.getCategories(true);
    },
    isEn: {
      handler() {
        this.options = [
          {
            value: null,
            label: this.$t("option.available")
          },
          {
            value: 7,
            label: this.$t("option.deleted")
          }
        ];
      },
      immediate: true
    }
  }
};
</script>
