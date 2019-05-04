import React from 'react';
import ModalWrapper from '../../pages/ModalManager/ModalWrapper';
import styles from './PuzzleCompleteModal.module.scss';
import PlainLink from '../PlainLink';
import { Dispatch } from 'redux';
import { displayOptionsActions } from '../../store/displayOptions';
import { connect } from 'react-redux';

interface DispatchProps {
    hideModal(): void;
}


const PuzzleCompleteModal = ({hideModal}: DispatchProps) => {
    return <ModalWrapper>
        <div className={styles.mainContainer}>
            <div className={styles.modalTitleContainer}>
                <h1 className={styles.modalTitle}>Puzzle complete!</h1>
            </div>
            <div className={styles.buttonControls}>
                <PlainLink to="/custom">
                    <div className={styles.button} onClick={hideModal}>More puzzles</div>
                </PlainLink>
                <div className={styles.button} onClick={hideModal}>Back</div>
            </div>
        </div>
    </ModalWrapper>
}

const mapDispatchToProps = (dispatch: Dispatch) => ({
    hideModal: () => dispatch(displayOptionsActions.hideModal())
});

export default connect(undefined, mapDispatchToProps)(PuzzleCompleteModal);