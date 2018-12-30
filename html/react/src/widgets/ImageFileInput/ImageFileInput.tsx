import React, {Component, ChangeEvent} from 'react';
import styles from './ImageFileInput.module.scss';

class ImageFileInput extends Component {

    onChangeFile = (e: React.ChangeEvent<HTMLInputElement>) => {
        var files = e.target.files;
        console.log(files);
    }

    render() {
        return <div className={styles.mainContainer}>
            <button className={styles.inputDisplay}>
                ^
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