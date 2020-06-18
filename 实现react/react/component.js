import { enqueueSetState } from "./set_state_queue";
import { renderComponent } from "../react-dom";

class Component {
	constructor(props = {}) {
		this.props = props;
		this.state = {};
	}
	setState(stateChange) {
		// 修改state，对象拷贝
		// Object.assign(this.state, stateChange);
		// 渲染组件
		// renderComponent(this);
		enqueueSetState(stateChange, this);
	}
}

export default Component;
