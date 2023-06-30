module.exports = {
  devServer: {
    host: "localhost",
    port: "8081",
    publicPath: "/",
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        // target: "http://115.29.197.145:8080",
        changeOrigin: true,
        pathRewrite: {
          "^/api": ""
        }
      }
    },
    disableHostCheck: true
  },
  chainWebpack: config => {
    config.resolve.alias.set("@", resolve("src"));
  },
  pages: {
    index: {
      entry: "./src/main.js",
      template: "./public/index.html",
      title: "后台管理系统"
    }
  }
};

const path = require("path");
function resolve(dir) {
  return path.join(__dirname, dir);
}