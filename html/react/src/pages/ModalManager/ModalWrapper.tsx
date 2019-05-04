import React, {Component} from 'react';

import styles from './ModalWrapper.module.scss'

interface ModalWrapperProps {
    onClickBackground?(): void;
}

class ModalWrapper extends Component<ModalWrapperProps> {
    render() {
        return <div className={styles.mainContainer}>
            {this.props.children}
        </div>;
    }
}

export default ModalWrapper;