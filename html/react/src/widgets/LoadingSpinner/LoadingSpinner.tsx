import React from "react";
import styles from './LoadingSpinner.module.scss';
import puzzlePieceIcon from '../../assets/piece-outline-rounded.png';

const LoadingSpinner = () => {
    return <img width='32px' height='32px'
        src={puzzlePieceIcon}
        className={styles.loadingSpinner}
        alt='Loading spinner'
    />;
}

export default LoadingSpinner;