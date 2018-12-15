import React, { Component } from 'react';
import { connect } from 'react-redux';

import { playablePuzzlesActions } from '../../actions/playablePuzzles';
import { generatedTemplatesActions } from '../../store/generatedTemplates';
import { backgroundsActions } from '../../store/backgrounds';

import styles from './PuzzleOfTheDayPage.module.scss';

import GameContainer from '../../widgets/GameContainer';

class PuzzleOfTheDayPage extends Component {

  componentDidMount() {
    this.props.fetchPuzzlesOfTheDay();
  };

  componentDidUpdate(prevProps, prevState, snapshot) {
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
      selectedPuzzle
    } = this.props;

    return (
        <React.Fragment>
          <h1>Today's Puzzles</h1>
          <div className={styles.gameContainer}>
            <GameContainer/>
          </div>
        </React.Fragment>
    );
  }
}

const mapStateToProps = state => {
  return {
    playablePuzzles: state.playablePuzzles.playablePuzzles,
    selectedPuzzle: state.playablePuzzles.playablePuzzlesMap[state.playablePuzzles.selectedId],
  };
}

const mapDispatchToProps = dispatch => {
  return {
    selectPuzzle: id => dispatch(playablePuzzlesActions.selectPlayablePuzzle(id)),
    fetchPuzzlesOfTheDay: () => dispatch(playablePuzzlesActions.fetchPuzzlesOfTheDay()),
    selectGeneratedTemplateByLink: id => dispatch(generatedTemplatesActions.selectByLink(id)),
    selectBackgroundByLink: id => dispatch(backgroundsActions.selectByLink(id))
  }
}

const ConnectedPuzzleOfTheDayPage = connect(
  mapStateToProps,
  mapDispatchToProps
)(PuzzleOfTheDayPage);

export default ConnectedPuzzleOfTheDayPage;
