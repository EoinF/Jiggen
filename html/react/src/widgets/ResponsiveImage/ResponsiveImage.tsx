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
}

class ResponsiveImage extends Component<ResponsiveImageProps, ResponsiveImageState> {
	containerRef: RefObject<HTMLDivElement>

	constructor(props: ResponsiveImageProps) {
		super(props);

		this.state = {
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

	componentDidUpdate(nextProps: ResponsiveImageProps) {
		if (this.props.src != nextProps.src || this.state.isCalculating) {
			this.updateSize();
		}
	}

	updateSize = () => {
		if (this.containerRef.current != null) {
			const {width, height} = this.containerRef.current.getBoundingClientRect();

			// Rendering isn't done yet, try again
			if (width === 0 && height === 0) {
				this.setState({
					isCalculating: true
				});
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