module.exports = {
  transpileDependencies: ["vuetify"],
  devServer: {
    host: "localhost",
    port: "80",
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
  pages: {
    index: {
      entry: "./src/main.js",
      template: "./public/index.html",
      title: "有一个地方"
    }
  }
};
