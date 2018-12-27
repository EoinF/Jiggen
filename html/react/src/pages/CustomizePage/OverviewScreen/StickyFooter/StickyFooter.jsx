import React from 'react';
import './stickyFooter.scss';

const StickyFooter = ({className, children}) => {
	return (
		<div className={'jiggenFooter ' + className} >
			<div className={'stickyFooter jiggenFooter ' + className} >
				{children}
			</div>
		</div>
	);
}

export default StickyFooter;