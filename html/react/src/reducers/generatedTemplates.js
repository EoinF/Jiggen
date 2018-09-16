import { 
	FETCH_GENERATED_TEMPLATE,
	SET_GENERATED_TEMPLATE,
	ADD_GENERATED_TEMPLATES
} from '../actions/generatedTemplates';

const initialState = {
  selectedId: null,
  generatedTemplatesMap: {
  	// map of generated template ids to generated templates
  },
  isFetching: false,
};

function addGeneratedTemplates(state, {generatedTemplates}) {
	let {
		generatedTemplatesMap
	} = state;

	const newEntries = {};

	generatedTemplates.forEach(template => {
		newEntries[template.id] = template;
	});

	return {
		...state,
		generatedTemplatesMap: {
			...generatedTemplatesMap,
			...newEntries
		},
		selectedId: generatedTemplates[0].id,
		isFetching: false
	}
}

function setGeneratedTemplate(state, {generatedTemplateId, generatedTemplate}) {
	let {
		generatedTemplatesMap,
	} = state;

	return {
		...state,
		generatedTemplatesMap: {
			...generatedTemplatesMap,
			[generatedTemplateId]: generatedTemplate
		},
		selectedId: generatedTemplateId,
		isFetching: false
	};
}

function startFetchingGeneratedTemplate(state, _) {
	return {
		...state,
		isFetching: true
	};
}


function generatedTemplateReducers(state = initialState, action) {
	switch (action.type) {
		case FETCH_GENERATED_TEMPLATE:
			return startFetchingGeneratedTemplate(state, action);
		case SET_GENERATED_TEMPLATE:
			return setGeneratedTemplate(state, action);
		case ADD_GENERATED_TEMPLATES:
			return addGeneratedTemplates(state, action);
		default:
			return state;
	}
}

export default generatedTemplateReducers;