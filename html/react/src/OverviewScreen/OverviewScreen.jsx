import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import './OverviewScreen.css';
import TemplateDisplay from '../TemplateDisplay';


class OverviewScreen extends Component {
  render() {
    return (
        <div className="OverviewScreen">
          <div>
            <TemplateDisplay/>
          </div>
          <div>
            <img src="" alt="hi"/>
          </div>
          <button>
            <Link to={`/puzzle`}>solve puzzle!</Link>
          </button>
        </div>
    );
  }
}

export default OverviewScreen;
