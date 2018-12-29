import React from 'react';
import { connect } from 'react-redux';
import { Observable, Subject, from, combineLatest, Subscription } from 'rxjs';

import {
  GeneratedTemplate,
  generatedTemplatesActions
} from '../../store/generatedTemplates';

import { Background } from '../../store/backgrounds';
import gwtAdapter from '../../gwtAdapter';


import {
  onFullScreenChange,
  isFullScreen
} from '../../utils/fullScreen';

import styles from './GameContainer.module.scss';
import { ReducersRoot, StateRoot } from '../../models';

interface DispatchProps {
  fetchGeneratedTemplateByLink(link: string): void;
}

interface StateProps {
  showFullScreenFallback: Boolean;
}

interface OwnProps {
  generatedTemplate: GeneratedTemplate;
  background: Background;
}

type GameContainerProps = StateProps & DispatchProps & OwnProps;

interface GameContainerState {
  shouldShuffleOnFullscreen: Boolean;
}

class GameContainer extends React.Component<GameContainerProps, GameContainerState> {
  gameContainerRef: React.RefObject<any>;
  updateContainerSize$: Subject<void>;
  updateContainerSubscription: Subscription;

  constructor(props: GameContainerProps) {
    super(props);
    this.state = {
      shouldShuffleOnFullscreen: true
    };
    this.gameContainerRef = React.createRef();
    this.updateContainerSize$ = new Subject();

    const onGwtLoaded$ = from(gwtAdapter.onGwtLoadedPromise);
    this.updateContainerSubscription = combineLatest(this.updateContainerSize$, onGwtLoaded$).subscribe(this.updateContainerSize);
    onFullScreenChange(this.onFullScreenChange);
    window.onresize = () => {
      this.updateContainerSize$.next();
    }
  }

  onFullScreenChange = () => {
    if (this.state.shouldShuffleOnFullscreen && isFullScreen()) {
      gwtAdapter.shuffle();
      this.setState({shouldShuffleOnFullscreen: false});
    }
    
    this.updateContainerSize$.next();
  }

  setOrFetchTemplate = (generatedTemplate: GeneratedTemplate) => {
    if (generatedTemplate.vertices != null) {
      gwtAdapter.setTemplate(generatedTemplate);
    } else {
      this.props.fetchGeneratedTemplateByLink(generatedTemplate.links.self);
    }
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

    if (this.props.generatedTemplate != null) {
      this.setOrFetchTemplate(this.props.generatedTemplate);
    }
    if (this.props.background != null) {
      gwtAdapter.setBackground(this.props.background);
    }
    this.updateContainerSize$.next();
  };

  componentWillUnmount() {
    // Put it back in the old container so it doesn't get discarded from the DOM
    const originalGameContainer = document.getElementById('jiggen-puzzle-solver');
    originalGameContainer!.appendChild(document.getElementById('embed-html')!);

    this.updateContainerSubscription.unsubscribe();
  }

  componentDidUpdate(prevProps: GameContainerProps, prevState: any) {
    let isUpdated = false;
    if (prevProps.generatedTemplate !== this.props.generatedTemplate && this.props.generatedTemplate != null) {
      this.setOrFetchTemplate(this.props.generatedTemplate);
      isUpdated = true;
    }
    if (prevProps.background !== this.props.background && this.props.background != null) {
      gwtAdapter.setBackground(this.props.background);
      isUpdated = true;
    }
    if (isUpdated) {
      this.setState({
        shouldShuffleOnFullscreen: true
      });
    }
    this.updateContainerSize$.next();
  }

  render() {
    let classAttribute = styles.container;
    if (this.props.showFullScreenFallback) {
      classAttribute += ` ${styles.fullScreenFallback}`;
    }
    return (
      <div ref={this.gameContainerRef} className={classAttribute}>
        { /*Game canvas will be moved into here using javascript */ }
      </div>
    );
  }
}

const mapStateToProps = (_state: any, ownProps: OwnProps): StateProps => {
  const state = (_state as StateRoot); // Required because we can't change type of _state
  return {
    showFullScreenFallback: state.displayOptions.showFullScreenFallback
  };
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
    fetchGeneratedTemplateByLink: (link: string) => {
      dispatch(generatedTemplatesActions.fetchByLink(link))
    }
  };
}

export default connect<StateProps, DispatchProps, OwnProps>(
  mapStateToProps,
  mapDispatchToProps
)(GameContainer);
