import React, { Component } from 'react';
import { connect, DispatchProp } from 'react-redux';

import styles from './CustomPuzzlePage.module.scss';
import { GeneratedTemplate } from '../../store/generatedTemplates';

import { StateRoot } from '../../models';
import { PlainLink } from '../../widgets';
import { CustomPuzzle } from '../../store/customPuzzle';
import PuzzleCard from '../../widgets/PuzzleCard/PuzzleCard';
import { backgroundsActions } from '../../store/backgrounds';

interface StateProps {
  customPuzzles: CustomPuzzle[];
}

interface DispatchProps {
  fetchBackgrounds(): void;
}

type CustomPuzzlePageProps = StateProps & DispatchProps;

class CustomPuzzlePage extends Component<CustomPuzzlePageProps> {

  componentDidMount() {
    this.props.fetchBackgrounds();
  }

  render() {
    return (
        <div className={styles.mainContainer}>
          { this.props.customPuzzles.map(
            (customPuzzle: CustomPuzzle) => 
              <PuzzleCard puzzle={customPuzzle}/>
            )
          }
          <PlainLink to="/custom/new">
            <div className={styles.puzzleCard}>
              <span className={styles.plusIcon}>+</span>
              <span className={styles.description}>New Puzzle</span>
            </div>
          </PlainLink>
        </div>
    );
  }
}

const mapStateToProps = (state: StateRoot): StateProps => {
  return {
    customPuzzles: state.customPuzzle.puzzleList
  };
}

const mapDispatchToProps = (dispatch: Function) : DispatchProps => {
  return {
    fetchBackgrounds: () => dispatch(backgroundsActions.fetchBackgrounds())
  };
}

const ConnectedCustomPuzzlePage = connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomPuzzlePage);

export default ConnectedCustomPuzzlePage;
