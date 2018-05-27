import { 
	FETCH_TEMPLATES,
	SET_TEMPLATES,
	SELECT_TEMPLATE
} from '../actions';

const initialState = {
  selectedId: null,
  templates: [],
  templatesMap: {
  	// map of template ids to templates
  },
  isFetching: false,
};

function selectTemplate(state, {id}) {
	return {
		...state,
		selectedId: id
	}
}

function setTemplates(state, {templates}) {
	let templatesMap = {};

	templates.forEach(template => {
		templatesMap[template.id] = template;
	});

	return {
		...state,
		templates,
		templatesMap,
		isFetching: false
	};
}

function startFetchingTemplates(state, _) {
	return {
		...state,
		isFetching: true
	};
}


function templateReducers(state = initialState, action) {
	switch (action.type) {
		case FETCH_TEMPLATES:
			return startFetchingTemplates(state, action);
		case SET_TEMPLATES:
			return setTemplates(state, action)
		case SELECT_TEMPLATE:
			return selectTemplate(state, action)
		default:
			return state;
	}
}

export default templateReducers;