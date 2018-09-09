import React, { Component } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import { Provider } from 'react-redux'

import './index.scss';

import OverviewScreen from './pages/OverviewScreen';
import TemplateSelection from './pages/TemplateSelection';
import BackgroundSelection from './pages/BackgroundSelection';
import PuzzleSolver from './pages/PuzzleSolver';

class App extends Component {
	render() {
		const { store } = this.props;
		return (
  			<Provider store={store}>
				<BrowserRouter basename={process.env.REACT_APP_PUBLIC_URL || ''}>
					<Switch>
						<Route exact path='/' component={OverviewScreen}/>
						<Route exact path='/templates' component={TemplateSelection}/>
						<Route exact path='/backgrounds' component={BackgroundSelection}/>
						<Route path='/puzzle/:id' component={PuzzleSolver}/>
						<Route path='/' component={OverviewScreen}/>
					</Switch>
				</BrowserRouter>
  			</Provider>
		);
	}
}

export default App;