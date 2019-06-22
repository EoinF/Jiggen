import React, { Component } from 'react';
import { CustomPuzzle, customPuzzleActions } from "../../store/customPuzzle";
import { connect } from "react-redux";
import { Template } from "../../store/templates";
import { Background } from "../../store/backgrounds";
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
    template: Template | null;
    background: Background | null;
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
        if (this.props.puzzle.isDownloaded && this.props.puzzle.isDownloaded !== prevProps.puzzle.isDownloaded) {
            this.setState({isDownloadComplete: true});
        }
    }
    
    PlayButton = () => {
        const {
            puzzle, playCustomPuzzle
        } = this.props;
        
        const templateLink = encodeURIComponent(puzzle.template!);
        let playPuzzleLink: string;
        if (puzzle.background!.isCustom) {
            const backgroundLink = encodeURIComponent(puzzle.background!.links.image);
            playPuzzleLink = `/play?template=${templateLink}&backgroundImage=${backgroundLink}`;
        } else {
            const backgroundLink = encodeURIComponent(puzzle.background!.links.self);
            playPuzzleLink = `/play?template=${templateLink}&background=${backgroundLink}`;
        }

        return <PlainLink to={playPuzzleLink}>
            <div className={[styles.iconSmall, styles.playIcon].join(" ")} onClick={playCustomPuzzle}>
                <img className={styles.iconImage} src={playIconSrc} alt="play"/>
            </div>
        </PlainLink>;
    }

    render() {
        const {
            background, template, puzzle, deleteCustomPuzzle
        } = this.props;

        return <div className={styles.mainContainer}>
            <div className={styles.cardTitleContainer}>
                <div className={styles.cardTitle}>
                    {puzzle.name}
                </div>
                <div className={styles.cardTitleBackground}/>
            </div>
            <div className={styles.mainContent}>
                <div className={styles.puzzleCardGroup}>
                    <BackgroundIcon background={background} />
                    <TemplateIcon template={template} />
                </div>
                <div className={styles.puzzleCardGroup}>
                    <this.PlayButton/>
                    <PlainLink to={`/custom/${puzzle.id}`}>
                        <div className={[styles.iconSmall, styles.editIcon].join(" ")}>
                            <img className={styles.iconImage} src={editIconSrc} alt="edit" />
                        </div>
                    </PlainLink>
                    <div className={[styles.iconSmall, styles.deleteIcon].join(" ")} onClick={deleteCustomPuzzle}>
                        <img className={styles.iconImage} src={deleteIconSrc} alt="delete" />
                    </div>
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
        template: ownProps.puzzle.template != null ? state.templates.linkMap[ownProps.puzzle.template]: null,
        background: ownProps.puzzle.background && state.backgrounds.linkMap[ownProps.puzzle.background.links.self]
    };
}

const mapDispatchToProps = (dispatch: Function, ownProps: OwnProps) : DispatchProps => {
    return {
        deleteCustomPuzzle: () => dispatch(customPuzzleActions.deletePuzzle(ownProps.puzzle)),
        playCustomPuzzle: () => {
            dispatch(puzzleSolverActions.selectAndDownloadBackground(
                    ownProps.puzzle.background!.links.self,
                    ownProps.puzzle.background!.isCustom
                ));
            dispatch(puzzleSolverActions.selectAndDownloadTemplate(ownProps.puzzle.template!));
        }
    };
}

export default connect<StateProps, DispatchProps, OwnProps>(mapStateToProps, mapDispatchToProps)(PuzzleCard);