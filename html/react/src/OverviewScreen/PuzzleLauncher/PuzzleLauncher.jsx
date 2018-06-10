import React from 'react';
import PlainLink from '../../utils/PlainLink';
import { TemplateWidget } from '../../TemplateSelection';
import StickyFooter from '../StickyFooter';

import './puzzleLauncher.css'

const PuzzleLauncher = ({generatedTemplate}) => {
	if (generatedTemplate != null) {
    	const puzzleLink = `/puzzle/${generatedTemplate.id}`;
		return (
			<div className="launcherContainer">
				<div className="launcherContent">
					<TemplateWidget template={generatedTemplate} />
				</div>
				<StickyFooter>
					<PlainLink className="launcherControls" to={puzzleLink}>
						<div>
							<div className="launcherButton">Start Solving!</div>
						</div>
					</PlainLink>
				</StickyFooter>
			</div>
		);
	} else {
		return (<div>
			Please select a template first
		</div>);
	}

}

export default PuzzleLauncher;