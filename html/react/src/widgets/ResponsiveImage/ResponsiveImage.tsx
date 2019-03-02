import React, {Component, RefObject} from 'react';

import './ResponsiveImage.scss';

interface ResponsiveImageProps {
	src: string;
	alt: string;
}

interface ResponsiveImageState {
	width?: number;
	height?: number;
	isCalculating: boolean;
	renderAttempts: number;
}

const MAX_RENDER_ATTEMPTS = 2;

class ResponsiveImage extends Component<ResponsiveImageProps, ResponsiveImageState> {
	containerRef: RefObject<HTMLDivElement>

	constructor(props: ResponsiveImageProps) {
		super(props);

		this.state = {
			renderAttempts: 0,
			isCalculating: true
		}
		this.containerRef = React.createRef();

		window.addEventListener("resize", this.onResize);
	}
	componentDidMount() {
		this.updateSize();
	}

	componentWillUnmount() {
		window.removeEventListener("resize", this.onResize);
	}

	onResize = () => {
		this.setState({isCalculating: true, renderAttempts: 0});
	}

	componentDidUpdate(nextProps: ResponsiveImageProps) {
		if (this.props.src != nextProps.src) {
			this.setState({
				isCalculating: true,
				renderAttempts: 0
			})
		}
		if (this.state.isCalculating) {
			this.updateSize();
		}
	}

	updateSize = () => {
		if (this.containerRef.current != null) {
			const {width, height} = this.containerRef.current.getBoundingClientRect();

			// Rendering isn't done yet, try again
			if (width === 0 && height === 0) {
				if (this.state.renderAttempts < MAX_RENDER_ATTEMPTS) {
					this.setState({
						isCalculating: true,
						renderAttempts: this.state.renderAttempts + 1
					});
				}
			} else {
				this.setState({
					width,
					height,
					isCalculating: false
				});
			}
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