import React from 'react';
import './cardContainer.scss';

const CardContainer = ({className, children}) => {
	return (
		<div className={"cardContainer " + className} >{children}</div>
	);
}

export default CardContainer;