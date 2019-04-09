import React, { Component } from 'react';
import { connect } from 'react-redux';

import styles from './CustomPuzzlePage.module.scss';
import { GeneratedTemplate } from '../../store/generatedTemplates';

import { StateRoot } from '../../models';
import { getGeneratedTemplatesForTemplate } from '../../store/selectors';
import { PlainLink } from '../../widgets';

interface StateProps {
  generatedTemplates: GeneratedTemplate[];
}

type CustomPuzzlePageProps = StateProps;

class CustomPuzzlePage extends Component<CustomPuzzlePageProps> {

  render() {

    return (
        <div className={styles.mainContainer}>
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
    generatedTemplates: getGeneratedTemplatesForTemplate(state)
  };
}

const ConnectedCustomPuzzlePage = connect(
  mapStateToProps
)(CustomPuzzlePage);

export default ConnectedCustomPuzzlePage;
