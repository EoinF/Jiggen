import React from 'react';
import './jiggenHeader.scss';

const JiggenHeader = ({className, children}) => {
	return (
		<div className={'stickyHeader'} >
			{children}
		</div>
	);
}

export default JiggenHeader;