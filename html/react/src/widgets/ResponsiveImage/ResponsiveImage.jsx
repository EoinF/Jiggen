import React, {Component} from 'react';

import './ResponsiveImage.scss';

class ResponsiveImage extends Component {

	constructor(props) {
		super(props);

		this.state = {
			width: null,
			height: null,
			isCalculating: true
		}
		this.containerRef = React.createRef();

		window.addEventListener("resize", () => {
			this.setState({isCalculating: true});
		});
	}
	componentDidMount() {
		this.updateSize();
	}

	componentDidUpdate() {
		this.updateSize();
	}

	updateSize = () => {
		if (this.state.isCalculating) {
		const {width, height} = this.containerRef.current.getBoundingClientRect();

			this.setState({
				width,
				height,
				isCalculating: false
			});
		}
	};

	render() {
		const {
			src,
			alt
		} = this.props;
		const {
			width, 
			height,
			isCalculating
		} = this.state;

		const imgStyle = {
			maxWidth: width,
			maxHeight: height
		};

		return (
			<div className='responsiveImage' ref={this.containerRef}>
				{!isCalculating && (
					<img 
						style={imgStyle}
						src={src}
						alt={alt}
					/>
				)}
			</div>
		);
	}
};

export default ResponsiveImage;