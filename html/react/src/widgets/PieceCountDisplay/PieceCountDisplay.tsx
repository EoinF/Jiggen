import React from 'react';
import styles from './PieceCountDisplay.module.scss';

interface PieceCountDisplayProps {
    count: number;
}

const PieceCountDisplay = ({count}: PieceCountDisplayProps) => (
    <div className={styles.pieceCount}>{count}</div>
)

export default PieceCountDisplay;