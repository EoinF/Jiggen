import React, { Component } from 'react';
import PlainLink from '../../../widgets/PlainLink';

import styles from './ImageDisplayReel.module.scss';

class ImageDisplayReel extends Component {
	render() {
		const {
			imageLinks,
			onClickLink
		} = this.props;

		return (
			<ul className={styles.imageDisplayReel}>
				{ 
					imageLinks.map(link => (
						<li>
							<PlainLink onClick={() => onClickLink(link)} to="/custom">
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