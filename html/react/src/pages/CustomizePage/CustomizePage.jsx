import React from 'react';
import { Route, Switch } from 'react-router-dom';

import OverviewScreen from './OverviewScreen';
import TemplateSelection from './TemplateSelection';
import BackgroundSelection from './BackgroundSelection';

const CustomizePage = () => {
	return (<Switch>
		<Route exact path='/templates' component={TemplateSelection}/>
		<Route exact path='/backgrounds' component={BackgroundSelection}/>
		<Route path='' component={OverviewScreen}/> 
	</Switch>);
}

export default CustomizePage;