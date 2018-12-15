import store from '../store.js';
import { BaseState, StringMap, Resource } from './models';

const initialState: BaseState = {
	selectedId: null,
	resourceMap: {
		// map of resource ids to resource
	},
	linkMap: {
		// map of unique links to resources
	},
	isFetching: false,
  }
  
const BROKEN_LINK = 'BROKEN_LINK';

function setResources(state: BaseState, {resourceList}: {resourceList: Resource[]}): BaseState {
	let resourceMap: StringMap<Resource> = {};
	let linkMap: StringMap<Resource> = {};

	resourceList.forEach((resource: Resource) => {
		resourceMap[resource.id] = resource;
		linkMap[resource.links.self] = resource;
	});

	return {
		...state,
		resourceMap,
		linkMap,
		isFetching: false
	};
}

function addResources(state: BaseState, {resourceList}: {resourceList: Resource[]}): BaseState {
	let {
		resourceMap, 
		linkMap
	} = state;

	const newEntries: StringMap<Resource> = {};
	const newLinks: StringMap<Resource> = {};

	resourceList.forEach((resource: Resource) => {
		newEntries[resource.id] = resource;
		newLinks[resource.links.self] = resource;
	});

	return {
		...state,
		resourceMap: {
			...resourceMap,
			...newEntries
		},
		linkMap: {
			...linkMap,
			...newLinks
		},
		isFetching: false
	};
}

function setOrUpdateResource(state: BaseState, {resource}: {resource: Resource}) {
	let {
		resourceMap,
		linkMap
	} = state;

	return {
		...state,
		resourceMap: {
			...resourceMap,
			[resource.id]: resource
		},
		linksMap: {
			...linkMap,
			[resource.links.self]: resource
		},
		isFetching: false
	};
}

function selectResource(state: BaseState, {selectedId}: {selectedId: string}) {
	return {
		...state,
		selectedId
	};
}

function setIsFetching(state: BaseState, {isFetching}: {isFetching: Boolean}) {
	return {
		...state,
		isFetching
	};
}


function getOrFetchResourceByLink(link: string, fetchResourceWithLink: Function, getResourceFromState: Function): Promise<Resource> {
	return new Promise((resolve, reject) => {
		const existingResource = getResourceFromState().linkMap[link];
		if (existingResource != null) {
			resolve(existingResource);
		} else {
			fetchResourceWithLink();
			const unsubscribe = store.subscribe(() => {
				const existingResource = getResourceFromState().linkMap[link];
				if (existingResource != null && existingResource !== BROKEN_LINK) {
					resolve(existingResource);
					unsubscribe();
				}
			});
		}
	})
}


export default {
	initialState,

	setResources,
	addResources,
	selectResource,
	setOrUpdateResource,
	setIsFetching,
	
	getOrFetchResourceByLink,
	
	BROKEN_LINK
};