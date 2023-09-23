<template>
  <el-menu
    :collapse="isShow"
    :default-active="this.$route.path"
    class="side-nav-bar"
    text-color="#BFCBD9"
    background-color="#304156"
    active-text-color="#409EFF"
    router
  >
    <el-scrollbar style="height: 100%;">
      <template v-for="route of this.$store.state.userMenuList">
        <template v-if="route.children[0].path">
          <el-submenu :key="route.path" :index="route.path">
            <template slot="title">
              <i :class="route.icon" />
              <span>{{ route.name }}</span>
            </template>
            <template v-for="(item, index) of route.children">
              <el-menu-item
                :index="item.path"
                :key="index"
                v-if="!item.hideFlag"
                :disabled="checkDisabledFlag(item.disabledFlag)"
                :style="checkStyle(item.hiddenFlag, item.disabledFlag)"
              >
                <i :class="item.icon" />
                <span slot="title">{{ item.name }}</span>
              </el-menu-item>
            </template>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item
            :index="route.path"
            :key="route.path"
            :disabled="checkDisabledFlag(route.disabledFlag)"
            :style="checkStyle(route.hiddenFlag, route.disabledFlag)"
          >
            <i :class="route.icon" />
            <span>{{ route.children[0].name }}</span>
            <template slot="title">
              <span v-show="isShow">{{ route.children[0].name }}</span>
            </template>
          </el-menu-item>
        </template>
      </template>
    </el-scrollbar>
  </el-menu>
</template>

<style scoped>
.side-nav-bar:not(.el-menu--collapse) {
  width: 210px;
}
.side-nav-bar {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
}
.side-nav-bar i {
  margin-right: 1rem;
}
.el-menu--collapse {
  width: 60px;
}
</style>

<script>
export default {
  created() {
    if (document.documentElement.clientWidth < 960) {
      this.$store.state.collapse = true;
    }
  },
  methods: {
    checkStyle(hiddenFlag, disabledFlag) {
      let style = "";
      if (this.checkWeight()) {
        if (disabledFlag) {
          style = "color: orange;";
        }
        if (hiddenFlag) {
          style = "color: red;";
        }
      }
      return style;
    },
    checkWeight(weight = 200) {
      return this.$store.state.weight <= weight;
    },
    checkDisabledFlag(disabledFlag) {
      return disabledFlag && !this.checkWeight();
    }
  },
  computed: {
    isShow() {
      return this.$store.state.collapse;
    }
  }
};
</script>
