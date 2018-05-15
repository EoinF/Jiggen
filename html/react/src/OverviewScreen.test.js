import React from 'react';
import ReactDOM from 'react-dom';
import OverviewScreen from './OverviewScreen';

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<OverviewScreen />, div);
  ReactDOM.unmountComponentAtNode(div);
});
