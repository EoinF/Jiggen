import React, { Component } from 'react';
import PlainLink from '../PlainLink';

import styles from './ImageDisplayReel.module.scss';
import { Resource } from '../../models';

interface ImageDisplayReelProps {
	resourceList: Resource[];
	onClickLink(id: string): void;
	onError(resource: Resource): void;
}

class ImageDisplayReel extends Component<ImageDisplayReelProps> {
	render() {
		const {
			resourceList,
			onClickLink
		} = this.props;

		return (
			<ul className={styles.imageDisplayReel}>
				{
					resourceList.map(resource => (
						<li key={resource.id}>
							<PlainLink onClick={() => onClickLink(resource.id)} to="/custom">
								<img 
									src={resource.links['image-compressed'] || resource.links.image} 
									alt={resource.name}
									onError={() => this.props.onError(resource)}
								/>
							</PlainLink>
						</li>
					))
				}
			</ul>
		);
	}
}

export default ImageDisplayReel;