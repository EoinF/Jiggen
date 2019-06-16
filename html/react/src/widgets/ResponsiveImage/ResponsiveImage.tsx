import React, {Component, RefObject} from 'react';

import styles from './ResponsiveImage.module.scss';

interface ResponsiveImageProps {
	src: string;
	alt: string;
}

class ResponsiveImage extends Component<ResponsiveImageProps> {
	constructor(props: ResponsiveImageProps) {
		super(props);
	}

	render() {
		const {
			src,
			alt
		} = this.props;

		return (
			<div className={styles.mainContainer}>
				<img
					src={src}
					alt={alt}
				/>
			</div>
		);
	}
};

export default ResponsiveImage;