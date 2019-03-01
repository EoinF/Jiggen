import React, { Component } from 'react';
import { Redirect } from 'react-router-dom';

import styles from './BackgroundSelectionForm.module.scss';
import ImageFileInput from '../../../widgets/ImageFileInput/ImageFileInput';
import { Background } from '../../../store/backgrounds';
import ImageLinkInput from '../../../widgets/ImageLinkInput/ImageLinkInput';

import upArrow from './up-arrow.png';

interface BackgroundSelectionFormProps {
	onSelectBackground(background: Background): void;
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
			this.props.onSelectBackground(this.state.validBackgroundImage);
			this.setState({isSubmitted: true});
		}
	}

	onValidUploadImage = (image: HTMLImageElement) => {
		this.setState({
			validBackgroundImage: new Background(image, false, true)
		})
	}
	
	onValidCustomImage = (image: HTMLImageElement) => {
		this.setState({
			validBackgroundImage: new Background(image, true)
		})
	}

	render() {
		const {
			isSubmitted
		} = this.state;

		if (isSubmitted) {
			return (<Redirect to="/custom" />);
		} else {
			return (
				<div className={styles.mainContainer}>
					<div className={styles.linkInputContainer}>
						<ImageLinkInput onValidImage={this.onValidCustomImage} />
					</div>
					<ImageFileInput onValidImage={this.onValidUploadImage}>
						<img
							className={styles.uploadIcon}
								src={upArrow}
								alt="Upload a file..."
							/>
					</ImageFileInput>
				</div>
			);
		}
	}
}

export default BackgroundSelectionForm;