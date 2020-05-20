/**
 * 开发环境配置
 */

const { resolve } = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
module.exports = {
    entry: resolve(__dirname, 'src/index.js'),
    output: {
        filename: 'js/built.js',
        path: resolve(__dirname, 'build')
    },
    module: {
        rules: [
            // loader的配置
            {
                // 处理less资源
                // less,css最后都会整合到js文件中
                test: /\.less$/,
                use: ['style-loader', 'css-loader', 'less-loader']
            },
            {
                // 处理css资源
                // less,css最后都会整合到js文件中
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                // 处理图片资源
                test: /\.(jpg|png|gif)$/i,
                loader: 'url-loader',
                options: {
                    limit: 8 * 1024,
                    name: '[hash:10].[ext]',
                    // 输出到imgs目录
                    outputPath: 'imgs'
                }
            },
            {
                // 处理html中的img资源
                test: /\.html$/,
                loader: 'html-loader'
            },
            {
                // 处理其它资源
                exclude: /\.(html|js|css|less|jpg|png|gif)/,
                loader: 'file-loader',
                options: {
                    name: '[hash:10].[ext]',
                    outputPath: 'media'
                }
            }
        ],
    },
    plugins: [
        // plugins的配置
        // 复制html文件，并将资源插入到html中
        new HtmlWebpackPlugin({
            template: resolve(__dirname, 'src/index.html')
        })
    ],
    mode: 'development',
    devServer: {
        // web服务根目录位置
        contentBase: resolve(__dirname, 'build'),
        // 开启gzip压缩
        compress: true,
        // 服务端口
        port: 3000,
        // 自动打开浏览器
        open: true
    }
}