<template>
  <div class="rightSide" :style="isShow">
    <div :class="'rightSide-config-hide ' + isOut">
      <i :class="'iconfont rightSide-icon ' + icon" @click="check" />
    </div>
    <div class="setting-container" @click="show">
      <i class="iconfont my-icon-setting setting" />
    </div>
    <div class="setting-container" @click="change">
      {{ $i18n.locale === "en_US" ? "zh" : "en" }}
    </div>
    <i @click="backTop" class="iconfont rightSide-icon my-icon-up" />
  </div>
</template>

<script>
export default {
  mounted() {
    window.addEventListener("scroll", this.scrollToTop);
  },
  destroyed() {
    window.removeEventListener("scroll", this.scrollToTop);
  },
  data: function() {
    return {
      isShow: "",
      isOut: "rightSide-out",
      icon: "my-icon-moon"
    };
  },
  methods: {
    backTop() {
      window.scrollTo({
        behavior: "smooth",
        top: 0
      });
    },
    scrollToTop() {
      this.scrollTop =
        window.pageYOffset ||
        document.documentElement.scrollTop ||
        document.body.scrollTop;
      if (this.scrollTop > 20) {
        this.isShow = "opacity: 1;transform: translateX(-38px);";
      } else {
        this.isShow = "";
      }
    },
    show() {
      const flag = this.isOut === "rightSide-out";
      this.isOut = flag ? "rightSide-in" : "rightSide-out";
    },
    check() {
      const flag = this.icon === "my-icon-moon";
      this.icon = flag ? "my-icon-sun" : "my-icon-moon";
      this.$vuetify.theme.dark = !this.$vuetify.theme.dark;
    },
    change() {
      if (this.$i18n.locale === "en_US") {
        localStorage.setItem("lang", "zh_CN");
        this.$i18n.locale = "zh_CN";
      } else {
        localStorage.setItem("lang", "en_US");
        this.$i18n.locale = "en_US";
      }
    }
  }
};
</script>

<style scoped>
.rightSide {
  z-index: 4;
  position: fixed;
  right: -38px;
  bottom: 85px;
  transition: all 0.5s;
}
.rightSide-config-hide {
  transform: translate(35px, 0);
}
.rightSide-out {
  animation: rightSideOut 0.3s;
}
.rightSide-in {
  transform: translate(0, 0) !important;
  animation: rightSideIn 0.3s;
}
.rightSide-icon,
.setting-container {
  display: block;
  margin-bottom: 2px;
  width: 30px;
  height: 30px;
  background-color: #49b1f5;
  color: #fff;
  text-align: center;
  font-size: 16px;
  line-height: 30px;
  cursor: pointer;
}
.rightSide-icon:hover,
.setting-container:hover {
  background-color: #ff7242;
}
.setting-container i {
  display: block;
  animation: turn-around 2s linear infinite;
}
@keyframes turn-around {
  0% {
    transform: rotate(0);
  }
  100% {
    transform: rotate(360deg);
  }
}
@keyframes rightSideOut {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(30px, 0);
  }
}
@keyframes rightSideIn {
  0% {
    transform: translate(30px, 0);
  }
  100% {
    transform: translate(0, 0);
  }
}
</style>
