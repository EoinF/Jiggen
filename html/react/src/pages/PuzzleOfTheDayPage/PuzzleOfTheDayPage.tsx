import React, { Component } from 'react';
import { connect } from 'react-redux';

import { playablePuzzlesActions } from '../../actions/playablePuzzles';
import { generatedTemplatesActions, GeneratedTemplate } from '../../store/generatedTemplates';
import { backgroundsActions, Background } from '../../store/backgrounds';

import styles from './PuzzleOfTheDayPage.module.scss';

import GameContainer from '../../widgets/GameContainer';
import { StateRoot } from '../../models';

interface PuzzleOfTheDayPageProps {
  selectPuzzle(id: string): void;
  fetchPuzzlesOfTheDay(): void;
  selectGeneratedTemplateByLink(link: string): void;
  selectBackgroundByLink(link: string): void;
  background: Background,
  generatedTemplate: GeneratedTemplate,
  playablePuzzles: any[];
  selectedPuzzle: any;
}

class PuzzleOfTheDayPage extends Component<PuzzleOfTheDayPageProps> {

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

const mapStateToProps = (state: StateRoot) => {
  return {
    background: state.backgrounds.resourceMap[state.backgrounds.selectedId!],
    playablePuzzles: state.playablePuzzles.playablePuzzles,
    selectedPuzzle: state.playablePuzzles.playablePuzzlesMap[state.playablePuzzles.selectedId],
  } as PuzzleOfTheDayPageProps;
}

const mapDispatchToProps = (dispatch: Function) => {
  return {
    selectPuzzle: (id: string) => dispatch(playablePuzzlesActions.selectPlayablePuzzle(id)),
    fetchPuzzlesOfTheDay: () => dispatch(playablePuzzlesActions.fetchPuzzlesOfTheDay()),
    selectGeneratedTemplateByLink: (id: string) => dispatch(generatedTemplatesActions.selectByLink(id)),
    selectBackgroundByLink: (id: string) => dispatch(backgroundsActions.selectByLink(id))
  } as PuzzleOfTheDayPageProps;
}

const ConnectedPuzzleOfTheDayPage = connect(
  mapStateToProps,
  mapDispatchToProps
)(PuzzleOfTheDayPage);

export default ConnectedPuzzleOfTheDayPage;
