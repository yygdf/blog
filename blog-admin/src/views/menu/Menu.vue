<template>
  <el-card class="main-card">
    <!-- 标题 -->
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        type="primary"
        size="small"
        icon="el-icon-plus"
        @click="openModel(null)"
      >
        新增菜单
      </el-button>
      <!-- 数据筛选 -->
      <div style="margin-left:auto">
        <el-input
          v-model="keywords"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入菜单名"
          style="width:200px"
          @keyup.enter.native="listMenus"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listMenus"
        >
          搜索
        </el-button>
      </div>
    </div>
    <!-- 权限列表 -->
    <el-table
      v-loading="loading"
      :data="menuList"
      row-key="id"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="name" label="菜单名称" width="150" />
      <el-table-column prop="icon" align="center" label="图标" width="100">
        <template slot-scope="scope">
          <i :class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column
        prop="orderNum"
        align="center"
        label="排序"
        width="100"
      />
      <el-table-column prop="path" label="访问路径" />
      <el-table-column prop="component" label="组件路径" />
      <el-table-column prop="isDisable" label="禁用" align="center" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isDisable"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="1"
            :inactive-value="0"
            @change="changeMenu(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="isHidden" label="隐藏" align="center" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isHidden"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="1"
            :inactive-value="0"
            @change="changeMenu(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        align="center"
        width="150"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="更新时间"
        align="center"
        width="150"
      >
        <template slot-scope="scope" v-if="scope.row.updateTime" >
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.updateTime | date }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            class="smallerBtn"
            @click="openModel(scope.row)"
            v-if="scope.row.children"
          >
            <i class="el-icon-plus" /> 新增
          </el-button>
          <el-button type="warning" size="mini" class="smallerBtn" @click="openEditModel(scope.row)">
            <i class="el-icon-edit" /> 修改
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="deleteMenu(scope.row.id)"
          >
            <el-button size="mini" type="danger" class="smallerBtn" slot="reference">
              <i class="el-icon-delete" /> 删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- 新增模态框 -->
    <el-dialog :visible.sync="addMenu" width="30%" top="12vh" @close="closeDialog">
      <div class="dialog-title-container" slot="title" ref="menuTitle" />
      <el-form label-width="80px" size="medium" :model="menuForm">
        <el-form-item label="菜单名称">
          <el-input v-model="menuForm.name" style="width:220px" />
        </el-form-item>
        <el-form-item label="菜单图标">
          <div @click="showIcon = true">
            <el-input
              :prefix-icon="icon"
              v-model="menuForm.icon"
              style="width:220px"
            />
          </div>
          <div class="menu-container" v-show="showIcon">
            <div
              v-for="(item, index) of iconList"
              :key="index"
              @click="checkIcon(item)"
            >
              <i :class="item" /> {{ item }}
            </div>
          </div>
        </el-form-item>
        <el-form-item label="路由地址">
          <el-input v-model="menuForm.path" style="width:220px" />
        </el-form-item>
        <el-form-item label="组件名称">
          <el-input v-model="menuForm.component" style="width:220px" />
        </el-form-item>
        <el-form-item label="显示排序">
          <el-input-number
            v-model="menuForm.orderNum"
            controls-position="right"
            value="1"
            :min="1"
            :max="10"
          />
        </el-form-item>
        <el-form-item label="父菜单" v-if="isEdit">
          <el-select
            v-model="menuForm.parentId"
            placeholder="请选择"
            size="small"
            style="margin-right:1rem"
          >
            <el-option
              v-for="item in menuList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="禁用">
          <el-switch
            v-model="menuForm.isDisable"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="1"
            :inactive-value="0"
          />
        </el-form-item>
        <el-form-item label="隐藏">
          <el-switch
            v-model="menuForm.isHidden"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="1"
            :inactive-value="0"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addMenu = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditMenu">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.listMenus();
  },
  data() {
    return {
      keywords: null,
      loading: true,
      addMenu: false,
      showIcon: false,
      isEdit: false,
      menuList: [],
      menuForm: {},
      icon: "",
      iconList: [
        "el-icon-grape",
        "el-icon-watermelon",
        "el-icon-cherry",
        "el-icon-apple",
        "el-icon-pear",
        "el-icon-orange",
        "el-icon-coffee",
        "el-icon-ice-tea",
        "el-icon-ice-drink",
        "el-icon-milk-tea"
      ]
    };
  },
  methods: {
    listMenus() {
      this.axios
        .get("/api/admin/menus", {
          params: {
            keywords: this.keywords
          }
        })
        .then(({ data }) => {
          this.menuList = data.data;
          this.loading = false;
        });
    },
    changeMenu(menu) {
      this.axios.post("/api/admin/menus", menu).then(({ data }) => {
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
    openModel(menu) {
      if (menu != null) {
        this.menuForm = {
          parentId: menu.id,
          orderNum: 1,
          component: "Layout"
        };
        this.menuForm.parentId = menu.id;
        this.$refs.menuTitle.innerHTML = "添加子菜单";
      } else {
        this.menuForm = {
          orderNum: 1,
          component: "Layout"
        };
        this.$refs.menuTitle.innerHTML = "添加菜单";
      }
      this.addMenu = true;
    },
    openEditModel(menu) {
      this.menuForm = JSON.parse(JSON.stringify(menu));
      this.$refs.menuTitle.innerHTML = "修改菜单";
      if (menu.parentId != null) {
        this.$refs.menuTitle.innerHTML = "修改子菜单";
        this.isEdit = true;
      }
      this.addMenu = true;
    },
    checkIcon(icon) {
      this.icon = icon;
      this.menuForm.icon = icon;
      this.showIcon = false;
    },
    addOrEditMenu() {
      if (this.menuForm.name.trim() == "") {
        this.$message.error("菜单名不能为空");
        return false;
      }
      if (this.menuForm.path.trim() == "") {
        this.$message.error("菜单路径不能为空");
        return false;
      }
      this.axios.post("/api/admin/menus", this.menuForm).then(({ data }) => {
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
        this.addMenu = false;
      });
    },
    deleteMenu(id) {
      var param = { data: [id] };
      this.axios.delete("/api/admin/menus", param).then(({ data }) => {
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
    closeDialog() {
      this.isEdit = false;
    }
  }
};
</script>

<style scoped>
.menu-container {
  position: absolute;
  width: 90%;
  background: #fff;
  border-radius: 4px;
  border: 1px solid #e6ebf5;
  padding: 12px;
  z-index: 2000;
  color: #606266;
  text-align: justify;
  font-size: 14px;
  /*-webkit-box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);*/
  -webkit-box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  /*box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);*/
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  word-break: break-all;
  overflow-y: auto;
  height: 200px;
}
.menu-container div {
  cursor: pointer;
}

.smallerBtn {
  padding: 5px;
}
</style>
