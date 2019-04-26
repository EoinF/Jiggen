import React, { Component } from 'react';
import { CustomPuzzle, customPuzzleActions } from "../../store/customPuzzle";
import { connect } from "react-redux";
import { Template, templatesActions } from "../../store/templates";
import { Background, backgroundsActions } from "../../store/backgrounds";
import templateIconSrc from './template-icon48x48.png';
import playIconSrc from '../../assets/play-icon.png';
import deleteIconSrc from '../../assets/delete-icon.png';
import editIconSrc from '../../assets/edit-icon.png';

import styles from './PuzzleCard.module.scss';
import { puzzleSolverActions } from '../../store/puzzleSolverScreen';
import { PlainLink } from '..';
import PieceCountDisplay from '../PieceCountDisplay/PieceCountDisplay';
import { StateRoot } from '../../models';
import BackgroundIcon from './BackgroundIcon/BackgroundIcon';

interface OwnProps {
    puzzle: CustomPuzzle;
}

interface StateProps {
    template: Template;
    background: Background;
}

interface DispatchProps {
    deleteCustomPuzzle(): void;
    playCustomPuzzle(): void;
    preloadCustomPuzzle(): void;
}

type PuzzleCardProps = OwnProps & StateProps & DispatchProps;

class PuzzleCard extends Component<PuzzleCardProps> {
    componentDidMount() {
        this.props.preloadCustomPuzzle();
    }

    render() { 
        const {
            background, template, puzzle,
            deleteCustomPuzzle, playCustomPuzzle
        } = this.props;

        return <div className={styles.mainContainer}>
            <div className={styles.cardTitleContainer}>
                <div className={styles.cardTitle}>
                    {puzzle.name}
                </div>
                <div className={styles.cardTitleBackground}/>
            </div>
            <div className={styles.mainContent}>
                <BackgroundIcon background={background} />
                <div className={`${styles.icon} ${styles.templateSelectionContainer}`}>
                    <img src={templateIconSrc}/>
                    {
                        template != null 
                        && <PieceCountDisplay count={template.pieces}/> 
                    }
                </div>
                <PlainLink to={`/play`}>
                    <div className={styles.iconSmall} onClick={playCustomPuzzle}>
                        <img src={playIconSrc}/>
                    </div>
                </PlainLink>
                <PlainLink to={`/custom/${puzzle.id}`}>
                    <div className={styles.iconSmall}>
                        <img src={editIconSrc}/>
                    </div>
                </PlainLink>
                <div className={styles.iconSmall} onClick={deleteCustomPuzzle}>
                    <img src={deleteIconSrc}/>
                </div>
            </div>
        </div>;
    }
}

const mapStateToProps = (_state: any, ownProps: OwnProps) : StateProps => {
    const state = _state as StateRoot;
    return {
        template: state.templates.linkMap[ownProps.puzzle.template!],
        background: state.backgrounds.linkMap[ownProps.puzzle.background!]
    };
}

const mapDispatchToProps = (dispatch: Function, ownProps: OwnProps) : DispatchProps => {
    return {
        deleteCustomPuzzle: () => dispatch(customPuzzleActions.deletePuzzle(ownProps.puzzle)),
        preloadCustomPuzzle: () => dispatch(customPuzzleActions.savePuzzle(ownProps.puzzle)),
        playCustomPuzzle: () => {
            dispatch(puzzleSolverActions.selectAndDownloadBackground(ownProps.puzzle.background!));
            dispatch(puzzleSolverActions.selectAndDownloadTemplate(ownProps.puzzle.template!));
        }
    };
}

export default connect<StateProps, DispatchProps, OwnProps>(mapStateToProps, mapDispatchToProps)(PuzzleCard);