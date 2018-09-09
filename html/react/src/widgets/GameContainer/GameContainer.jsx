import React, {Component} from 'react';

class GameContainer extends Component {

  constructor(props) {
    super(props);
    this.gameContainerRef = React.createRef();
  }

  componentDidMount() {
    this.gameContainerRef.current.appendChild(document.getElementById('embed-html'))
  };

  componentWillUnmount() {
    // Put it back in the old container so it doesn't get discarded from the DOM
    const originalGameContainer = document.getElementById('jiggen-puzzle-solver');
    originalGameContainer.appendChild(document.getElementById('embed-html'));
  }

  componentDidUpdate() {
    const {
      width, 
      height
    } = this.gameContainerRef.current.getBoundingClientRect();

    console.log('game container size:', {width, height});

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
    return (
      <div ref={this.gameContainerRef} className="gameContainer">
        { /*Game canvas will be moved into here using javascript */ }
      </div>
    );
  }
}

export default GameContainer;
