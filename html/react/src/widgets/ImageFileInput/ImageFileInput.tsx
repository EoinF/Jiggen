import React, {Component, ChangeEvent} from 'react';
import styles from './ImageFileInput.module.scss';

interface ImageFileInputProps {
    onValidImage(image: HTMLImageElement): void;
}

class ImageFileInput extends Component<ImageFileInputProps> {

    onChangeFile = (e: React.ChangeEvent<HTMLInputElement>) => {
        const files = e.target.files;

        const image = new Image();
        if (files != null && files.length === 1) {
            image.src = URL.createObjectURL(files[0]);
            image.onload = () => {
                this.props.onValidImage(image)
            }
        }
    }

    render() {
        return <div className={styles.mainContainer}>
            <button className={styles.inputDisplayButton}>
                {this.props.children}
            </button>
            <input className={styles.hiddenInput}
                type="file" 
                accept="image/*" 
                onChange={this.onChangeFile}
            />
        </div>
    }
}

export default ImageFileInput;