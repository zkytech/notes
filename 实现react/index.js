import React, { Component } from "./react";
import ReactDOM from "./react-dom";
const ele = (
	<div className="active" title="123">
		hello, <span>react</span>
	</div>
);

/** createElement(tag,attrs,child1,child2...)
    var ele = React.createElement("div", {
        className: "active",
        title: "123"
        }, "hello, ", React.createElement("span", null, "react"));
 */

// ReactDOM.render(ele, document.querySelector("#root"));

// function Home() {
// 	return (
// 		<div className="active" title="123">
// 			hello,<span>react</span>
// 		</div>
// 	);
// }
class Home extends Component {
	constructor(props) {
		super(props);
		this.state = {
			num: 0,
		};
	}

	componentWillMount() {
		console.log("组件将要加载");
	}
	componentWillReceiveProps(props) {
		console.log("组件将接收props:", props);
	}

	componentWillUpdate() {
		console.log("组件将要更新");
	}

	componentDidUpdate() {
		console.log("组件已完成更新");
	}

	componentWillUnmount() {
		console.log("组件将会被卸载");
	}

	componentDidMount() {
		console.log("组件加载完成");
		let i = 0;
		while (i < 100) {
			this.setState({ num: this.state.num + 1 });
			i++;
		}
	}

	handlerClick() {
		this.setState({
			num: this.state.num + 1,
		});
	}
	render() {
		return (
			<div className="active" title="123">
				hello,<span>react {this.state.num}</span>
				<button onclick={this.handlerClick.bind(this)}>点我</button>
			</div>
		);
	}
}
const title = "active";
console.log("Home:", <Home></Home>);
ReactDOM.render(<Home name={title}></Home>, document.querySelector("#root"));
// ReactDOM.render(ele, document.querySelector("#root"));
