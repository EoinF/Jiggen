import React from 'react';
import { NavLink, LinkProps, NavLinkProps } from 'react-router-dom';
import styles from './PlainNavLink.module.scss';

const PlainNavLink = (props: NavLinkProps) => {
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