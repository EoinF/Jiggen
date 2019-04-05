import React, { KeyboardEventHandler } from 'react';
import { connect } from 'react-redux';
import { Subject, from, Subscription } from 'rxjs';

import {
  GeneratedTemplate,
  generatedTemplatesActions
} from '../../store/generatedTemplates';

import gwtAdapter from '../../gwtAdapter';

import {
  setFullScreen,
  onFullScreenChange,
  isFullScreen,
  unsetOnFullScreenChange
} from '../../utils/fullScreen';

import styles from './GameContainer.module.scss';
import { StateRoot } from '../../models';
import withLoadingWrapper from './withLoadingDisplay';
import { DownloadedImage } from '../../store/downloadedImages';
import { puzzleSolverActions } from '../../store/puzzleSolverScreen';

interface DispatchProps {
  setPuzzleStatus(isFreshPuzzle: Boolean): void;
}

interface StateProps {
  showFullScreenFallback: Boolean;
  shouldShuffleOnFullscreen: Boolean;
}

type GameContainerProps = StateProps & DispatchProps;

class GameContainer extends React.Component<GameContainerProps> {
  gameContainerRef: React.RefObject<any>;
  updateContainerSize$: Subject<void>;
  updateContainerSubscription: Subscription;
  onKeyDownEventListener: EventListener;

  constructor(props: GameContainerProps) {
    super(props);
    this.state = {
      shouldShuffleOnFullscreen: true
    };
    this.gameContainerRef = React.createRef();
    this.updateContainerSize$ = new Subject();

    this.updateContainerSubscription = this.updateContainerSize$.subscribe(this.updateContainerSize);
    
    onFullScreenChange(this.onFullScreenChange);
    
    window.onresize = () => this.updateContainerSize$.next();
    
    this.onKeyDownEventListener = document.onkeydown = (evt: Event) => {
      const e = evt as KeyboardEvent
      if (e.key === "Escape" || e.key === "Esc" || e.keyCode === 27) {
        setFullScreen(false);
      }
    }
  }

  onFullScreenChange = () => {
    if (this.props.shouldShuffleOnFullscreen && isFullScreen()) {
      gwtAdapter.shuffle();
      this.props.setPuzzleStatus(false);
    }
    
    this.updateContainerSize$.next();
  }

  setOrFetchTemplate = (generatedTemplate: GeneratedTemplate) => {
    
  }
  
  updateContainerSize = () => {
    let boundingRect;
    if (isFullScreen()) {
      boundingRect = document.getElementById('embed-html')!.getBoundingClientRect();
    } else {
      // Remove the gdx app so the GameContainer can resize to its parents intended size
      // (canvas seems to override flex properties of its parents)
      const originalGameContainer = document.getElementById('jiggen-puzzle-solver');
      originalGameContainer!.appendChild(document.getElementById('embed-html')!);
      // Get the resized width and height of the container
      boundingRect = this.gameContainerRef.current.getBoundingClientRect();
      // Add the gdx app back right before we resize it to match the container
      this.gameContainerRef.current.appendChild(document.getElementById('embed-html'));
    }
    
    gwtAdapter.resizeGameContainer(boundingRect.width, boundingRect.height);
  }

  componentDidMount() {
    this.gameContainerRef.current.appendChild(document.getElementById('embed-html'));
    this.updateContainerSize$.next();
  }

  componentWillUnmount() {
    unsetOnFullScreenChange(this.onFullScreenChange);
    // Put it back in the old container so it doesn't get discarded from the DOM
    const originalGameContainer = document.getElementById('jiggen-puzzle-solver');
    originalGameContainer!.appendChild(document.getElementById('embed-html')!);

    this.updateContainerSubscription.unsubscribe();
    if (document.removeEventListener) {
      document.removeEventListener('keydown', this.onKeyDownEventListener);
    } else {
      (document as any).detachEvent("onkeydown", this.onKeyDownEventListener);
    }
  }

  componentDidUpdate(prevProps: GameContainerProps) {
    if (prevProps.showFullScreenFallback != this.props.showFullScreenFallback) {
      this.onFullScreenChange();
    }
  }

  render() {
    let classAttribute = styles.innerContainer;
    if (this.props.showFullScreenFallback) {
      classAttribute += ` ${styles.fullScreenFallback}`;
    }
    return (
      <div className={styles.mainContainer}>
        <div ref={this.gameContainerRef} className={classAttribute}>
          { /*Game canvas will be moved into here using javascript */ }
        </div>
      </div>
    );
  }
}

const mapStateToProps = (_state: any, ownProps: {}): StateProps => {
  const state = (_state as StateRoot); // Required because we can't change type of _state
  return {
    showFullScreenFallback: state.displayOptions.showFullScreenFallback,
    shouldShuffleOnFullscreen: state.puzzleSolverScreen.isFreshPuzzle
  };
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
    setPuzzleStatus: (isFreshPuzzle: Boolean) => {
      dispatch(puzzleSolverActions.setPuzzleStatus(isFreshPuzzle))
    }
  };
}

const ConnectedGameContainer = connect<StateProps, DispatchProps, {}>(
  mapStateToProps,
  mapDispatchToProps
)(GameContainer);

export default withLoadingWrapper(ConnectedGameContainer);