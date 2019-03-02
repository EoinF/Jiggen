import React, { Component } from 'react';
import { connect, MapStateToPropsParam } from 'react-redux';

import { playablePuzzlesActions, PlayablePuzzle } from '../../store/playablePuzzles';
import { generatedTemplatesActions, GeneratedTemplate } from '../../store/generatedTemplates';
import { backgroundsActions, Background } from '../../store/backgrounds';

import styles from './PuzzleOfTheDayPage.module.scss';

import GameContainer from '../../widgets/GameContainer';
import { StateRoot, StringMap } from '../../models';
import PieceCountSelection from './PieceCountSelection/PieceCountSelection';
import { getPlayablePuzzleList, getPieceCountMap } from '../../store/selectors';

interface DispatchProps {
  selectPuzzle(id: string): void;
  selectBackground(link: string): void;
  selectGeneratedTemplate(link: string): void;
  fetchPuzzlesOfTheDay(): void;
  fetchGeneratedTemplateByLink(link: string): void;
  fetchBackgroundByLink(link: string): void;
}

interface StateProps {
  playablePuzzles: PlayablePuzzle[];
  selectedPuzzle: PlayablePuzzle;
  pieceCountMap: StringMap<number>;
}

type PuzzleOfTheDayPageProps = DispatchProps & StateProps;

class PuzzleOfTheDayPage extends Component<PuzzleOfTheDayPageProps, any> {

  componentDidMount() {
    this.props.fetchPuzzlesOfTheDay();
  };

  componentDidUpdate(prevProps: PuzzleOfTheDayPageProps) {
    if ((this.props.playablePuzzles !== prevProps.playablePuzzles)
      && this.props.playablePuzzles != null 
      && this.props.playablePuzzles.length > 0) {
      this.props.selectPuzzle(this.props.playablePuzzles[0].id);

      this.props.playablePuzzles.forEach((playablePuzzle) => {
        this.props.fetchBackgroundByLink(playablePuzzle.links.background);
        this.props.fetchGeneratedTemplateByLink(playablePuzzle.links.generatedTemplate);
      });
    }
    if (this.props.selectedPuzzle != prevProps.selectedPuzzle) {
      const selectedPuzzle = this.props.selectedPuzzle;
      this.props.selectBackground(selectedPuzzle.links.background);
      this.props.selectGeneratedTemplate(selectedPuzzle.links.generatedTemplate);
    }
  };

  onSelectPieceCount = (id: string) => {
    this.props.selectPuzzle(id);
  }

  render() {
    const {
      selectedPuzzle
    } = this.props;

    return (
        <div className={styles.mainContainer}>
          <h1>Today's Puzzles</h1>
          <div className={styles.contentContainer}>
            <div className={styles.gameContainer}>
              <GameContainer/>
            </div>
            <PieceCountSelection
              selectedId={selectedPuzzle ? selectedPuzzle.id : null}
              pieceCountMap={this.props.pieceCountMap} 
              onClick={this.onSelectPieceCount} />
          </div>
        </div>
    );
  }
}

function mapStateToProps(_state: any, ownProps: any): StateProps {
  const state = (_state as StateRoot); // Required because we can't change type of _state

  return {
    pieceCountMap: getPieceCountMap(state),
    playablePuzzles: getPlayablePuzzleList(state),
    selectedPuzzle: state.playablePuzzles.resourceMap[state.playablePuzzles.selectedId!]
  };
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
    selectPuzzle: (id: string) => dispatch(playablePuzzlesActions.selectPlayablePuzzle(id)),
    selectBackground: (link: string) => dispatch(backgroundsActions.selectByLink(link)),
    selectGeneratedTemplate: (link: string) => dispatch(generatedTemplatesActions.selectByLink(link)),
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
