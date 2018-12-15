import React, {Component} from 'react';
import { connect } from 'react-redux';

import {
  generatedTemplatesActions,
} from '../../store/generatedTemplates';

import gwtAdapter from '../../gwtAdapter';

import {
  onFullScreenChange,
  isFullScreen
} from '../../utils/fullScreen';

import styles from './GameContainer.module.scss';

class GameContainer extends Component {

  constructor(props) {
    super(props);
    this.gameContainerRef = React.createRef();
    onFullScreenChange(this.onFullScreenChange);
  }

  onFullScreenChange = () => {
    if (!isFullScreen()) {
      this.updateContainerSize();
    }
  }

  setOrFetchTemplate = (generatedTemplate) => {
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
    originalGameContainer.appendChild(document.getElementById('embed-html'));
  }

  componentDidUpdate(prevProps, prevState) {
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

    const canvasElement = document.querySelector('#embed-html canvas');
    if (canvasElement) {
      canvasElement.width = width;
      canvasElement.height = height;
    }
    const tableElement = document.querySelector('#embed-html table');
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


const mapStateToProps = state => {
  return {
    showFullScreenFallback: state.displayOptions.showFullScreenFallback
  };
}

const mapDispatchToProps = dispatch => {
  return {
    fetchGeneratedTemplateByLink: id => {
      dispatch(generatedTemplatesActions.fetchGeneratedTemplateByLink(id))
    }
  }
}

const ConnectedGameContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(GameContainer);

export default ConnectedGameContainer;
