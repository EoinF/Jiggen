import * as React from 'react';
import styles from './PieceCountSelection.module.scss';
import { GeneratedTemplate } from '../../../store/generatedTemplates';
import { StringMap } from '../../../models';

interface PieceCountSelectionProps {
    pieceCountMap: StringMap<number>;
    onClick(key: string): void;
}

const PieceCountSelection = ({pieceCountMap, onClick}: PieceCountSelectionProps) => {
    console.log(pieceCountMap);
    return <div>
        { Object.keys(pieceCountMap).map(key => 
            <PieceCountItem
                count={pieceCountMap[key]}
                onClick={() => onClick(key)} />
            )
        }
    </div>
};

const PieceCountItem = ({count, onClick}: any) => {
    return <span 
        className={styles.pieceCountItem}
        onClick={onClick}
    >{count}</span>
}

export default PieceCountSelection;