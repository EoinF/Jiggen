import React from 'react';
import { Link } from 'react-router-dom';
import styles from './PlainLink.module.scss';

const PlainLink = (props) => {
	const {
		children,
		className,
		...otherProps
	} = props;
	return (
		<Link
			className={[styles.noTextDecoration, className].join(' ')}
			{...otherProps}
		>
			{children}
		</Link>
	);
}

export default PlainLink;