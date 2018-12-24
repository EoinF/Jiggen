import * as React from 'react';
import styles from './PieceCountSelection.module.scss';
import { GeneratedTemplate } from '../../../store/generatedTemplates';
import { StringMap } from '../../../models';
import pieceBackground from './piece-outline.png';

interface PieceCountSelectionProps {
    selectedId: string | null;
    pieceCountMap: StringMap<number>;
    onClick(key: string): void;
}

const PieceCountSelection = ({selectedId, pieceCountMap, onClick}: PieceCountSelectionProps) => {
    return <div className={styles.pieceCountSelectionContainer}>
        { Object.keys(pieceCountMap).map(key =>
            <PieceCountItem
                isSelected={key == selectedId}
                count={pieceCountMap[key]}
                onClick={() => onClick(key)} />
            )
        }
    </div>
};

const PieceCountItem = ({isSelected, count, onClick}: any) => {
    let className = styles.pieceCountItem;
    if (isSelected) {
        className += ` ${styles.selectedItem}`;
    }
    return <div
        className={className}
        onClick={onClick}
    >
        <img src={pieceBackground}/>
        <div>{count}</div>
    </div>
}

export default PieceCountSelection;