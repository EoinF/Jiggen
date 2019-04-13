import React from 'react';
import { CustomPuzzle, customPuzzleActions } from "../../store/customPuzzle";
import { connect } from "react-redux";
import { Template } from "../../store/templates";
import { Background } from "../../store/backgrounds";
import templateIconSrc from './template-icon48x48.png';
import playIconSrc from '../../assets/play-icon.png';
import deleteIconSrc from '../../assets/delete-icon.png';
import downloadIconSrc from '../../assets/download-icon.png';

import styles from './PuzzleCard.module.scss';
import { puzzleSolverActions } from '../../store/puzzleSolverScreen';

interface OwnProps {
    puzzle: CustomPuzzle;
}

interface StateProps {
    template: Template;
    background: Background;
}

interface DispatchProps {
    deleteCustomPuzzle(): void;
}

type PuzzleCardProps = OwnProps & StateProps & DispatchProps;

const PuzzleCard = ({background, puzzle, deleteCustomPuzzle} : PuzzleCardProps) => {
    let backgroundSrc = '';
    if (background != null) {
        backgroundSrc = background.links['image-thumbnail48x48']
            || background.links['image-compressed']
            || background.links['image'];
    }

    return <div className={styles.mainContainer}>
        <div className={styles.cardTitleContainer}>
            <div className={styles.cardTitle}>
                {puzzle.name}
            </div>
            <div className={styles.cardTitleBackground}/>
        </div>
        <div className={styles.mainContent}>
            <div className={styles.icon}>
                <img src={backgroundSrc} />
            </div>
            <div className={styles.icon}>
                <img src={templateIconSrc}/>
            </div>
            <div className={styles.iconSmall}>
                <img src={playIconSrc}/>
            </div>
            <div className={styles.iconSmall}>
                <img src={downloadIconSrc}/>
            </div>
            <div className={styles.iconSmall} onClick={deleteCustomPuzzle}>
                <img src={deleteIconSrc}/>
            </div>
        </div>
    </div> 
}

const mapStateToProps = (state: any, ownProps: OwnProps) : StateProps => {
    return {
        template: state.templates.linkMap[ownProps.puzzle.template],
        background: state.backgrounds.linkMap[ownProps.puzzle.background]
    };
}

const mapDispatchToProps = (dispatch: Function, ownProps: OwnProps) : DispatchProps => {
    return {
        deleteCustomPuzzle: () => dispatch(customPuzzleActions.deletePuzzle(ownProps.puzzle))
    };
}

export default connect<StateProps, DispatchProps, OwnProps>(mapStateToProps, mapDispatchToProps)(PuzzleCard);