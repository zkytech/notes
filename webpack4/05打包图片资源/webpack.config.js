const { resolve } = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
module.exports = {
    entry: resolve(__dirname, 'src/index.js'),
    output: {
        filename: 'built.js',
        path: resolve(__dirname, 'build')
    },
    module: {
        rules: [
            {
                test: /\.less$/,
                // 要使用多个loader处理，用use
                use: ['style-loader', 'css-loader', 'less-loader']
            },
            {   // 问题：默认处理不了html中的img图片
                // i表示忽略大小写
                test: /\.(jpg|png|gif)$/i,
                // 使用一个loader
                // 下载url-loader file-loader（url-loader依赖于file-loader）
                loader: 'url-loader',
                options: {
                    // 图片大小小于8kb，就会被base64处理
                    // 优点：减少请求数量（减轻服务器压力）
                    // 缺点：图片体积会更大（文件请求速度更慢）
                    limit: 8 * 1024,
                    // 给图片进行重命名
                    // [hash:10] 取图片hash的前十位
                    // [ext] 图片的原扩展名
                    name: '[hash:10].[ext]'
                }
            },
            {
                test: /\.html$/,
                // 处理html文件的img图片（负责引入img，从而能被url-loader处理）
                loader: 'html-loader'
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin(
            {
                template: resolve(__dirname, 'src/index.html')
            }
        )
    ],
    mode: 'development'
}