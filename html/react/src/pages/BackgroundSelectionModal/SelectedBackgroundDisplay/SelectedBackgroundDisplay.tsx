import React, { Component } from 'react';
import { Redirect } from 'react-router-dom';
import { Subject, Subscription } from 'rxjs';
import { debounceTime, throttleTime, distinctUntilChanged } from 'rxjs/operators'

import './selectedBackgroundDisplay.scss';
import ImageFileInput from '../../../widgets/ImageFileInput/ImageFileInput';

interface SelectedBackgroundDisplayProps {
	onSelectBackground(link: string): void;
}

interface SelectedBackgroundDisplayState {
	inputText: string;
	isValid: Boolean;
	isSubmitted: Boolean;
}

class SelectedBackgroundDisplay extends Component<SelectedBackgroundDisplayProps, SelectedBackgroundDisplayState> {
	image: HTMLImageElement;
	formRef: React.RefObject<HTMLFormElement>;
	onChangeLink$: Subject<string>;
	subscription: Subscription | undefined;

	constructor(props: SelectedBackgroundDisplayProps) {
		super(props);
		this.state = {
			inputText: '',
			isValid: false,
			isSubmitted: false
		}
		this.image = new Image();
		this.image.onload = this.onLoad;
		this.image.onerror = this.onError;
		this.image.crossOrigin = "";

		this.formRef = React.createRef();
		this.onChangeLink$ = new Subject();
	}

	componentDidMount() {
		this.subscription = this.onChangeLink$
			.pipe(
				debounceTime(400),
				throttleTime(350), 
				distinctUntilChanged()
			)
			.subscribe(link => this.image.src = link);
	}

	componentWillUnmount() {
		if (this.subscription) {
			this.subscription.unsubscribe();
		}
	}

	componentDidUpdate(prevProps: SelectedBackgroundDisplayProps, prevState: SelectedBackgroundDisplayState) {
		if (prevState.inputText !== this.state.inputText) {
	    	this.onChangeLink$.next(this.state.inputText);
		}
	}

	onSubmit = (e: React.SyntheticEvent) => {
		e.preventDefault();
		if (this.state.isValid) {
			let suggestedInputs;
			const suggestedInputsRaw = localStorage.getItem('suggestedInputs');
			if (suggestedInputsRaw == null) {
				suggestedInputs = [];
			} else {
				suggestedInputs = JSON.parse(suggestedInputsRaw);
			}

			if (!suggestedInputs.includes(this.image.src)) {
				suggestedInputs.push(this.image.src);
				localStorage.setItem('suggestedInputs', JSON.stringify(suggestedInputs));
			}
			
			this.props.onSelectBackground(this.image.src);

			this.setState({isSubmitted: true});
		}
	}

	onChangeInputText = (e: React.SyntheticEvent<HTMLInputElement>) => {
		this.setState({inputText: e.currentTarget.value});
	};

	onLoad = () => {
		this.setState({isValid: true}, () => 
			this.formRef.current!.dispatchEvent(new Event("submit"))
		);
	};

	onError = () => {
		this.setState({isValid: false});
	};

	render() {
		const {
			inputText,
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
					<div>
						<input
							value={inputText}
							placeholder="Paste a link here"
							onChange={this.onChangeInputText}
							name="background"
							type="text"
						/>
						<ImageFileInput />
					</div>
				</form>
			);
		}
	}
}

export default SelectedBackgroundDisplay;