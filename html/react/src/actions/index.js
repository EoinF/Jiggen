import { 
	fetchGeneratedTemplate,
	setGeneratedTemplate,
	FETCH_GENERATED_TEMPLATE, 
	SET_GENERATED_TEMPLATE
} from './generatedTemplates';

import { 
	fetchResourceLinks,
	FETCH_RESOURCE_LINKS,
} from './resourceLinks';

const generatedTemplatesActions = {
	fetchGeneratedTemplate,
	setGeneratedTemplate
}

const resourceLinksActions = {
	fetchResourceLinks
}

export {
	// generated templates
	generatedTemplatesActions,
	FETCH_GENERATED_TEMPLATE, 
	SET_GENERATED_TEMPLATE,

	// resource links
	resourceLinksActions,
	FETCH_RESOURCE_LINKS
};
