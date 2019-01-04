import React, {Component} from 'react';
import { debounceTime, throttleTime, distinctUntilChanged } from 'rxjs/operators'
import { Subject, Subscription } from 'rxjs';

interface ImageLinkInputProps {
    onValidImage(image: HTMLImageElement): void;
}

interface ImageLinkInputState {
	inputText: string;
	isValid: Boolean;
	isSubmitted: Boolean;
}


class ImageLinkInput extends Component<ImageLinkInputProps, ImageLinkInputState> {
    image: HTMLImageElement;
	onChangeLink$: Subject<string>;
    subscription: Subscription | undefined;
    
	constructor(props: ImageLinkInputProps) {
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

	componentDidUpdate(prevProps: ImageLinkInputProps, prevState: ImageLinkInputState) {
		if (prevState.inputText !== this.state.inputText) {
	    	this.onChangeLink$.next(this.state.inputText);
		}
	}
    
	onChangeInputText = (e: React.SyntheticEvent<HTMLInputElement>) => {
		this.setState({inputText: e.currentTarget.value});
	};

	onLoad = () => {
        this.props.onValidImage(this.image);
	};

	onError = () => {
		this.setState({isValid: false});
	};


    render() {
		const {
			inputText
        } = this.state;
        
        return <input
                value={inputText}
                placeholder="Paste a link here"
                onChange={this.onChangeInputText}
                name="background"
                type="text"
            />
    }
}

export default ImageLinkInput;