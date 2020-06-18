/**
 * 1.异步更新state，短时间内把多个setState合并为一个(队列：先进先出)
 * 2.一段时间之后，循环清空队列，渲染组件
 */

import { renderComponent } from "../react-dom";

// 保存组件和state
const setStateQueue = [];
// 保存当前的组件
const renderQueue = [];

/**
 * 异步调用fn
 * @param {*} fn
 */
function defer(fn) {
	return Promise.resolve().then(fn);
}

/**
 * 使用队列的异步setState
 * @param {*} stateChange
 * @param {*} component
 */
export function enqueueSetState(stateChange, component) {
	if (setStateQueue.length === 0) {
		// defer(flush);
		setTimeout(flush, 0);
		/**
		 * defer的作用相当于
		 * setTimeout(flush,0)
		 *
		 */
	}

	// 1.短时间内合并多个setState
	setStateQueue.push({
		stateChange,
		component,
	});
	// 如果renderQueue里面没有组件，添加到队列中
	let r = renderQueue.some((item) => {
		return item === component;
	});
	if (!r) {
		// 证明是第一次添加
		renderQueue.push(component);
	}
}

/**
 * 每隔一段时间运行此函数，处理并清空队列中待处理的stateChange
 */
function flush() {
	let item, component;
	while ((item = setStateQueue.shift())) {
		const { stateChange, component } = item;
		// 保存之前的状态
		if (!component.prevState) {
			component.prevState = Object.assign({}, component.state);
		}
		if (typeof stateChange === "function") {
			// 是一个函数
			Object.assign(
				component.state,
				stateChange(component.prevState, component.props)
			);
		} else {
			// 是一个对象
			Object.assign(component.state, stateChange);
		}
		component.prevState = component.state;
	}

	while ((component = renderQueue.shift())) {
		renderComponent(component);
	}
}
