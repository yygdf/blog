import Vue from "vue";
import VueI18n from "vue-i18n";
import en_US from "./en_US";
import zh_CN from "./zh_CN";
Vue.use(VueI18n);

const messages = {
  en_US,
  zh_CN
};
const i18n = new VueI18n({
  messages,
  locale: localStorage.getItem("lang") || "zh_CN"
});

export { i18n };

export function $t(args) {
  return i18n.tc.call(i18n, args);
}
