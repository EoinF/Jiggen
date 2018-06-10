import React from 'react';
import './jiggenHeader.css';

const JiggenHeader = ({className, children}) => {
	return (
		<div className={'jiggenHeader ' + className} >
			<div className={'stickyHeader jiggenHeader ' + className} >
				{children}
			</div>
		</div>
	);
}

export default JiggenHeader;