import { Component } from "../react";
import { diff ,diffNode} from "./diff";
const reactDOM = {
	render,
};
/**
 * 将react中的节点渲染到dom中
 * @param {*} vnode react节点
 * @param {*} container dom容器
 */
function render(vnode, container, dom) {
	return diff(dom, vnode, container);
	// return container.appendChild(_render(vnode));
}
/**
 * 创建组件(函数组件、类组件)
 * @param {*} comp 函数组件或者类组件
 * @param {*} props 组件的属性
 */
export function createComponent(comp, props) {
	let inst;
	if (comp.prototype && comp.prototype.render) {
		// 如果是类定义的组件，则创建实例 返回
		inst = new comp(props);
	} else {
		// 如果是函数定义的组件，将函数组件扩展成类组件  方便后面统一管理
		inst = new Component(props);
		// 替换constructor
		inst.constructor = comp;
		// 定义render函数;
		inst.render = function () {
			return this.constructor();
		};
	}
	return inst;
}

/**
 * 渲染组件
 * @param {*} comp 组件
 */
export function renderComponent(comp) {
	if (comp.base && comp.componentWillUpdate) comp.componentWillUpdate();
	// 调用renderer获取jsx对象
	const renderer = comp.render(comp.props);
	// 渲染jsx对象
	// const base = _render(renderer);
	const base = diffNode(comp.base, renderer)
	if (comp.base) {
		if (comp.componentDidUpdate) comp.componentDidUpdate();
	} else if (comp.componentDidMount) comp.componentDidMount();
	// // 节点替换
	// if (comp.base && comp.base.parentNode) {
	// 	comp.base.parentNode.replaceChild(base, comp.base);
	// }
	comp.base = base;
}

/**
 * 设置组件属性，并进行渲染
 * @param {*} comp 组件
 * @param {*} props 属性
 */
export function setComponentProps(comp, props) {
	// 首次渲染前执行componentWillMount
	if (!comp.base) {
		if (comp.componentWillMount) comp.componentWillMount();
	} else if (comp.componentWillReceiveProps) {
		comp.componentWillReceiveProps(props);
	}
	// 设置组件的属性
	comp.props = props;
	// 渲染组件
	renderComponent(comp);
}

/**
 * 渲染jsx对象,将虚拟DOM转换为真实DOM
 * @param {*} vnode
 */
function _render(vnode) {
	if (vnode === undefined || vnode === null || typeof vnode === "boolean")
		return;
	if (typeof vnode === "number") {
		vnode = String(vnode);
	}
	// 如果vnode时字符串
	if (typeof vnode === "string") {
		// 创建文本节点
		const textNode = document.createTextNode(vnode);
		// 将文本节点渲染到dom中
		return textNode;
	}
	// 如果tag是函数，则渲染组件
	if (typeof vnode.tag === "function") {
		// 1.创建组件
		const comp = createComponent(vnode.tag, vnode.attrs);
		// 2.设置组件的属性
		setComponentProps(comp, vnode.attrs);
		// 3.返回组件渲染的节点对象
		return comp.base;
	}
	// 否则就是虚拟DOM对象
	const { tag, attrs } = vnode;
	const dom = document.createElement(tag);
	if (attrs) {
		// 有属性
		Object.keys(attrs).forEach((key) => {
			const val = attrs[key];
			setAttribute(dom, key, val);
		});
	}

	// 递归渲染子节点
	if (vnode.childrens) vnode.childrens.forEach((child) => render(child, dom));

	return dom;
}
/**
 * 设置dom节点的属性
 * @param {*} dom dom节点
 * @param {*} key 键
 * @param {*} value 值
 */
export function setAttribute(dom, key, value) {
	// 将属性名className转换成class
	if (key === "className") {
		key = "class";
	}

	// 如果是事件 onClick onBlur ...
	if (/^on\w+/.test(key)) {
		// 转小写
		key = key.toLowerCase();
		dom[key] = value;
	}
	// 如果是style
	else if (key === "style") {
		// 如果style是空值或者是一个字符串
		if (!value || typeof value === "string") {
			dom.style.cssText = value || "";
		} else if (value && typeof value === "object") {
			// {width:20}
			for (let k in value) {
				if (typeof value[k] === "number") {
					dom.style[k] = value[k] + "px";
				} else {
					dom.style[k] = value[k];
				}
			}
		}
	}
	// 其它属性
	else {
		// if (key in dom) {
		// 	dom[key] = value || "";
		// }
		if (value) {
			// 更新属性值
			dom.setAttribute(key, value);
		} else {
			// 如果属性值为空，移除这个属性
			dom.removeAttribute(key);
		}
	}
}

export default reactDOM;
