import React, { Component } from 'react';
import { CustomPuzzle, customPuzzleActions } from "../../store/customPuzzle";
import { connect } from "react-redux";
import { Template, templatesActions } from "../../store/templates";
import { Background, backgroundsActions } from "../../store/backgrounds";
import playIconSrc from '../../assets/play-icon.png';
import deleteIconSrc from '../../assets/delete-icon.png';
import editIconSrc from '../../assets/edit-icon.png';

import styles from './PuzzleCard.module.scss';
import { puzzleSolverActions } from '../../store/puzzleSolverScreen';
import { PlainLink } from '..';
import { StateRoot } from '../../models';
import BackgroundIcon from './BackgroundIcon/BackgroundIcon';
import TemplateIcon from './TemplateIcon/TemplateIcon';
import PopupNotification from '../PopupNotification/PopupNotification';

interface OwnProps {
    puzzle: CustomPuzzle;
}

interface OwnState {
    isDownloadComplete: boolean;
}

interface StateProps {
    template: Template;
    background: Background;
}

interface DispatchProps {
    deleteCustomPuzzle(): void;
    playCustomPuzzle(): void;
}

type PuzzleCardProps = OwnProps & StateProps & DispatchProps;

class PuzzleCard extends Component<PuzzleCardProps, OwnState> {
    state = {
        isDownloadComplete: false
    }

    componentDidUpdate(prevProps: PuzzleCardProps) {
        if (this.props.puzzle.isDownloaded && this.props.puzzle.isDownloaded != prevProps.puzzle.isDownloaded) {
            this.setState({isDownloadComplete: true});
        }
    }
    
    PlayButton = () => {
        const {
            puzzle, playCustomPuzzle
        } = this.props;
        
        const templateLink = encodeURIComponent(puzzle.template!);
        const backgroundLink = encodeURIComponent(puzzle.background!);
        const playPuzzleLink = `/play?template=${templateLink}&background=${backgroundLink}`;
        return <PlainLink to={playPuzzleLink}>
            <div className={styles.iconSmall} onClick={playCustomPuzzle}>
                <img className={styles.iconImage} src={playIconSrc}/>
            </div>
        </PlainLink>;
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
                <TemplateIcon template={template} />
                <this.PlayButton/>
                <PlainLink to={`/custom/${puzzle.id}`}>
                    <div className={styles.iconSmall}>
                        <img className={styles.iconImage} src={editIconSrc}/>
                    </div>
                </PlainLink>
                <div className={styles.iconSmall} onClick={deleteCustomPuzzle}>
                    <img src={deleteIconSrc}/>
                </div>
            </div>
            {
                this.state.isDownloadComplete && (
                <PopupNotification>
                    <div>
                        <span>Puzzle </span> 
                        <span className={styles.highlightedText}>{this.props.puzzle.name}</span>
                        <span> downloaded and ready for </span> 
                        <span className={styles.highlightedText}>offline</span>
                        <span> use</span>
                    </div>
                </PopupNotification>
            )
            }
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
        playCustomPuzzle: () => {
            dispatch(puzzleSolverActions.selectAndDownloadBackground(ownProps.puzzle.background!));
            dispatch(puzzleSolverActions.selectAndDownloadTemplate(ownProps.puzzle.template!));
        }
    };
}

export default connect<StateProps, DispatchProps, OwnProps>(mapStateToProps, mapDispatchToProps)(PuzzleCard);