import store from '.';
import { BaseState, StringMap, Resource } from '../models';

const initialState: BaseState<Resource> = {
	selectedId: null,
	resourceList: [],
	resourceMap: {
		// map of resource ids to resource
	},
	linkMap: {
		// map of unique links to resources
	},
	isFetching: false,
  }
  
const BROKEN_LINK = 'BROKEN_LINK';

function setResources(state: BaseState<Resource>, {resourceList}: {resourceList: Resource[]}): BaseState<Resource> {
	let resourceMap: StringMap<Resource> = {};
	let linkMap: StringMap<Resource> = {};

	resourceList.forEach((resource: Resource) => {
		resourceMap[resource.id] = resource;
		linkMap[resource.links.self] = resource;
	});

	return {
		...state,
		resourceList,
		resourceMap,
		linkMap,
		isFetching: false
	};
}

function addResources(state: BaseState<Resource>, {resourceList}: {resourceList: Resource[]}): BaseState<Resource> {
	let {
		resourceList: oldResourceList,
		resourceMap, 
		linkMap
	} = state;

	const newEntries: StringMap<Resource> = {};
	const newLinks: StringMap<Resource> = {};
	const newResourceList: Resource[] = [...oldResourceList];

	resourceList.forEach((resource: Resource) => {
		newEntries[resource.id] = resource;
		newLinks[resource.links.self] = resource;

		// Check if the resource is already in the list
		let i = 0;
		for (i = 0; i < newResourceList.length; i++) {
			if (newResourceList[i].id === resource.id) {
				newResourceList[i] = resource;
				break;
			}
		}
		// If the resource doesn't already exist, add it
		if (i === newResourceList.length) {
			newResourceList.push(resource);
		}
	});

	return {
		...state,
		resourceList: newResourceList,
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

function setOrUpdateResource(state: BaseState<Resource>, {resource}: {resource: Resource}): BaseState<Resource> {
	let {
		resourceList,
		resourceMap,
		linkMap
	} = state;

	const resourceListUpdated = [...resourceList];
	const index = resourceList.findIndex((r: Resource) => {
		return r.id == resource.id
	});
	if (index != -1) {
		resourceListUpdated[index] = resource;
	} else {
		resourceListUpdated.push(resource);
	}

	return {
		...state,
		resourceList: resourceListUpdated,
		resourceMap: {
			...resourceMap,
			[resource.id]: resource
		},
		linkMap: {
			...linkMap,
			[resource.links.self]: resource
		},
		isFetching: false
	};
}

function removeResource(state: BaseState<Resource>, {resource}: {resource: Resource}): BaseState<Resource> {
	let {
		resourceList,
		resourceMap,
		linkMap
	} = state;

	const resourceMapUpdated = {...resourceMap};
	const linkMapUpdated = {...linkMap};

	delete resourceMapUpdated[resource.id];
	delete linkMapUpdated[resource.links.self];

	return {
		...state,
		resourceList: resourceList.filter(existingResource => existingResource.id !== resource.id),
		resourceMap: resourceMapUpdated,
		linkMap: linkMapUpdated
	};
}

function selectResource(state: BaseState<Resource>, {selectedId}: {selectedId: string}): BaseState<Resource> {
	return {
		...state,
		selectedId
	};
}

function setIsFetching(state: BaseState<Resource>, {isFetching}: {isFetching: boolean}): BaseState<Resource> {
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
	removeResource,
	selectResource,
	setOrUpdateResource,
	setIsFetching,
	
	getOrFetchResourceByLink,
	
	BROKEN_LINK
};