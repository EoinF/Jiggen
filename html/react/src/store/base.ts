import store from '.';
import { BaseState, StringMap, Resource } from '../models';

const initialState: BaseState<Resource> = {
	selectedId: null,
	linkMap: {
		// map of unique links to resources
	},
	isFetching: false,
  }
  
const BROKEN_LINK = 'BROKEN_LINK';

function setResources(state: BaseState<Resource>, {resourceList}: {resourceList: Resource[]}): BaseState<Resource> {
	let linkMap: StringMap<Resource> = {};

	resourceList.forEach((resource: Resource) => {
		linkMap[resource.links.self] = resource;
	});

	return {
		...state,
		linkMap,
		isFetching: false
	};
}

function addResources(state: BaseState<Resource>, {resourceList}: {resourceList: Resource[]}): BaseState<Resource> {
	let {
		linkMap
	} = state;

	const newLinks: StringMap<Resource> = {};

	resourceList.forEach((resource: Resource) => {
		newLinks[resource.links.self] = resource;
	});

	return {
		...state,
		linkMap: {
			...linkMap,
			...newLinks
		},
		isFetching: false
	};
}

function setOrUpdateResource<R extends Resource>(state: BaseState<R>, {resource}: {resource: R}): BaseState<R> {
	let {
		linkMap
	} = state;

	return {
		...state,
		linkMap: {
			...linkMap,
			[resource.links.self]: resource
		},
		isFetching: false
	};
}

function removeResource(state: BaseState<Resource>, {resource}: {resource: Resource}): BaseState<Resource> {
	let {
		linkMap
	} = state;

	const linkMapUpdated = {...linkMap};
	delete linkMapUpdated[resource.links.self];

	return {
		...state,
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