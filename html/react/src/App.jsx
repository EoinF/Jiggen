import React, { Component } from 'react';
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom';
import { Provider } from 'react-redux'

import './index.css';

import CustomizePage from './pages/CustomizePage';
import PuzzleOfTheDayPage from './pages/PuzzleOfTheDayPage';


class App extends Component {
	render() {
		const { store } = this.props;
		return (
  			<Provider store={store}>
				<BrowserRouter basename={process.env.REACT_APP_PUBLIC_URL || ''}>
					<Switch>
						<Route path='/custom' component={CustomizePage}/>
						<Route path='/puzzlesOfTheDay' component={PuzzleOfTheDayPage}/>
						<Route path='/'>
							<Redirect to="/puzzlesOfTheDay"/>
						</Route>
					</Switch>
				</BrowserRouter>
  			</Provider>
		);
	}
}

export default App;