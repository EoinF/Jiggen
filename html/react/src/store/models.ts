
export interface StringMap<T> {
	[key: string]: T
}

export interface Resource {
	id: string;
	links: any;
}

export interface BaseState {
	selectedId: string | null;
	resourceMap: StringMap<Resource>;
	linkMap:  StringMap<Resource>;
	isFetching: boolean;
}

export interface ReducersRoot {
	templates: any,
    backgrounds: any,
    generatedTemplates: any,
    resourceLinks: any,
    displayOptions: any,
    playablePuzzles: any
}
