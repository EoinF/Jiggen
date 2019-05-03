import React, { Component } from 'react';
import { connect } from 'react-redux';

import { playablePuzzlesActions, PlayablePuzzle } from '../../store/playablePuzzles';
import { Background, backgroundsActions } from '../../store/backgrounds';

import styles from './PuzzleOfTheDayPage.module.scss';

import playIconSrc from '../../assets/play-icon.png';
import { StateRoot, StringMap } from '../../models';
import PieceCountSelection from './PieceCountSelection/PieceCountSelection';
import { puzzleSolverActions } from '../../store/puzzleSolverScreen';
import { PlainLink } from '../../widgets';
import { templatesActions } from '../../store/templates';
import { getPlayablePuzzles } from '../../store/selectors';

import puzzlePieceIcon from '../../assets/piece-outline-rounded.png';
import LoadingSpinner from '../../widgets/LoadingSpinner/LoadingSpinner';

interface DispatchProps {
  selectPuzzle(id: string): void;
  selectBackground(link: string): void;
  selectTemplate(link: string): void;
  fetchPuzzlesOfTheDay(): void;
  fetchTemplateByLink(link: string): void;
  fetchBackgroundByLink(link: string): void;
}

interface StateProps {
  puzzles: PlayablePuzzle[];
  selectedPuzzle: PlayablePuzzle;
  selectedBackground: Background;
}

type PuzzleOfTheDayPageProps = DispatchProps & StateProps;

class PuzzleOfTheDayPage extends Component<PuzzleOfTheDayPageProps, any> {

  componentDidMount() {
    this.props.fetchPuzzlesOfTheDay();
  };

  componentDidUpdate(prevProps: PuzzleOfTheDayPageProps) {
    if ((this.props.puzzles !== prevProps.puzzles)
      && this.props.puzzles != null) {
        if (this.props.puzzles.length > 0) {
        this.props.selectPuzzle(this.props.puzzles[0].links.self);

        this.props.puzzles.forEach(puzzle => {
          this.props.fetchBackgroundByLink(puzzle.links.background);
          this.props.fetchTemplateByLink(puzzle.links.template);
        })
      }
    }
    if (this.props.selectedPuzzle != prevProps.selectedPuzzle) {
      const {background, template} = this.props.selectedPuzzle.links;
      this.props.selectBackground(background);
      this.props.selectTemplate(template);
    }
  };

  onSelectPieceCount = (link: string) => {
    this.props.selectPuzzle(link);
  }

  render() {
    const {
      puzzles,
      selectedPuzzle,
      selectedBackground
    } = this.props;

    return (
        <div className={styles.mainContainer}>
          <h1>Today's Puzzles</h1>
          <div className={styles.contentContainer}>
            <div className={styles.backgroundContainer}>
              { 
                selectedBackground ? <img className={styles.backgroundImage} src={selectedBackground.links['image-compressed'] || selectedBackground.links.image} /> :
                  <div className={styles.loadingContainer}>
                    <h1>Loading daily puzzles...</h1>
                    <LoadingSpinner/>
                  </div>
              }
            </div>

            <div className={styles.pieceCountContainer}>
              { puzzles.map(puzzle => 
              <PieceCountSelection
                isSelected={ selectedPuzzle == puzzle}
                puzzle={puzzle}
                onClick={() => this.onSelectPieceCount(puzzle.links.self)} />
              )}
            </div>
            <div className={styles.playButtonContainer}>
            { selectedBackground && <PlainLink to="/play">
                <div className={styles.playIcon}>
                  <img src={playIconSrc} />
                  <span>Play</span>
                </div>
              </PlainLink>
            }
            </div>
          </div>
        </div>
    );
  }
}

function mapStateToProps(_state: any): StateProps {
  const state = (_state as StateRoot); // Required because we can't change type of _state

  const selectedPuzzle = state.playablePuzzles.linkMap[state.playablePuzzles.selectedId!];

  return {
    puzzles: getPlayablePuzzles(state),
    selectedPuzzle: selectedPuzzle,
    selectedBackground: selectedPuzzle && state.backgrounds.linkMap[selectedPuzzle.links.background!]
  };
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
    selectPuzzle: (link: string) => dispatch(playablePuzzlesActions.selectPlayablePuzzle(link)),
    selectBackground: (link: string) => dispatch(puzzleSolverActions.selectAndDownloadBackground(link)),
    selectTemplate: (link: string) => dispatch(puzzleSolverActions.selectAndDownloadTemplate(link)),
    fetchPuzzlesOfTheDay: () => dispatch(playablePuzzlesActions.fetchPuzzlesOfTheDay()),
    fetchBackgroundByLink: (link: string) => dispatch(backgroundsActions.fetchByLink(link)),
    fetchTemplateByLink: (link: string) => dispatch(templatesActions.fetchByLink(link))
  };
}

const ConnectedPuzzleOfTheDayPage = connect<StateProps, DispatchProps, any> (
  mapStateToProps,
  mapDispatchToProps
)(PuzzleOfTheDayPage);

export default ConnectedPuzzleOfTheDayPage;
