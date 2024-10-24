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
      <div style="margin-left:auto">
        <el-input
          v-model="keywords"
          ref="input"
          size="small"
          style="width: 200px"
          prefix-icon="el-icon-search"
          :placeholder="$t('menu.inputName')"
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
          {{ $t("button.search") }}
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
      <el-table-column
        :prop="isEn ? 'nameEn' : 'name'"
        :label="$t('menu.name')"
        width="120"
      />
      <el-table-column
        prop="icon"
        :label="$t('menu.icon')"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <i :class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column
        prop="rank"
        :label="$t('menu.rank')"
        align="center"
        width="80"
      />
      <el-table-column prop="path" :label="$t('menu.path')" />
      <el-table-column prop="component" :label="$t('menu.component')" />
      <el-table-column
        prop="hideFlag"
        :label="$t('switch.hide')"
        align="center"
        width="80"
      >
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
      <el-table-column
        prop="hiddenFlag"
        :label="$t('switch.hidden')"
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
            @change="updateMenuStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="disabledFlag"
        :label="$t('switch.disabled')"
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
      <el-table-column :label="$t('table.operate')" align="center" width="200">
        <template slot-scope="scope">
          <el-button
            :disabled="scope.row.parentId !== -1 || scope.row.id === homeMenuId"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row, true)"
          >
            <i class="el-icon-plus" /> {{ $t("button.add") }}
          </el-button>
          <el-button
            type="warning"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> {{ $t("button.edit") }}
          </el-button>
          <el-popconfirm
            :title="$t('confirm.content4')"
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
              <i class="el-icon-delete" /> {{ $t("button.delete") }}
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="menuTitle" />
      <el-form :model="menu" size="medium" label-width="100px">
        <el-form-item v-if="menu.parentId" :label="$t('menu.parentMenu')">
          <el-select
            v-model="menu.parentId"
            size="small"
            class="form-input-width"
            :placeholder="$t('input.select')"
          >
            <el-option
              v-for="item in menuList.filter(e => e.id !== homeMenuId)"
              :key="item.id"
              :value="item.id"
              :label="isEn ? item.nameEn : item.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('menu.name')">
          <el-input
            v-model="menu.name"
            ref="input"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('menu.inputName')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('menu.nameEn')">
          <el-input
            v-model="menu.nameEn"
            ref="input"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('menu.inputNameEn')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('menu.icon')">
          <el-input
            v-model="menu.icon"
            :prefix-icon="menu.icon"
            class="form-input-width"
            :placeholder="$t('menu.inputIcon')"
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
        <el-form-item :label="$t('menu.path')">
          <el-input
            v-model="menu.path"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('menu.inputPath')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('menu.component')">
          <el-input
            v-model="menu.component"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('menu.inputComponent')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('menu.rank')">
          <el-input-number
            v-model="menu.rank"
            class="form-input-width"
            :min="1"
            :max="100"
            value="1"
            controls-position="right"
          />
        </el-form-item>
      </el-form>
      <el-form :model="menu" :inline="true" size="medium" label-width="100px">
        <el-form-item :label="$t('switch.hide')">
          <el-switch
            v-model="menu.hideFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item :label="$t('switch.hidden')">
          <el-switch
            v-model="menu.hiddenFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item :label="$t('switch.disabled')">
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
        <el-button @click="addOrEditStatus = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button type="primary" @click="addOrEditMenu">
          {{ $t("button.save") }}
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
    openModel(menu, flag) {
      if (menu == null) {
        this.menu = {
          name: "",
          nameEn: "",
          icon: "",
          path: "",
          component: "Layout",
          rank: 100,
          hideFlag: false,
          hiddenFlag: false,
          disabledFlag: false
        };
        this.$refs.menuTitle.innerHTML = this.$t("menu.add1");
      } else {
        if (menu.parentId !== -1) {
          this.menu = {
            id: menu.id,
            parentId: menu.parentId,
            name: menu.name,
            nameEn: menu.nameEn,
            icon: menu.icon,
            path: menu.path,
            component: menu.component,
            rank: menu.rank,
            hideFlag: menu.hideFlag,
            hiddenFlag: menu.hiddenFlag,
            disabledFlag: menu.disabledFlag
          };
          this.$refs.menuTitle.innerHTML = this.$t("menu.edit2");
        } else {
          if (flag) {
            this.menu = {
              parentId: menu.id,
              name: "",
              nameEn: "",
              icon: "",
              path: "",
              component: "",
              rank: 100,
              hideFlag: menu.hideFlag,
              hiddenFlag: menu.hiddenFlag,
              disabledFlag: menu.disabledFlag
            };
            this.$refs.menuTitle.innerHTML = this.$t("menu.add2");
          } else {
            this.menu = {
              id: menu.id,
              parentId: null,
              name: menu.name,
              nameEn: menu.nameEn,
              icon: menu.icon,
              path: menu.path,
              component: menu.component,
              rank: menu.rank,
              hideFlag: menu.hideFlag,
              hiddenFlag: menu.hiddenFlag,
              disabledFlag: menu.disabledFlag
            };
            this.$refs.menuTitle.innerHTML = this.$t("menu.edit1");
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
            title: this.$t("success"),
            message: data.message
          });
          this.getMenus();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
    },
    addOrEditMenu() {
      if (this.menu.name.trim() === "") {
        this.$message.error(this.$t("menu.nameRule1"));
        return false;
      }
      if (this.menu.nameEn.trim() === "") {
        this.$message.error(this.$t("menu.nameEnRule1"));
        return false;
      }
      if (this.menu.icon.trim() === "") {
        this.$message.error(this.$t("menu.iconRule1"));
        return false;
      }
      if (this.menu.path.trim() === "") {
        this.$message.error(this.$t("menu.pathRule1"));
        return false;
      }
      if (this.menu.component.trim() === "") {
        this.$message.error(this.$t("menu.componentRule1"));
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
            title: this.$t("success"),
            message: data.message
          });
          this.getMenus();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
            title: this.$t("failure"),
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
  },
  computed: {
    isEn() {
      return this.$i18n.locale === "en_US";
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
