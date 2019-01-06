import React, { Component } from 'react';
import PlainLink from '../../../widgets/PlainLink';

import styles from './ImageDisplayReel.module.scss';

class ImageDisplayReel extends Component {
	render() {
		const {
			resourceList,
			onClickLink
		} = this.props;

		return (
			<ul className={styles.imageDisplayReel}>
				{ 
					resourceList.map(resource => (
						<li>
							<PlainLink onClick={() => onClickLink(resource.id)} to="/custom">
								<img src={resource.links.image} alt=""/>
							</PlainLink>
						</li>
					))
				}
			</ul>
		);
	}
}

export default ImageDisplayReel;