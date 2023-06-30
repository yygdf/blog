module.exports = {
  transpileDependencies: ["vuetify"],
  devServer: {
    host: "localhost",
    port: "80",
    publicPath: "/",
    proxy: {
      "/api": {
        target: "http://localhost:8080",
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
      title: "洧①個哋汸, 呮洧妳倁檤"
    }
  }
};
