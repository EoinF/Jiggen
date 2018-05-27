import { 
	fetchGeneratedTemplateById,
	fetchGeneratedTemplatesByLink,
	setGeneratedTemplate,
	addGeneratedTemplates,
	FETCH_GENERATED_TEMPLATE, 
	SET_GENERATED_TEMPLATE,
	ADD_GENERATED_TEMPLATES
} from './generatedTemplates';

import { 
	fetchTemplates,
	setTemplates,
	selectTemplate,
	FETCH_TEMPLATES, 
	SET_TEMPLATES,
	SELECT_TEMPLATE
} from './templates';

import { 
	fetchResourceLinks,
	SET_RESOURCE_LINKS,
} from './resourceLinks';

const generatedTemplatesActions = {
	fetchGeneratedTemplateById,
	fetchGeneratedTemplatesByLink,
	setGeneratedTemplate,
	addGeneratedTemplates
}

const templatesActions = {
	fetchTemplates,
	setTemplates,
	selectTemplate
}

const resourceLinksActions = {
	fetchResourceLinks
}

export {
	// templates
	templatesActions,
	FETCH_TEMPLATES,
	SET_TEMPLATES,
	SELECT_TEMPLATE,

	// generated templates
	generatedTemplatesActions,
	FETCH_GENERATED_TEMPLATE, 
	SET_GENERATED_TEMPLATE,
	ADD_GENERATED_TEMPLATES,

	// resource links
	resourceLinksActions,
	SET_RESOURCE_LINKS
};
