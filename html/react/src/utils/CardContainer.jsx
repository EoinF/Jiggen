import React from 'react';
import './cardContainer.css';

const CardContainer = ({className, children}) => {
	return (
		<div className={"cardContainer " + className} >{children}</div>
	);
}

export default CardContainer;