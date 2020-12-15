module.exports = {
    devServer: {
        port: 8080,
    },
    chainWebpack: config => {
        config.plugin('html').tap(args => {
            args[0].title = "音乐管理平台"
            return args
        })
    }
}
