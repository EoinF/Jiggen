import React from 'react';
import { NavLink, LinkProps } from 'react-router-dom';
import styles from './PlainNavLink.module.scss';

const PlainNavLink = (props: LinkProps) => {
	const {
		children,
		className,
		...otherProps
	} = props;
	return (
		<NavLink
			className={styles.noTextDecoration}
			{...otherProps}
		>
			{children}
		</NavLink>
	);
}

export default PlainNavLink;