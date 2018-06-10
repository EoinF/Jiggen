import React from 'react';
import './messageBox.css';

const MessageBox = ({className, children}) => {
	return (
		<div className={"messageBox " + className} >{children}</div>
	);
}

export default MessageBox;