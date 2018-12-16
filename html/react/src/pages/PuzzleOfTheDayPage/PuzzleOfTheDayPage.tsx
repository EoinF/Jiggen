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

  componentDidUpdate(prevProps: PuzzleOfTheDayPageProps, prevState: any) {
    if ((this.props.playablePuzzles !== prevProps.playablePuzzles) && this.props.playablePuzzles != null && this.props.playablePuzzles.length > 0) {
      this.props.selectPuzzle(this.props.playablePuzzles[0].id);

      this.props.playablePuzzles.forEach((playablePuzzle) => {
        if (playablePuzzle.background == null) { 
          this.props.fetchBackgroundByLink(playablePuzzle.links.background);
        } else if (playablePuzzle.generatedTemplate == null) {
          this.props.fetchGeneratedTemplateByLink(playablePuzzle.links.generatedTemplate);
        }
      });
    }
  };

  onClickPiece(id: string) {
    console.log('selected '+ id);
  }

  render() {
    const {
      selectedPuzzle
    } = this.props;

    return (
        <React.Fragment>
          <h1>Today's Puzzles</h1>
          <div className={styles.gameContainer}>
          { selectedPuzzle &&
            <GameContainer background={selectedPuzzle.background} generatedTemplate={selectedPuzzle.generatedTemplate}/>
          }
            </div>
          { 
            <PieceCountSelection 
              pieceCountMap={this.props.pieceCountMap} 
              onClick={this.onClickPiece} />
          }
        </React.Fragment>
    );
  }
}


function mapStateToProps(_state: any, ownProps: any): StateProps {
  const state = (_state as StateRoot); // Required because we can't change type of _state
  return {
    pieceCountMap: getPieceCountMap(state),
    playablePuzzles: getPlayablePuzzleList(state),
    selectedPuzzle: state.playablePuzzles.resourceMap[state.playablePuzzles.selectedId!],
  };
}

const mapDispatchToProps = (dispatch: Function) => {
  return {
    selectPuzzle: (id: string) => dispatch(playablePuzzlesActions.selectPlayablePuzzle(id)),
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
