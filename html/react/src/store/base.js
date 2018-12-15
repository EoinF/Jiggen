import store from '../store';

const BROKEN_LINK = 'BROKEN_LINK';

const initialState = {
  selectedId: null,
  resourceMap: {
  	// map of resource ids to resource
  },
  linkMap: {
  	// map of unique links to resources
  },
  isFetching: false,
}

function setResources(state, {resourceList}) {
	let resourceMap = {};
	let linksMap = {};

	resourceList.forEach(resource => {
		resourceMap[resource.id] = resource;
		linksMap[resource.links.self] = resource;
	});

	return {
		...state,
		resourceMap,
		linksMap,
		isFetching: false
	};
}

function addResources(state, {resourceList}) {
	let {
		resourceMap, 
		linksMap
	} = state;

	const newEntries = {};
	const newLinks = {};

	resourceList.forEach(resource => {
		newEntries[resource.id] = resource;
		newLinks[resource.links.self] = resource;
	});

	return {
		...state,
		resourceMap: {
			...resourceMap,
			...newEntries
		},
		linksMap: {
			...linksMap,
			...newLinks
		},
		isFetching: false
	}
}

function setOrUpdateResource(state, {resource}) {
	let {
		resourceMap,
		linksMap
	} = state;

	console.log(state);
	console.log(resource);

	return {
		...state,
		resourceMap: {
			...resourceMap,
			[resource.id]: resource
		},
		linksMap: {
			...linksMap,
			[resource.links.self]: resource
		},
		isFetching: false
	};
}

function selectResource(state, {selectedId}) {
	return {
		...state,
		selectedId
	};
}

function setIsFetching(state, {isFetching}) {
	return {
		...state,
		isFetching
	};
}


const getOrFetchResourceByLink = (link, fetchResourceWithLink, getResourceFromState) => {
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