import React from 'react';

import styles from './ResponsiveImage.module.scss';

interface ResponsiveImageProps {
	src: string;
	alt: string;
}

const ResponsiveImage = ({src, alt} : ResponsiveImageProps) => {
	return (
		<div className={styles.mainContainer}>
			<img
				src={src}
				alt={alt}
			/>
		</div>
	);
}

export default ResponsiveImage;