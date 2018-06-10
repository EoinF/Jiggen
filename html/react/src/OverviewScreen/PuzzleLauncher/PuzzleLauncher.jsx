import React from 'react';
import PlainLink from '../../utils/PlainLink';
import { TemplateWidget } from '../../TemplateSelection';

import './puzzleLauncher.css'

const PuzzleLauncher = ({generatedTemplate}) => {
	if (generatedTemplate != null) {
    	const puzzleLink = `/puzzle/${generatedTemplate.id}`;
		return (
			<div className="LauncherContainer">
				<div className="LauncherContent">
					<TemplateWidget template={generatedTemplate} />
				</div>
				<PlainLink className="LauncherControls" to={puzzleLink}>
					<button>Start Solving!</button>
				</PlainLink>
			</div>
		);
	} else {
		return (<div>
			Please select a template first
		</div>);
	}

}

export default PuzzleLauncher;