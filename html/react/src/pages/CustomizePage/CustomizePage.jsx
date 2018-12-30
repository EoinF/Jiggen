import React from 'react';
import { Route, Switch } from 'react-router-dom';

import OverviewScreen from './OverviewScreen';

const CustomizePage = () => {
	return (<Switch>
		<Route component={OverviewScreen}/> 
	</Switch>);
}

export default CustomizePage;