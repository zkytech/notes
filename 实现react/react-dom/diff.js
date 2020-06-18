import { setAttribute, setComponentProps, createComponent } from "./index";

/**
 * diff算法
 * @param {*} dom 真实DOM
 * @param {*} vnode 虚拟DOM
 * @param {*} container 容器
 */
export function diff(dom, vnode, container) {
	// 对比节点的变化
	const ret = diffNode(dom, vnode);
	if (container) {
		console.log("appending ret");
		container.appendChild(ret);
	}
	return ret;
}

export function diffNode(dom, vnode) {
	let out = dom;
	if (vnode === undefined || vnode === null || typeof vnode === "boolean")
		return;
	if (typeof vnode === "number") {
		vnode = String(vnode);
	}
	// 如果vnode是字符串
	if (typeof vnode === "string") {
		// dom.nodeType === 3 代表元素或属性中的文本内容。
		// 如果dom存在，就尝试对文本节点进行更新
		if (dom && dom.nodeType === 3) {
			if (dom.textContent !== vnode) {
				// 更新文本内容
				dom.textContent = vnode;
			}
		}
		// dom不存在，或节点类型发生改变
		else {
			out = document.createTextNode(vnode);
			if (dom && dom.parentNode) {
				// 替换原有节点
				dom.parentNode.replaceNode(out, dom);
			}
		}
		return out;
	}
	// 非文本DOM节点
	if (typeof vnode.tag === "function") {
		return diffComponent(out, vnode);
	}
	// 如果不存在原有的dom
	if (!dom) {
		out = document.createElement(vnode.tag);
	}
	// 比较子节点(dom节点和组件)
	if (
		(vnode.childrens && vnode.childrens.length > 0) ||
		(out.childNodes && out.childNodes.length > 0)
	) {
		console.log("diff children");
		// 对比组件 或者子节点
		diffChildren(out, vnode.childrens);
	}
	diffAttribute(out, vnode);
	return out;
}

/**
 * 对组件做diff，并更新dom
 * @param {*} dom
 * @param {*} vnode
 */
function diffComponent(dom, vnode) {
	let comp = dom;
	// 如果组件没有变化，重新设置props
	if (comp && comp.constructor === vnode.tag) {
		// 重新设置props
		setComponentProps(comp, vnode.attrs);
		// 复制
		dom = comp.base;
	} else {
		// 组件类型发生变化
		if (comp) {
			// 卸载旧组件
			unmountComponent(comp);
			comp = null;
		}
		// 1.创建新组件
		comp = createComponent(vnode.tag, vnode.attrs);
		// 2.设置组件属性
		setComponentProps(comp, vnode.attrs);
		// 3.给当前挂载base
		dom = comp.base;
	}
	return dom;
}

/**
 * 移除组件
 * @param {*} comp
 */
function unmountComponent(comp) {
	removeNode(comp.base);
	comp.componentWillUnmount();
}

function removeNode(dom) {
	if (dom && dom.parentNode) {
		dom.parentNode.removeNode(dom);
	}
}

/**
 * 对children做diff，并返回更新后的dom
 * @param {*} dom
 * @param {*} vchildren
 */
function diffChildren(dom, vchildren) {
	const domChildren = dom.childNodes;
	const children = [];
	const keyed = {};

	// 将有key的节点(用对象保存)和没有key的节点(用数组保存)分开
	if (domChildren.length > 0) {
		domChildren.forEach((c) => {
			// 将有key的节点存入keyed
			if (c.attributes && "key" in c.attributes) keyed[c.attributes["key"]] = c;
			// 将没有key的节点存入children
			else children.push(c);
		});
	}
	if (vchildren && vchildren.length > 0) {
		let min = 0;
		let childrenLen = children.length;
		[...vchildren].forEach((vchild, i) => {
			// 获取虚拟DOM中所有的key
			const key = vchild.key;
			let child;
			if (key) {
				// 如果有key，找到对应key值的节点
				if (keyed[key]) {
					child = keyed[key];
					keyed[key] = undefined;
				}
			} else if (childrenLen > min) {
				// 如果没有key，则优先找类型相同的节点
				for (let j = min; j < childrenLen; j++) {
					let c = children[j];
					if (c) {
						child = c;
						children[j] = undefined;
						if (j === childrenLen - 1) childrenLen--;
						if (j === min) min++;
						break;
					}
				}
			}
			// 对比
			child = diffNode(child, vchild);
			// 更新DOM
			const f = domChildren[i];
			if (child && child !== dom && child !== f) {
				// ? child和dom怎么可能相等？
				// child与dom和f均不相等，有以下几种情况：1.child本身是新增的节点，2.child是原节点位置移动后得到的
				if (!f) {
					// 如果更新前的对应位置为空，说明此节点是新增的
					dom.appendChild(child);
					// 如果更新后的节点和更新前对应位置的下一个节点一样，说明这个位置的节点被删了
				} else if (child === f.nextSibling) {
					removeNode(f);
					// 将更新后的节点移动到正确的位置
				} else {
					// 注意insertBefore的用法，第一个参数是要插入的节点
					dom.insertBefore(child, f);
				}
			}
		});
	}
}

/**
 * 对dom的属性做diff，并进行更新
 * @param {*} dom
 * @param {*} vnode
 */
function diffAttribute(dom, vnode) {
	// 保存之前的DOM的所有属性
	const oldAttrs = {};
	const newAttrs = vnode.attrs;
	// dom 是原有的节点对象  vnode是虚拟DOM
	const domAttrs = dom.attributes;
	[...domAttrs].forEach((item) => {
		oldAttrs[item.name] = item.value;
	});
	// 比较
	// 如果原来的属性跟新的属性对比，不在新的属性中，则将其移除掉(设置属性值为undefined)
	for (let key in oldAttrs) {
		if (!(domKeyToVKey(key) in newAttrs)) {
			setAttribute(dom, key, undefined);
		}
	}
	// 更新属性、新增属性
	for (let vnodeKey in newAttrs) {
		let domKey = vKeyToDomKey(vnodeKey);
		if (oldAttrs[domKey] !== newAttrs[vnodeKey]) {
			// 值不同，或者key在原来的属性中不存在，进行更新
			setAttribute(dom, vnodeKey, newAttrs[vnodeKey]);
		}
	}
}

/**
 * 虚拟DOM的属性名转换为真实DOM的属性名
 * @param {string} vKey 虚拟DOM属性名
 */
function vKeyToDomKey(vKey) {
	let domKey = vKey;
	if (vKey === "className") domKey = "class";
	else if (/^on\w+/.test(vKey)) domKey = vKey.toLowerCase();
	return domKey;
}

/**
 * 真实DOM的属性名转换为虚拟DOM的属性名
 * @param {string} domKey 真实DOM属性名
 */
function domKeyToVKey(domKey) {
	let vKey = domKey;
	if (domKey === "class") vKey = "className";
	else if (/^on\w+/.test(domKey)) {
		const temp = domKey.split("");
		temp[2] = temp[2].toUpperCase();
		vKey = temp.join("");
	}
	return vKey;
}
