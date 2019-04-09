import React from 'react';
import { Link, LinkProps } from 'react-router-dom';
import styles from './PlainLink.module.scss';

const PlainLink = (props: LinkProps) => {
	const {
		children,
		className,
		...otherProps
	} = props;
	return (
		<Link
			className={styles.noTextDecoration}
			{...otherProps}
		>
			{children}
		</Link>
	);
}

export default PlainLink;