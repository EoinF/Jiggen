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
				<div className={styles.innerContainer}>
					<img
						src={src}
						alt={alt}
					/>
				</div>
			</div>
		);
	}
};

export default ResponsiveImage;