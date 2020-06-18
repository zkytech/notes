import Component from "./component.js";
const React = {
	createElement,
	Component,
};
/**
 * babel插件 transform-react-jsx调用此方法处理jsx
 * @param {*} tag jsx标签的名称，如div、button
 * @param {*} attrs jsx标签的属性
 * @param  {...any} childrens jsx标签下的子元素（children）
 */
function createElement(tag, attrs, ...childrens) {
	return {
		tag, // 外层的标签
		attrs, // 属性，是一个对象
		childrens, // 是一个数组
		key: attrs ? attrs.key || null : null,
	};
}
export default React;
exports.Component = Component;
