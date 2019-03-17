import React, { Component } from 'react';
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom';
import { Provider } from 'react-redux'

import './index.scss';

import CustomizePage from './pages/CustomizePage';
import PuzzleOfTheDayPage from './pages/PuzzleOfTheDayPage';
import NavigationBar from './widgets/NavigationBar/NavigationBar';
import ModalManager from './pages/ModalManager/ModalManager';


class App extends Component {
	render() {
		const { store } = this.props;
		return (
  			<Provider store={store}>
				<BrowserRouter basename={process.env.REACT_APP_PUBLIC_URL || ''}>
					<ModalManager>
						<AppContents/>
					</ModalManager>
				</BrowserRouter>
  			</Provider>
		);
	}	
}

const AppContents = () => {
	return <div className='flexContainer'>
		<Switch>
			<Route path='/custom' component={CustomizePage}/>
			<Route path='/daily' component={PuzzleOfTheDayPage}/>
			<Route path='/'>
				<Redirect to="/daily"/>
			</Route>
		</Switch>
		<Route path='/' component={NavigationBar}/>
	</div>
};

export default App;