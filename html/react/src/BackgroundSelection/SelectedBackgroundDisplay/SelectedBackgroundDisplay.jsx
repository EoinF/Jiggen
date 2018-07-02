import React, { Component } from 'react';
import { Subject } from 'rxjs';
import { debounceTime, throttleTime, distinctUntilChanged } from 'rxjs/operators'

import PlainLink from '../../utils/PlainLink';

import './selectedBackgroundDisplay.css';


class SelectedBackgroundDisplay extends Component {
	constructor(props) {
		super();
		this.state = {
			inputText: props.initialLink || '',
			link: props.initialLink || '',
			isValid: false
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
		if (prevState.inputText != this.state.inputText) {
			console.log("update", prevState.inputText, this.state.inputText);
	    	this.onChangeLink$.next(this.state.inputText);
		}
	}

	onChangeInputText = (e) => {
		console.log('set state of input', e.target.value);
		this.setState({inputText: e.target.value});
	};

	onLoad = (e) => {
		this.props.onSelectBackground(this.state.link);
		this.setState({isValid: true});
	};

	onError = (e) => {
		this.setState({isValid: false});
	};

	render() {
		const {
			inputText,
			link,
			isValid
		 } = this.state;
		return (
			<div className="backgroundDisplay">
				<div>
					<input
						value={inputText}
						placeholder="Paste a link here"
						onChange={this.onChangeInputText}
					/>
				</div>
				{ link && !isValid && (
					<img src={link} onLoad={this.onLoad} onError={this.onError} />
				)}
				{ link && isValid && (
					<PlainLink to={`/`}>
						<img src={link} onLoad={this.onLoad} onError={this.onError} />
					</PlainLink>
				)}
			</div>
		);
	}
}

export default SelectedBackgroundDisplay;