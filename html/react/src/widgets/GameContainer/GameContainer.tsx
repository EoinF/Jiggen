import * as React from 'react';
import { connect } from 'react-redux';

import {
  GeneratedTemplate,
  generatedTemplatesActions
} from '../../store/generatedTemplates';
import gwtAdapter from '../../gwtAdapter';


import {
  onFullScreenChange,
  isFullScreen
} from '../../utils/fullScreen';

import styles from './GameContainer.module.scss';
import { ReducersRoot } from '../../models';

interface GameContainerProps {
  fetchGeneratedTemplateByLink: Function;
  generatedTemplate: GeneratedTemplate;
  background: any;
  showFullScreenFallback: Boolean;
};

class GameContainer extends React.Component<GameContainerProps> {
  gameContainerRef: any;

  constructor(props: GameContainerProps) {
    super(props);
    this.gameContainerRef = React.createRef();
    onFullScreenChange(this.onFullScreenChange);
  }

  onFullScreenChange = () => {
    if (!isFullScreen()) {
      this.updateContainerSize();
    }
  }

  setOrFetchTemplate = (generatedTemplate: GeneratedTemplate) => {
    if ('vertices' in generatedTemplate) {
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
    this.updateContainerSize();
  }

  updateContainerSize = () => {
    const {
      width,
      height
    } = this.gameContainerRef.current.getBoundingClientRect();

    const canvasElement: HTMLImageElement = document.querySelector('#embed-html canvas') as HTMLImageElement;
    if (canvasElement) {
      canvasElement.width = width;
      canvasElement.height = height;
    }
    const tableElement = document.querySelector('#embed-html table') as HTMLTableElement;
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


const mapStateToProps = (state: ReducersRoot) => {
  return {
    showFullScreenFallback: state.displayOptions.showFullScreenFallback
  };
}

const mapDispatchToProps = (dispatch: Function) => {
  return {
    fetchGeneratedTemplateByLink: (link: string) => {
      dispatch(generatedTemplatesActions.fetchByLink(link))
    }
  }
}

const ConnectedGameContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(GameContainer);

export default ConnectedGameContainer;
