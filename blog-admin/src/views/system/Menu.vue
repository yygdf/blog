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
          style="width: 200px"
          prefix-icon="el-icon-search"
          placeholder="请输入菜单名"
          clearable
          @keyup.enter.native="getMenus"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getMenus"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="menuList"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      row-key="id"
      height="720"
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
            @change="updateMenuStatus(scope.row, 8)"
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
            @change="updateMenuStatus(scope.row)"
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
            @change="updateMenuStatus(scope.row, 9)"
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
            class="smaller-btn"
            @click="openModel(scope.row, true)"
          >
            <i class="el-icon-plus" /> 新增
          </el-button>
          <el-button
            type="warning"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> 编辑
          </el-button>
          <el-popconfirm
            title="确定彻底删除吗？"
            style="margin-left:10px"
            @confirm="deleteMenus(scope.row.id)"
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
              class="smaller-btn"
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
            ref="input"
            class="word-limit-input"
            style="width: 200px"
            maxlength="50"
            placeholder="请输入菜单名称"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="菜单图标">
          <el-input
            v-model="menu.icon"
            :prefix-icon="menu.icon"
            style="width: 200px"
            placeholder="请选择菜单图标"
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
          <el-input
            v-model="menu.path"
            class="word-limit-input"
            style="width: 200px"
            maxlength="50"
            placeholder="请输入菜单路径"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="菜单组件">
          <el-input
            v-model="menu.component"
            class="word-limit-input"
            style="width: 200px"
            maxlength="50"
            placeholder="请输入菜单组件"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="排序指标">
          <el-input-number
            v-model="menu.rank"
            :min="1"
            :max="100"
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
    this.getMenus();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data() {
    return {
      iconList: [
        "el-icon-pear",
        "el-icon-apple",
        "el-icon-grape",
        "el-icon-cherry",
        "el-icon-coffee",
        "el-icon-orange",
        "el-icon-ice-tea",
        "el-icon-milk-tea",
        "el-icon-ice-drink",
        "el-icon-watermelon"
      ],
      menuList: [],
      menu: {},
      menuOrigin: {},
      keywords: "",
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
          rank: 100,
          hideFlag: false,
          hiddenFlag: false,
          disabledFlag: false
        };
        this.$refs.menuTitle.innerHTML = "添加菜单";
      } else {
        if (menu.parentId !== -1) {
          this.menu = {
            id: menu.id,
            parentId: menu.parentId,
            name: menu.name,
            icon: menu.icon,
            path: menu.path,
            component: menu.component,
            rank: menu.rank,
            hideFlag: menu.hideFlag,
            hiddenFlag: menu.hiddenFlag,
            disabledFlag: menu.disabledFlag
          };
          this.$refs.menuTitle.innerHTML = "修改子菜单";
        } else {
          if (flag) {
            this.menu = {
              parentId: menu.id,
              name: "",
              icon: "",
              path: "",
              component: "",
              rank: 100,
              hideFlag: menu.hideFlag,
              hiddenFlag: menu.hiddenFlag,
              disabledFlag: menu.disabledFlag
            };
            this.$refs.menuTitle.innerHTML = "添加子菜单";
          } else {
            this.menu = {
              id: menu.id,
              parentId: null,
              name: menu.name,
              icon: menu.icon,
              path: menu.path,
              component: menu.component,
              rank: menu.rank,
              hideFlag: menu.hideFlag,
              hiddenFlag: menu.hiddenFlag,
              disabledFlag: menu.disabledFlag
            };
            this.$refs.menuTitle.innerHTML = "修改菜单";
          }
        }
      }
      this.menuOrigin = JSON.parse(JSON.stringify(this.menu));
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    getMenus() {
      let params = {};
      if (this.keywords.trim() !== "") {
        params.keywords = this.keywords;
      }
      this.axios
        .get("/api/back/menus", {
          params
        })
        .then(({ data }) => {
          this.homeMenuId = data.data.homeMenuId;
          this.menuList = data.data.dataList;
          this.loading = false;
        });
    },
    deleteMenus(id) {
      let param = { data: [id] };
      this.axios.delete("/api/back/menus", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.getMenus();
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
      let param = this.$commonMethod.skipIdenticalValue(
        this.menu,
        this.menuOrigin
      );
      if (Object.keys(param).length === 0) {
        return false;
      }
      if (this.menu.id != null) {
        param.id = this.menu.id;
      } else if (this.menu.parentId != null) {
        param.parentId = this.menu.parentId;
      }
      this.axios.post("/api/back/menu", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.getMenus();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.addOrEditStatus = false;
    },
    updateMenuStatus(menu, type) {
      let param = {
        idList: [menu.id]
      };
      if (type != null) {
        param.type = type;
      }
      this.axios.put("/api/back/menu/status", param).then(({ data }) => {
        if (!data.flag) {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
          if (type === 8) {
            menu.hideFlag = !menu.hideFlag;
          } else if (type === 9) {
            menu.disabledFlag = !menu.disabledFlag;
          } else {
            menu.hiddenFlag = !menu.hiddenFlag;
          }
        }
      });
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
</style>
