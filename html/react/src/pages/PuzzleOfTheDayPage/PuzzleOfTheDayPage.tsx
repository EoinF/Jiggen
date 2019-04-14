import React, { Component } from 'react';
import { connect, MapStateToPropsParam } from 'react-redux';

import { playablePuzzlesActions, PlayablePuzzle } from '../../store/playablePuzzles';
import { generatedTemplatesActions, GeneratedTemplate } from '../../store/generatedTemplates';
import { backgroundsActions, Background } from '../../store/backgrounds';

import styles from './PuzzleOfTheDayPage.module.scss';

import GameContainer from '../../widgets/GameContainer';
import { StateRoot, StringMap } from '../../models';
import PieceCountSelection from './PieceCountSelection/PieceCountSelection';
import { getPlayablePuzzleMap, getPieceCountMap } from '../../store/selectors';
import { puzzleSolverActions } from '../../store/puzzleSolverScreen';

interface DispatchProps {
  selectPuzzle(id: string): void;
  selectBackground(link: string): void;
  selectTemplate(link: string): void;
  fetchPuzzlesOfTheDay(): void;
  fetchGeneratedTemplateByLink(link: string): void;
  fetchBackgroundByLink(link: string): void;
}

interface StateProps {
  playablePuzzleMap: StringMap<PlayablePuzzle>;
  selectedPuzzle: PlayablePuzzle;
  selectedBackground: Background;
  pieceCountMap: StringMap<number>;
}

type PuzzleOfTheDayPageProps = DispatchProps & StateProps;

class PuzzleOfTheDayPage extends Component<PuzzleOfTheDayPageProps, any> {

  componentDidMount() {
    this.props.fetchPuzzlesOfTheDay();
  };

  componentDidUpdate(prevProps: PuzzleOfTheDayPageProps) {
    if ((this.props.playablePuzzleMap !== prevProps.playablePuzzleMap)
      && this.props.playablePuzzleMap != null) {
      const playablePuzzleList = Object.values(this.props.playablePuzzleMap);
        if (playablePuzzleList.length > 0) {
        this.props.selectPuzzle(playablePuzzleList[0].links.self);

        playablePuzzleList.forEach((playablePuzzle) => {
          this.props.fetchBackgroundByLink(playablePuzzle.links.background);
          this.props.fetchGeneratedTemplateByLink(playablePuzzle.links.generatedTemplate);
        });
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
      selectedPuzzle,
      selectedBackground
    } = this.props;

    return (
        <div className={styles.mainContainer}>
          <h1>Today's Puzzles</h1>
          <div className={styles.contentContainer}>
            <div className={styles.backgroundContainer}>
              { 
                selectedBackground && <img src={selectedBackground.links['image-compressed'] || selectedBackground.links.image} />
              }
            </div>
            <PieceCountSelection
              selectedId={selectedPuzzle ? selectedPuzzle.links.self : null}
              pieceCountMap={this.props.pieceCountMap}
              onClick={this.onSelectPieceCount} />
          </div>
        </div>
    );
  }
}

function mapStateToProps(_state: any): StateProps {
  const state = (_state as StateRoot); // Required because we can't change type of _state

  const selectedPuzzle = state.playablePuzzles.linkMap[state.playablePuzzles.selectedId!];
  return {
    pieceCountMap: getPieceCountMap(state),
    playablePuzzleMap: getPlayablePuzzleMap(state),
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
    fetchGeneratedTemplateByLink: (link: string) => dispatch(generatedTemplatesActions.fetchByLink(link)),
    fetchBackgroundByLink: (link: string) => dispatch(backgroundsActions.fetchByLink(link))
  };
}

const ConnectedPuzzleOfTheDayPage = connect<StateProps, DispatchProps, any> (
  mapStateToProps,
  mapDispatchToProps
)(PuzzleOfTheDayPage);

export default ConnectedPuzzleOfTheDayPage;
