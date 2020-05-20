
const { resolve } = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')

const mode = 'production'
const OptimizeCssAssetsWebpackPlugin = require("optimize-css-assets-webpack-plugin")
// 设置nodejs环境变量
process.env.NODE_ENV = mode

module.exports = {
    entry: resolve(__dirname, 'src/js/index.js'),
    output: {
        filename: 'js/built.js',
        path: resolve(__dirname, 'build')
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                use: [
                    // 创建style标签，将样式放进去
                    // 'style-loader',
                    // 这个loader取代style-loader，作用：提取js中的css成单独文件
                    MiniCssExtractPlugin.loader,
                    // 将css文件整合到js文件中
                    'css-loader',
                    // css兼容性处理，自动将可能存在兼容性问题的css语句转换为多浏览器版本 --> postcss-loader postcss-preset-env
                    // 使用loader的默认配置
                    // 'postcss-loader'
                    // 自定义loader的配置
                    {
                        loader: 'postcss-loader',
                        options: {
                            ident: 'postcss',
                            plugins: () => [
                                // postcss的插件
                                // 帮助postcss找到package.json中browserslist里面的配置，通过配置加载指定的css兼容性样式
                                /**
                                "browserslist": {
                                    "development": [
                                    "last 1 chrome version", // 兼容最新版本chrome
                                    "last 1 firefox version", // 兼容最新版本firefox
                                    "last 1 safari version" // 兼容最新版本safari
                                    ],
                                    "production": [
                                    ">0.2%", // 兼容占有率大于0.2%的浏览器
                                    "not dead", // 不兼容已经停止维护的浏览器
                                    "not op_nimi all" // 不兼容opera mini
                                    ]
                                }
                                 */
                                require('postcss-preset-env')()
                            ]
                        }
                    }

                ]
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: resolve(__dirname, 'src/index.html')
        }),
        new MiniCssExtractPlugin(
            {
                // 对输出的文件进行重命名
                filename: 'css/built.css'
            }
        ),
        // 压缩css
        new OptimizeCssAssetsWebpackPlugin()
    ],
    mode: mode
}