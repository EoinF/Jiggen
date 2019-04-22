import React, { Component, ReactNode } from 'react';

import styles from './ImageDisplayReel.module.scss';
import { timer, Subscription } from 'rxjs';

interface ImageDisplayReelProps {
	displayComponents: ReactNode[];
}

interface ImageDisplayReelState {
	orderedDisplayComponents: ReactNode[],
	amountDisplayed: number;
}

class ImageDisplayReel extends Component<ImageDisplayReelProps, ImageDisplayReelState> {
	bottomMarkerRef: React.RefObject<HTMLLIElement>;
	timedSubscription: Subscription;

	state = {
		orderedDisplayComponents: [],
		amountDisplayed: 0
	}

	constructor(props: ImageDisplayReelProps) {
		super(props)
		this.bottomMarkerRef = React.createRef();
		this.timedSubscription = timer(1000, 400)
			.subscribe(this.checkBottomVisible);
	}

	componentDidMount() {
		this.sortComponents(this.props.displayComponents);
	}

	componentDidUpdate(prevProps: ImageDisplayReelProps) {
		if (prevProps.displayComponents != this.props.displayComponents) {
			this.sortComponents(this.props.displayComponents);
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

	sortComponents = (displayComponents: ReactNode[]) => {
		this.setState({
			orderedDisplayComponents: displayComponents,
			amountDisplayed: Math.min(6, displayComponents.length)
		});
	}

	render() {
		const {
			displayComponents
		} = this.props;

		const displayComponentsFiltered = displayComponents.slice(0, this.state.amountDisplayed);

		return (
			<ul className={styles.imageDisplayReel}>
				{ displayComponentsFiltered }
				<li ref={this.bottomMarkerRef} />
			</ul>
		);
	}
}

export default ImageDisplayReel;