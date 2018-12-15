import React from 'react';
import { Route, Switch } from 'react-router-dom';

import OverviewScreen from './OverviewScreen';
import TemplateSelection from './TemplateSelection';
import BackgroundSelection from './BackgroundSelection';

const CustomizePage = () => {
	return (<Switch>
		<Route exact path='/custom/templates' component={TemplateSelection}/>
		<Route exact path='/custom/backgrounds' component={BackgroundSelection}/>
		<Route component={OverviewScreen}/> 
	</Switch>);
}

export default CustomizePage;