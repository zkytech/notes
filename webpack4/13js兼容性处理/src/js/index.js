// @babel-polyfill 通过替换js的默认方法、
// import '@babel/polyfill'
import "core-js/stable"
import "regenerator-runtime/runtime"
const test = () => 0;
const promise = new Promise(resolve=>{
    setTimeout(()=>{
        console.log("xxxxxxxxxxxxxxxxxxxxxxx")
    },1000)
})
