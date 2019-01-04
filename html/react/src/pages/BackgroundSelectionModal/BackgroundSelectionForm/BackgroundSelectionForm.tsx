import React, { Component } from 'react';
import { Redirect } from 'react-router-dom';

import styles from './BackgroundSelectionForm.module.scss';
import ImageFileInput from '../../../widgets/ImageFileInput/ImageFileInput';
import { Background } from '../../../store/backgrounds';
import ImageLinkInput from '../../../widgets/ImageLinkInput/ImageLinkInput';

import upArrow from './up-arrow.png';

interface BackgroundSelectionFormProps {
	onSelectBackground(link: string): void;
}

interface BackgroundSelectionFormState {
	validBackgroundImage?: Background;
	isSubmitted: boolean;
}

class BackgroundSelectionForm extends Component<BackgroundSelectionFormProps, BackgroundSelectionFormState> {
	formRef: React.RefObject<HTMLFormElement>;
	
	constructor(props: BackgroundSelectionFormProps) {
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
					onSubmit={this.onSubmit}
					ref={this.formRef}
				>
					<div className={styles.linkInputContainer}>
						<ImageLinkInput onValidImage={this.onValidImage} />
					</div>
					<ImageFileInput>
						<img
							className={styles.uploadIcon}
							 src={upArrow}
							 alt="Upload a file..."
						 />
					</ImageFileInput>
				</form>
			);
		}
	}
}

export default BackgroundSelectionForm;