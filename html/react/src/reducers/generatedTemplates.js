import { 
	FETCH_GENERATED_TEMPLATE,
	SET_GENERATED_TEMPLATE
} from '../actions';

const initialState = {
  selectedId: null,
  generatedTemplatesMap: {
  	// map of generated template ids to generated templates
  },
  isFetching: false,
};


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
			console.log("Setting generatedTemplates");
			const x = setGeneratedTemplate(state, action);
			console.log(x);
			return x;
		default:
			return state;
	}
}

export default generatedTemplateReducers;