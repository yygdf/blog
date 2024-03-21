import Vue from "vue";
import Confirm from "@/components/Confirm.vue";

const VueComponent = Vue.extend(Confirm);
const vm = new VueComponent().$mount();
let init = false;
let defaultOptions = {};

const confirm = function(options) {
  Object.assign(vm, defaultOptions, options, {
    type: "confirm"
  });
  if (!init) {
    document.body.appendChild(vm.$el);
    init = true;
  }
  return vm.confirm();
};
export default confirm;
