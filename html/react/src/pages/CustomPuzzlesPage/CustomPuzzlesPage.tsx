import React, { Component } from 'react';
import { connect, DispatchProp } from 'react-redux';

import styles from './CustomPuzzlePage.module.scss';
import { GeneratedTemplate } from '../../store/generatedTemplates';

import { StateRoot } from '../../models';
import { PlainLink } from '../../widgets';
import { CustomPuzzle } from '../../store/customPuzzle';
import PuzzleCard from '../../widgets/PuzzleCard/PuzzleCard';
import { backgroundsActions } from '../../store/backgrounds';
import { Switch, Route } from 'react-router';
import CreatePuzzlePage from '../CreatePuzzlePage/CreatePuzzlePage';

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
          <PlainLink to="/custom/new">
            <div className={styles.puzzleCard}>
              <span className={styles.plusIcon}>+</span>
              <span className={styles.description}>New Puzzle</span>
            </div>
          </PlainLink>
        </div>
}

const mapStateToProps = (state: StateRoot): StateProps => {
  return {
    customPuzzles: Object.values(state.customPuzzle.puzzleMap)
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
