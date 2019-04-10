import React, { Component } from 'react';
import PlainLink from '../PlainLink';

import styles from './ImageDisplayReel.module.scss';
import { Resource } from '../../models';
import { timer, Subscription } from 'rxjs';

interface ImageDisplayReelProps {
	resourceList: Resource[];
	onClickLink(id: string): void;
	onError(resource: Resource): void;
}

interface ImageDisplayReelState {
	orderedResourceList: Resource[],
	amountDisplayed: number;
}

class ImageDisplayReel extends Component<ImageDisplayReelProps, ImageDisplayReelState> {
	bottomMarkerRef: React.RefObject<HTMLLIElement>;
	timedSubscription: Subscription;

	state = {
		orderedResourceList: [],
		amountDisplayed: 0
	}

	constructor(props: ImageDisplayReelProps) {
		super(props)
		this.bottomMarkerRef = React.createRef();
		this.timedSubscription = timer(1000, 400)
			.subscribe(this.checkBottomVisible);
	}

	componentDidMount() {
		this.sortResources(this.props.resourceList);
	}

	componentDidUpdate(prevProps: ImageDisplayReelProps) {
		if (prevProps.resourceList != this.props.resourceList) {
			this.sortResources(this.props.resourceList);
		}
	}

	checkBottomVisible = () => {
		const element = this.bottomMarkerRef.current;
		if (element != null) {
			var rect = element.getBoundingClientRect();
			if (rect.bottom <= (window.innerHeight || document.documentElement.clientHeight)) {
				this.setState({amountDisplayed: this.state.amountDisplayed + 2})
			}
		}
	}

	sortResources = (resourceList: Resource[]) => {
		this.setState({
			orderedResourceList: resourceList,
			amountDisplayed: Math.min(6, resourceList.length)
		});
	}

	render() {
		const {
			resourceList,
			onClickLink
		} = this.props;

		const resourcesDisplayed = resourceList.slice(0, this.state.amountDisplayed)

		return (
			<ul className={styles.imageDisplayReel}>
				{
					resourcesDisplayed.map(resource => (
						<li key={resource.links.self}>
							<div onClick={() => onClickLink(resource.links.self)}>
								<img 
									src={resource.links['image-compressed'] || resource.links.image} 
									alt={resource.name}
									onError={() => this.props.onError(resource)}
								/>
							</div>
						</li>
					))
				}
				<li ref={this.bottomMarkerRef} />
			</ul>
		);
	}
}

export default ImageDisplayReel;