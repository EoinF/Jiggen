import * as React from 'react';
import styles from './PieceCountSelection.module.scss';
import { StateRoot } from '../../../models';
import pieceBackground from './piece-outline.png';
import { PlayablePuzzle } from '../../../store/playablePuzzles';
import { connect } from 'react-redux';

interface StateProps {
    count: number;
}

interface OwnProps {
    isSelected: boolean;
    puzzle: PlayablePuzzle;
    onClick(): void;
}

type PieceCountSelectionProps = OwnProps & StateProps;

const PieceCountSelection = ({isSelected, count, onClick}: PieceCountSelectionProps) => {
    let className = styles.pieceCountItem;
    if (isSelected) {
        className += ` ${styles.selectedItem}`;
    }
    return <div
        className={className}
        onClick={onClick}
    >
        <img src={pieceBackground} alt={`${count} pieces`}/>
        <div>{count}</div>
    </div>
}

const mapStateToProps = (_state: any, ownProps: OwnProps): StateProps => {
    const state = _state as StateRoot;

    const template = state.templates.linkMap[ownProps.puzzle.links.template!];

    return {
        count: template && template.pieces
    }
}

export default connect(mapStateToProps)(PieceCountSelection);