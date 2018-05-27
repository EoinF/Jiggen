import React from 'react';
import './ContentOrMessage.css';

const ContentOrMessage = ({message, showContent, children}) => {
	if (showContent) {
		return children;
	} else {
		return (
			<div className="PlaceholderMessage">
				{message}
			</div>
		);
	}
}

export default ContentOrMessage;
