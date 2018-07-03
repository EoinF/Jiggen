import React from 'react';
import PlainLink from '../../utils/PlainLink';
import { TemplateWidget } from '../../TemplateSelection';
import StickyFooter from '../StickyFooter';

import './puzzleLauncher.css'

const PuzzleLauncher = ({generatedTemplate, background}) => {
	if (generatedTemplate == null) {
		return (<div>
			Please select a template first
		</div>);
	} else if (background == null) {
		return (<div>
			Pick a background!
		</div>);
	} else {
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
	}

}

export default PuzzleLauncher;