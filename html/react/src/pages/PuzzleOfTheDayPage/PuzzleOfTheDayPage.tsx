import React, { Component } from 'react';
import { connect, MapStateToPropsParam } from 'react-redux';

import { playablePuzzlesActions } from '../../actions/playablePuzzles';
import { generatedTemplatesActions, GeneratedTemplate } from '../../store/generatedTemplates';
import { backgroundsActions, Background } from '../../store/backgrounds';

import styles from './PuzzleOfTheDayPage.module.scss';

import GameContainer from '../../widgets/GameContainer';
import { StateRoot } from '../../models';

interface DispatchProps {
  selectPuzzle(id: string): void;
  fetchPuzzlesOfTheDay(): void;
  selectGeneratedTemplateByLink(link: string): void;
  selectBackgroundByLink(link: string): void;
}

interface StateProps {
  background: Background,
  generatedTemplate: GeneratedTemplate,
  playablePuzzles: any[];
  selectedPuzzle: any;
}

type PuzzleOfTheDayPageProps = DispatchProps & StateProps;


class PuzzleOfTheDayPage extends Component<PuzzleOfTheDayPageProps, any> {

  componentDidMount() {
    this.props.fetchPuzzlesOfTheDay();
  };

  componentDidUpdate(prevProps: PuzzleOfTheDayPageProps, prevState: any) {
    if (this.props.playablePuzzles !== prevProps.playablePuzzles && this.props.playablePuzzles != null) {
      this.props.selectPuzzle(this.props.playablePuzzles[0].id);
    }

    if (this.props.selectedPuzzle !== prevProps.selectedPuzzle) {
      this.props.selectGeneratedTemplateByLink(this.props.selectedPuzzle.links.generatedTemplate);
      this.props.selectBackgroundByLink(this.props.selectedPuzzle.links.background);
    }
  };

  render() {
    const {
      selectedPuzzle,
      background,
      generatedTemplate,
    } = this.props;

    return (
        <React.Fragment>
          <h1>Today's Puzzles</h1>
          <div className={styles.gameContainer}>
            <GameContainer background={background} generatedTemplate={generatedTemplate}/>
          </div>
        </React.Fragment>
    );
  }
}

function mapStateToProps(_state: any, ownProps: any): StateProps {
  const state = (_state as StateRoot); // Required because we can't change type of _state
  return {
    background: state.backgrounds.resourceMap[state.backgrounds.selectedId!],
    generatedTemplate: state.generatedTemplates.resourceMap[state.generatedTemplates.selectedId],
    playablePuzzles: state.playablePuzzles.playablePuzzles,
    selectedPuzzle: state.playablePuzzles.playablePuzzlesMap[state.playablePuzzles.selectedId],
  };
}

const mapDispatchToProps = (dispatch: Function) => {
  return {
    selectPuzzle: (id: string) => dispatch(playablePuzzlesActions.selectPlayablePuzzle(id)),
    fetchPuzzlesOfTheDay: () => dispatch(playablePuzzlesActions.fetchPuzzlesOfTheDay()),
    selectGeneratedTemplateByLink: (id: string) => dispatch(generatedTemplatesActions.selectByLink(id)),
    selectBackgroundByLink: (id: string) => dispatch(backgroundsActions.selectByLink(id))
  };
}

const ConnectedPuzzleOfTheDayPage = connect<StateProps, DispatchProps, any> (
  mapStateToProps,
  mapDispatchToProps
)(PuzzleOfTheDayPage);

export default ConnectedPuzzleOfTheDayPage;
