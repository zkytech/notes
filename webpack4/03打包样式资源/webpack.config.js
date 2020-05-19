/**
 * webpack.config.js webpack的配置文件
 *  作用：指示webpack干哪些活（当webpack运行时，会加载这里面的配置）
 *  webpack是基于nodejs运行的，模块化默认采用commonjs
 */

// resolve 用来拼接绝对路径，保证各系统中使用正确的路径分隔符
const { resolve } = require('path')
module.exports = {
    // webpack配置
    // 入口起点
    entry: './src/index.js',
    // 输出
    output: {
        // 输出文件名
        filename: "built.js",
        // 输出路径
        // __dirname是nodejs的变量，代表当前文件的目录绝对路径
        path: resolve(__dirname, "build")
    },
    // loader的配置
    module: {
        rules: [
            // 详细的loader配置

            {
                // 匹配哪些文件，这里用正则表达式进行匹配
                test: /\.css$/,
                // 使用哪些loader进行处理
                // 按从下到上，从右到左的顺序调用
                use: [
                    // 通过js创建style标签，插入到html head中
                    'style-loader',
                    // 将css文件变成commonjs模块加载到js中，内容是样式字符串
                    'css-loader'
                ]
            },
            {
                test: /\.less$/,
                use: [
                    // 通过js创建style标签，插入到html head中
                    'style-loader',
                    // 将css文件变成commonjs模块加载到js中，内容是样式字符串
                    'css-loader',
                    // 将less编译为css
                    'less-loader'
                ]
            }

        ]
    },
    plugins: [
        // 详细的plugins的配置
    ],
    // 模式
    mode: 'development',
    // mode: 'production'
}
