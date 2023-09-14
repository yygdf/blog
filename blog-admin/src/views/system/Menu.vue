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
        新增菜单
      </el-button>
      <div style="margin-left:auto">
        <el-input
          v-model="keywords"
          ref="input"
          size="small"
          style="width:200px"
          prefix-icon="el-icon-search"
          placeholder="请输入菜单名"
          clearable
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
    <el-table
      v-loading="loading"
      :data="menuList"
      row-key="id"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="name" label="菜单名称" width="120" />
      <el-table-column prop="icon" label="菜单图标" align="center" width="80">
        <template slot-scope="scope">
          <i :class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="rank" label="排序指标" align="center" width="80" />
      <el-table-column prop="path" label="菜单路径" />
      <el-table-column prop="component" label="菜单组件" />
      <el-table-column prop="hideFlag" label="隐藏" align="center" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.hideFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="changeMenuStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="hiddenFlag" label="隐藏" align="center" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.hiddenFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="changeMenuStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="disabledFlag"
        label="禁用"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.disabledFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="changeMenuStatus(scope.row)"
          />
        </template>
      </el-table-column>
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
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-button
            :disabled="scope.row.parentId !== -1 || scope.row.id === homeMenuId"
            type="primary"
            size="mini"
            class="smallerBtn"
            @click="openModel(scope.row, true)"
          >
            <i class="el-icon-plus" /> 新增
          </el-button>
          <el-button
            type="warning"
            size="mini"
            class="smallerBtn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> 修改
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="deleteMenu(scope.row.id)"
          >
            <el-button
              :disabled="
                !scope.row.deletableFlag ||
                  (scope.row.children != null &&
                    scope.row.children.length !== 0)
              "
              size="mini"
              type="danger"
              slot="reference"
              class="smallerBtn"
            >
              <i class="el-icon-delete" /> 删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="menuTitle" />
      <el-form :model="menu" size="medium" label-width="80">
        <el-form-item v-if="menu.parentId" label="父菜单名">
          <el-select
            v-model="menu.parentId"
            size="small"
            style="width: 200px"
            placeholder="请选择"
          >
            <el-option
              v-for="item in menuList.filter(e => e.id !== homeMenuId)"
              :key="item.id"
              :value="item.id"
              :label="item.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="菜单名称">
          <el-input
            v-model="menu.name"
            :maxlength="50"
            ref="input"
            style="width:200px"
          />
          <span style="color: red;"> *</span>
        </el-form-item>
        <el-form-item label="菜单图标">
          <el-input
            v-model="menu.icon"
            :prefix-icon="menu.icon"
            style="width:200px"
            @focus="showIcon = true"
            @blur="showIcon = false"
          />
          <span style="color: red;"> *</span>
          <div
            v-show="showIcon"
            class="menu-container"
            style="width: 200px;margin-left: 67px"
          >
            <div
              v-for="(item, index) of iconList"
              :key="index"
              @mousedown="checkIcon(item)"
            >
              <i :class="item" /> {{ item }}
            </div>
          </div>
        </el-form-item>
        <el-form-item label="菜单路径">
          <el-input v-model="menu.path" :maxlength="50" style="width:200px" />
          <span style="color: red;"> *</span>
        </el-form-item>
        <el-form-item label="菜单组件">
          <el-input
            v-model="menu.component"
            :maxlength="50"
            style="width:200px"
          />
          <span style="color: red;"> *</span>
        </el-form-item>
        <el-form-item label="排序指标">
          <el-input-number
            v-model="menu.rank"
            :min="1"
            :max="127"
            value="1"
            controls-position="right"
          />
        </el-form-item>
      </el-form>
      <el-form :model="menu" :inline="true" size="medium" label-width="80">
        <el-form-item label="隐藏">
          <el-switch
            v-model="menu.hideFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item label="隐藏">
          <el-switch
            v-model="menu.hiddenFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item label="禁用">
          <el-switch
            v-model="menu.disabledFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addOrEditStatus = false">取 消</el-button>
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
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data() {
    return {
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
      ],
      menu: {},
      menuList: [],
      keywords: null,
      homeMenuId: null,
      loading: true,
      showIcon: false,
      addOrEditStatus: false
    };
  },
  methods: {
    checkIcon(icon) {
      setTimeout(() => {
        this.menu.icon = icon;
        this.showIcon = false;
      }, 0);
    },
    openModel(menu, flag = false) {
      if (menu == null) {
        this.menu = {
          name: "",
          icon: "",
          path: "",
          component: "Layout",
          rank: 127
        };
        this.$refs.menuTitle.innerHTML = "添加菜单";
      } else {
        if (menu.parentId !== -1) {
          this.menu = {
            id: menu.id,
            parentId: menu.parentId,
            icon: menu.icon,
            rank: menu.rank,
            path: menu.path,
            name: menu.name,
            component: menu.component,
            hideFlag: menu.hideFlag,
            hiddenFlag: menu.hiddenFlag,
            disabledFlag: menu.disabledFlag
          };
          this.$refs.menuTitle.innerHTML = "修改子菜单";
        } else {
          if (flag) {
            this.menu = {
              name: "",
              icon: "",
              path: "",
              component: "",
              rank: 127,
              parentId: menu.id
            };
            this.$refs.menuTitle.innerHTML = "添加子菜单";
          } else {
            this.menu = {
              id: menu.id,
              parentId: null,
              icon: menu.icon,
              rank: menu.rank,
              path: menu.path,
              name: menu.name,
              component: menu.component,
              hideFlag: menu.hideFlag,
              hiddenFlag: menu.hiddenFlag,
              disabledFlag: menu.disabledFlag
            };
            this.$refs.menuTitle.innerHTML = "修改菜单";
          }
        }
      }
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    listMenus() {
      this.axios
        .get("/api/back/menus", {
          params: {
            keywords: this.keywords
          }
        })
        .then(({ data }) => {
          this.homeMenuId = data.data.homeMenuId;
          this.menuList = data.data.dataList;
          this.loading = false;
        });
    },
    deleteMenu(id) {
      let param = { data: id };
      this.axios.delete("/api/back/menu", param).then(({ data }) => {
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
    addOrEditMenu() {
      if (this.menu.name.trim() === "") {
        this.$message.error("菜单名称不能为空");
        return false;
      }
      if (this.menu.icon.trim() === "") {
        this.$message.error("菜单图标不能为空");
        return false;
      }
      if (this.menu.path.trim() === "") {
        this.$message.error("菜单路径不能为空");
        return false;
      }
      if (this.menu.component.trim() === "") {
        this.$message.error("菜单组件不能为空");
        return false;
      }
      this.axios.post("/api/back/menu", this.menu).then(({ data }) => {
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
        this.addOrEditStatus = false;
      });
    },
    changeMenuStatus(menu) {
      let param = {
        id: menu.id,
        topFlag: menu.hideFlag,
        hiddenFlag: menu.hiddenFlag,
        publicFlag: menu.disabledFlag
      };
      this.axios.put("/api/back/menu/status", param);
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
  -webkit-box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
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
