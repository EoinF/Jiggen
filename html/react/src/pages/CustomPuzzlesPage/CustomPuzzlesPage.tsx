import React, { Component } from 'react';
import { connect } from 'react-redux';

import styles from './CustomPuzzlePage.module.scss';

import { StateRoot } from '../../models';
import { PlainLink } from '../../widgets';
import { CustomPuzzle } from '../../store/customPuzzle';
import PuzzleCard from '../../widgets/PuzzleCard/PuzzleCard';
import { backgroundsActions } from '../../store/backgrounds';
import { Switch, Route } from 'react-router';
import CreatePuzzlePage from '../CreatePuzzlePage/CreatePuzzlePage';
import { templatesActions } from '../../store/templates';

interface StateProps {
  customPuzzles: CustomPuzzle[];
}

interface DispatchProps {
  fetchBackgrounds(): void;
  fetchTemplates(): void;
}

type CustomPuzzlePageProps = StateProps & DispatchProps;

class CustomPuzzlePage extends Component<CustomPuzzlePageProps> {
  render() {
    return <Switch>
      <Route path='/custom/:id' component={CreatePuzzlePage} />
      <Route render={() => <CustomPuzzles customPuzzles={this.props.customPuzzles}/>} />
    </Switch>
  }
}

type CustomPuzzlesProps = StateProps; 

const CustomPuzzles = ({customPuzzles}: CustomPuzzlesProps) => {
  return <div className={styles.mainContainer}>
          { customPuzzles.map(
            (customPuzzle: CustomPuzzle) => 
              <PuzzleCard key={customPuzzle.id} puzzle={customPuzzle}/>
            )
          }
          <div className={styles.puzzleCardContainer}>
            <PlainLink to="/custom/new">
              <div className={styles.puzzleCardContent}>
                <span className={styles.plusIcon}>+</span>
                <span className={styles.description}>New Puzzle</span>
              </div>
            </PlainLink>
          </div>
        </div>
}

const mapStateToProps = (state: StateRoot): StateProps => {
  return {
    customPuzzles: Object.values(state.customPuzzle.puzzleMap)
  };
}

const mapDispatchToProps = (dispatch: Function) : DispatchProps => {
  return {
    fetchBackgrounds: () => dispatch(backgroundsActions.fetchBackgrounds()),
    fetchTemplates: () => dispatch(templatesActions.fetchTemplates())
  };
}

const ConnectedCustomPuzzlePage = connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomPuzzlePage);

export default ConnectedCustomPuzzlePage;
