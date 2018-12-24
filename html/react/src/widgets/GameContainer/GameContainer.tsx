import * as React from 'react';
import { connect } from 'react-redux';
import { Observable, Subject, from, combineLatest } from 'rxjs';

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

class GameContainer extends React.Component<GameContainerProps> {
  gameContainerRef: React.RefObject<any>;
  updateContainerSize$: Subject<void>;

  constructor(props: GameContainerProps) {
    super(props);
    this.gameContainerRef = React.createRef();
    this.updateContainerSize$ = new Subject();

    const onGwtLoaded$ = from(gwtAdapter.onGwtLoadedPromise);
    combineLatest(this.updateContainerSize$, onGwtLoaded$).subscribe(this.updateContainerSize);
    onFullScreenChange(this.onFullScreenChange);
  }

  onFullScreenChange = () => {
    if (!isFullScreen()) {
      this.updateContainerSize$.next();
    }
  }

  setOrFetchTemplate = (generatedTemplate: GeneratedTemplate) => {
    if (generatedTemplate.vertices != null) {
      gwtAdapter.setTemplate(generatedTemplate);
    } else {
      this.props.fetchGeneratedTemplateByLink(generatedTemplate.links.self);
    }
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
  }

  componentDidUpdate(prevProps: GameContainerProps, prevState: any) {
    if (prevProps.generatedTemplate !== this.props.generatedTemplate && this.props.generatedTemplate != null) {
      this.setOrFetchTemplate(this.props.generatedTemplate);
    }
    if (prevProps.background !== this.props.background && this.props.background != null) {
      gwtAdapter.setBackground(this.props.background);
    }
    this.updateContainerSize$.next();
  }

  updateContainerSize = () => {
    const {
      width,
      height
    } = this.gameContainerRef.current.getBoundingClientRect();

    const canvasElement: HTMLImageElement = document.querySelector('#embed-html canvas') as HTMLImageElement;
    console.log(canvasElement);
    if (canvasElement) {
      canvasElement.width = width;
      canvasElement.height = height;
    }
    const tableElement = document.querySelector('#embed-html table') as HTMLTableElement;
    console.log(tableElement);
    if (tableElement) {
      tableElement.style['width'] = width + 'px';
      tableElement.style['height'] = height + 'px';
    }
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
