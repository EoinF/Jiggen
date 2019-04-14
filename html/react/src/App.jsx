import React, { Component } from 'react';
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom';
import { Provider } from 'react-redux'

import './index.scss';

import CustomPuzzlesPage from './pages/CustomPuzzlesPage/CustomPuzzlesPage';
import PuzzleOfTheDayPage from './pages/PuzzleOfTheDayPage';
import NavigationBar from './widgets/NavigationBar/NavigationBar';
import ModalManager from './pages/ModalManager/ModalManager';
import { GameContainer } from './widgets';
import PlayPage from './pages/PlayPage/PlayPage';


class App extends Component {
	render() {
		const { store } = this.props;
		return (
  			<Provider store={store}>
				<BrowserRouter basename={process.env.REACT_APP_PUBLIC_URL || ''}>
					<ModalManager>
						<AppContents/>
						<GameContainer/>
					</ModalManager>
				</BrowserRouter>
  			</Provider>
		);
	}	
}

const AppContents = () => {
	return <div className='flexContainer'>
		<Switch>
			<Route path='/custom' component={CustomPuzzlesPage}/>
			<Route path='/daily' component={PuzzleOfTheDayPage}/>
			<Route path='/play' component={PlayPage}/>
			<Route path='/'>
				<Redirect to="/daily"/>
			</Route>
		</Switch>
		<Route path='/' component={NavigationBar}/>
	</div>
};

export default App;