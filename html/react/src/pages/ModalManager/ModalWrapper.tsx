import React, {Component} from 'react';

import styles from './ModalWrapper.module.scss'
import { connect } from 'react-redux';
import { displayOptionsActions } from '../../store/displayOptions';
import { Dispatch } from 'redux';

interface DispatchProps {
    closeModal: () => void;
}

type ModalWrapperProps = DispatchProps;

class ModalWrapper extends Component<ModalWrapperProps> {
    render() {
        return <div className={styles.mainContainer}>
            <div className={styles.closeButton} onClick={this.props.closeModal}>x</div>
            <div className={styles.innerContainer}>
                {this.props.children}
            </div>
        </div>;
    }
}

const mapDispatchToProps = (dispatch: Dispatch) => ({
    closeModal: () => dispatch(displayOptionsActions.hideModal())
} as DispatchProps);

export default connect(undefined, mapDispatchToProps)(ModalWrapper);