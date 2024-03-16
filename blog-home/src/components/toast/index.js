import ToastComponent from "./Toast.vue";

const Toast = {};

Toast.install = function(Vue) {
  const ToastConstructor = Vue.extend(ToastComponent);
  const instance = new ToastConstructor();
  instance.$mount(document.createElement("div"));
  document.body.appendChild(instance.$el);
  Vue.prototype.$toast = (options, duration = 2000) => {
    instance.message = options.message;
    instance.type = options.type;
    instance.show = true;
    setTimeout(() => {
      instance.show = false;
    }, duration);
  };
};
export default Toast;
