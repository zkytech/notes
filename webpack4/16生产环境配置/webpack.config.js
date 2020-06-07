const {resolve} = require('path')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const OptimizeCssAssetsWebpackPlugin = require('optimize-css-assets-webpack-plugin')
// 定义nodejs环境变量。该环境变量可影响package.json中的browserslist
process.env.NODE_ENV = 'production'
// 复用loader
const commonCssLoader = [
    // 提取css成单独文件
    MiniCssExtractPlugin.loader,
    'css-loader',
    {
        // css兼容性处理
        // 还需要在pacakge.json中定义
        loader: 'postcss-loader',
        options: {
            ident: 'postcss',
            plugins: () => [
                require('postcss-preset-env')
            ]

        }
    }
]
module.exports = {
    entry: './src/js/index.js',
    output: {
        filename: 'js/built.js',
        path: resolve(__dirname, 'build')
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                use: [
                    ...commonCssLoader
                ]
            },
            {
                test: /\.less$/,
                use: [
                    ...commonCssLoader,
                    'less-loader'
                ]
            },
            /*
             * 正常来讲，一个文件只能被一个loader处理。
             * 当一个文件要被多个loader处理，那么一定要指定laoder执行的先后顺序：
             *   先执行eslint 再执行babel
             * */
            {
                // eslint代码规范检查
                // 在package.json中eslintConfig --> airbnb
                test: /\.js$/,
                exclude: /node_modules/,
                // 优先执行
                enforce: 'pre',
                loader: 'eslint-loader',
                options: {
                    fix: true
                }
            },
            {
                test: /\.js$/,
                exclude: /node_modules/,
                loader: 'babel-loader',
                options: {
                    // 预设：指示babel做怎么样的兼容性处理
                    presets: [
                        [
                            '@babel/preset-env',
                            {
                                // 按需加载
                                useBuiltIns: 'usage',
                                // 指定core-js版本
                                corejs: {
                                    version: 3
                                },
                                // 指定兼容性做到哪个版本浏览器
                                targets: {
                                    // 浏览器: 版本
                                    chrome: '60',
                                    firefox: '60',
                                    ie: '9',
                                    safari: '10',
                                    edge: '17'
                                }
                            }

                        ]

                    ]

                }
            },
            {
                test:/\.(jpg|png|gif)/,
                loader:'url-loader',
                options:{
                    limit: 8 * 1024,
                    name:'[hash:10].[ext]',
                    outputPath:'imgs',
                    esModule:false
                }
            },
            {
                test:/\.html$/,
                loader:'html-loader'
            },
            {
                exclude: /\.(js|css|less|html|jpg|png|gif)/,
                loader:'file-loader',
                options: {
                    outputPath:'media'
                }
            }


        ]
    },
    plugins: [
        new MiniCssExtractPlugin({
            filename: 'css/built.css'
        }),
        new HtmlWebpackPlugin({
            template: './src/index.html',
            minify: {
                // 移除空格
                collapseWhitespace: true,
                // 移除注释
                removeComments: true
            }

        }),
        new OptimizeCssAssetsWebpackPlugin()

    ],
    mode: 'production'

}
