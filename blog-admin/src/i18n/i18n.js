import Vue from "vue";
import ElementUILocale from "element-ui/lib/locale";
import ElementUI_EN from "element-ui/lib/locale/lang/en";
import ElementUI_ZH_CN from "element-ui/lib/locale/lang/zh-CN";
import VueI18n from "vue-i18n";
import en_US from "./en_US";
import zh_CN from "./zh_CN";

Vue.use(VueI18n);

const messages = {
  en_US: { ...en_US, ...ElementUI_EN },
  zh_CN: { ...zh_CN, ...ElementUI_ZH_CN }
};
const i18n = new VueI18n({
  messages,
  locale: localStorage.getItem("lang") || "zh_CN"
});
ElementUILocale.i18n((key, value) => i18n.t(key, value));

export { i18n };
