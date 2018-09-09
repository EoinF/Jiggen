import React, { Component } from 'react';
import PlainLink from '../../../widgets/PlainLink';

import './imageDisplayReel.css';

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
							<PlainLink onClick={() => onSelectBackground(link)} to="/">
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