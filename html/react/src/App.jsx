import React, { Component } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import { Provider } from 'react-redux'

import './index.css';

import CustomizePage from './pages/CustomizePage';


class App extends Component {
	render() {
		const { store } = this.props;
		return (
  			<Provider store={store}>
				<BrowserRouter basename={process.env.REACT_APP_PUBLIC_URL || ''}>
					<Switch>
						<Route path='/' component={CustomizePage}/>
					</Switch>
				</BrowserRouter>
  			</Provider>
		);
	}
}

export default App;