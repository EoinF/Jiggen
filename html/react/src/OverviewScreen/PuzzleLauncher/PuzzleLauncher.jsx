import React from 'react';
import PlainLink from '../../utils/PlainLink';
import { TemplateWidget } from '../../TemplateSelection';
import StickyFooter from '../StickyFooter';

import './puzzleLauncher.css'

const PuzzleLauncher = ({generatedTemplate, background}) => {
	if (generatedTemplate != null) {
    	const puzzleLink = `/puzzle/${generatedTemplate.id}?background=${background.links.image}`;
		return (
			<div className="launcherContainer">
				<div className="launcherContent">
					<TemplateWidget template={generatedTemplate} />
				</div>
				<StickyFooter>
					<div className="launcherControls">
						<PlainLink to={puzzleLink}>
							<div className="launcherButton">Start Solving!</div>
						</PlainLink>
					</div>
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