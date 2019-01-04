import React, { Component } from 'react';
import { Redirect } from 'react-router-dom';
import { Subject, Subscription } from 'rxjs';

import './selectedBackgroundDisplay.scss';
import ImageFileInput from '../../../widgets/ImageFileInput/ImageFileInput';
import { Background } from '../../../store/backgrounds';
import ImageLinkInput from '../../../widgets/ImageLinkInput/ImageLinkInput';

interface SelectedBackgroundDisplayProps {
	onSelectBackground(link: string): void;
}

interface SelectedBackgroundDisplayState {
	validBackgroundImage?: Background;
	isSubmitted: boolean;
}

class SelectedBackgroundDisplay extends Component<SelectedBackgroundDisplayProps, SelectedBackgroundDisplayState> {
	formRef: React.RefObject<HTMLFormElement>;
	
	constructor(props: SelectedBackgroundDisplayProps) {
		super(props);
		this.state = {
			isSubmitted: false
		}
		this.formRef = React.createRef();
	}

	componentDidUpdate() {
		if (!this.state.isSubmitted && this.state.validBackgroundImage != null) {
			this.formRef.current!.dispatchEvent(new Event("submit"));
		}
	}

	onSubmit = (e: React.SyntheticEvent) => {
		e.preventDefault();
		if (this.state.validBackgroundImage != null) {
			let suggestedInputs;
			const suggestedInputsRaw = localStorage.getItem('suggestedInputs');
			if (suggestedInputsRaw == null) {
				suggestedInputs = [];
			} else {
				suggestedInputs = JSON.parse(suggestedInputsRaw);
			}

			if (!suggestedInputs.includes(this.state.validBackgroundImage.links.image)) {
				suggestedInputs.push(this.state.validBackgroundImage.links.image);
				localStorage.setItem('suggestedInputs', JSON.stringify(suggestedInputs));
			}
			
			this.props.onSelectBackground(this.state.validBackgroundImage.links.image);

			this.setState({isSubmitted: true});
		}
	}

	onValidImage = (image: HTMLImageElement) => {
		this.setState({
			validBackgroundImage: new Background(image)
		})
	}

	render() {
		const {
			isSubmitted
		} = this.state;

		if (isSubmitted) {
			return (<Redirect to="/custom" />)
		} else {
			return (
				<form
					className="backgroundDisplay"
					onSubmit={this.onSubmit}
					ref={this.formRef}
				>
					<ImageLinkInput onValidImage={this.onValidImage} />
					<ImageFileInput />
				</form>
			);
		}
	}
}

export default SelectedBackgroundDisplay;