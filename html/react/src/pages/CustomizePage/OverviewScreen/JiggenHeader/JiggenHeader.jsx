import React from 'react';
import './jiggenHeader.css';

const JiggenHeader = ({className, children}) => {
	return (
		<div className={'stickyHeader'} >
			{children}
		</div>
	);
}

export default JiggenHeader;