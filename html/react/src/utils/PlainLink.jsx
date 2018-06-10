import React from 'react';
import { Link } from 'react-router-dom';

const PlainLink = (props) => {
	const {
		children,
		...otherProps
	} = props;
	return (
		<Link
			style={{ textDecoration: 'none', color: 'inherit' }}
			{...otherProps}
		>
		{children}
		</Link>
	);
}

export default PlainLink;