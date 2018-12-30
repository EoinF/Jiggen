import React, { Component } from 'react';
import PlainLink from '../../../widgets/PlainLink';

import './imageDisplayReel.scss';

class ImageDisplayReel extends Component {
	render() {
		const {
			imageLinks,
			onSelectBackground
		} = this.props;

		return (
			<ul className="imageDisplayReel">
				{ 
					imageLinks.map(link => (
						<li>
							<PlainLink onClick={() => onSelectBackground(link)} to="/custom">
								<img src={link} alt=""/>
							</PlainLink>
						</li>
					))
				}
			</ul>
		);
	}
}

export default ImageDisplayReel;