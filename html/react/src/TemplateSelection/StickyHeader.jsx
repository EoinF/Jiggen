import React from 'react';

import './stickyHeader.css'

const StickyHeader = ({children, className}) => {
	return (
		<div className={className}>
			<div className={'stickyHeader ' + className} >
				{children}
			</div>
		</div>
	);
}

export default StickyHeader;