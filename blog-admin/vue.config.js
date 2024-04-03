module.exports = {
  devServer: {
    host: "localhost",
    port: "8081",
    publicPath: "/",
    proxy: {
      "/api": {
        target: process.env.VUE_APP_API_URL,
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
      title: "只有你知道"
    }
  }
};

const path = require("path");
function resolve(dir) {
  return path.join(__dirname, dir);
}
