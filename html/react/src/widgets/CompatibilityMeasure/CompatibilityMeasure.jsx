import React, {Component} from 'react';

import generateColourMap from '../../utils/colorInterpolate';

class CompatibilityMeasure extends Component {

	constructor(props) {
		super(props);
		const colourMap = generateColourMap(['#f26', '#f38', '#f4b','#e4b', '#e5a', '#eb2', '#ec0', '#ec0', '#5e5']);
		this.state = {
			compatibility: null,
			colour: null,
			colourMap
		}
	}

	componentDidMount() {
		this.calculateCompatibility();
		this.calculateColours();
	}

	componentDidUpdate(prevProps, prevState) {
		if(prevProps.background !== this.props.background
			|| prevProps.generatedTemplate !== this.props.generatedTemplate) {
			this.calculateCompatibility();
		}
		if (prevState.compatibility !== this.state.compatibility) {
			this.calculateColours();
		}
	}

	getBottomHeavyRatio(a, b) {
		return Math.min(a, b) / Math.max(a, b);
	}

	calculateColours = () => {
		if (this.state.compatibility != null && !isNaN(this.state.compatibility)) {
			const {
				compatibility,
				colourMap
			} = this.state;
			const colour = colourMap(compatibility / 100);
			this.setState({colour});
		}
	}

	// weight = 0 => colour1
	// weight = 1 => colour2
	// weight = 0.5 => half way between colour1 and colour2
	interpolateColours = (colour1, colour2, weight) => {
		return {
			r: this.interpolateValues(colour1.r, colour2.r, weight),
			g: this.interpolateValues(colour1.g, colour2.g, weight),
			b: this.interpolateValues(colour1.b, colour2.b, weight), 
		}
	}

	interpolateValues = (v1, v2, weight) => {
		return v1  + (weight * (v2 - v1));
	}

	calculateCompatibility() {
		if (this.props.background != null
			&& this.props.generatedTemplate != null
			&& 'width' in this.props.background) {
			const {
				background: {
					width: w1,
					height: h1
				},
				generatedTemplate: {
					width: w2,
					height: h2
				}
			} = this.props;

			let x1 = this.getBottomHeavyRatio(w1, h1);
			let x2 = this.getBottomHeavyRatio(w2, h2);

			if (x1 > x2) {
				x1 = 1 / x1;
			} else {
				x2 = 1 / x2;
			}

			this.setState({
				compatibility: x1 * x2 * 100
			});
		} else {
			this.setState({compatibility: null});
		}
	}

	render() {
		if (this.state.compatibility == null
			|| this.state.colour == null) {
			return (<div>Loading...</div>);
		}
		else {
			const style = {
				background: this.state.colour
			};
			const text = this.state.compatibility.toString().split('.')[0] + '%';

			return (
				<div className={this.props.className} style={style}>{text}</div>
			);
		}
	}
}

export default CompatibilityMeasure;