import React from 'react';

import './puzzleStats.css'

const PuzzleStats = ({generatedTemplate, background}) => {
	if (generatedTemplate == null) {
		return (<div>
			Please select a template first
		</div>);
	} else if (background == null) {
		return (<div>
			Pick a background!
		</div>);
	} else {
		const {
			width,
			height
		} = generatedTemplate;
		console.log({generatedTemplate, background});

		const templateSize = `Template: ${width} x ${height}`;
		const backgroundSize = `Template: ${width} x ${height}`;

		return (
			<div className="launcherContainer">
				<div>{templateSize}</div>
				<div>{backgroundSize}</div>
			</div>
		);
	}

}

export default PuzzleStats;