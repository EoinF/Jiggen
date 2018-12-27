import React, { Component } from 'react';
import { Redirect } from 'react-router-dom';
import { Subject } from 'rxjs';
import { debounceTime, throttleTime, distinctUntilChanged } from 'rxjs/operators'

import './selectedBackgroundDisplay.scss';


class SelectedBackgroundDisplay extends Component {
	constructor(props) {
		super();
		this.state = {
			inputText: props.initialLink || '',
			link: props.initialLink || '',
			isValid: false,
			isSubmitted: false
		}
	    this.onChangeLink$ = new Subject();
	}

	componentDidMount() {
		this.subscription = this.onChangeLink$
			.pipe(
				debounceTime(400),
				throttleTime(350), 
				distinctUntilChanged()
			)
			.subscribe(link => this.setState({link}));
	}
	componentWillUnmount() {
		if (this.subscription) {
			this.subscription.unsubscribe();
		}
	}

	componentDidUpdate(prevProps, prevState) {
		if (prevState.inputText !== this.state.inputText) {
	    	this.onChangeLink$.next(this.state.inputText);
		}
	}

	onSubmit = (e) => {
		e.preventDefault();

		if (this.state.isValid) {
			let suggestedInputs = JSON.parse(localStorage.getItem('suggestedInputs'));
			if (suggestedInputs === null) {
				suggestedInputs = [];
			}

			console.log(this.state.link);
			console.log({suggestedInputs});
			if (!suggestedInputs.includes(this.state.link)) {
				suggestedInputs.push(this.state.link);
				localStorage.setItem('suggestedInputs', JSON.stringify(suggestedInputs));
			}

			this.setState({isSubmitted: true});
		}
	}

	onChangeInputText = (e) => {
		this.setState({inputText: e.target.value});
	};

	onLoad = (e) => {
		this.props.onSelectBackground(this.state.link);
		this.setState({isValid: true});
	};

	onError = (e) => {
		this.setState({isValid: false});
	};

	getImageElement = () => {
		return (
			<div className="imageContainer">
				<button type="submit" className="imageButton">
					<img
						src={this.state.link}
						onLoad={this.onLoad}
						onError={this.onError}
						crossOrigin=""
						alt="error loading background"
					 />
				 </button>
			</div>
		);
	}

	render() {
		const {
			inputText,
			link,
			isSubmitted
		} = this.state;

		if (isSubmitted) {
			return (<Redirect to="/" />)
		} else {
			return (
				<form
					className="backgroundDisplay" 
					onSubmit={this.onSubmit}
				>
					<div>
						<input
							value={inputText}
							placeholder="Paste a link here"
							onChange={this.onChangeInputText}
							name="background"
							type="text"
						/>
					</div>
					{ link && (
						this.getImageElement()
					)}
				</form>
			);
		}
	}
}

export default SelectedBackgroundDisplay;