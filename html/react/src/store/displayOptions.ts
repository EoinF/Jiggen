import { createActions, handleActions, Action } from "redux-actions";

const MODAL_TYPE_BACKGROUND_SELECTION = 'BACKGROUND_SELECTION';

export interface DisplayOptionsState {
    showFullScreenFallback: boolean,
	modalType?: string,
	isModalVisible: boolean
}

const initialState: DisplayOptionsState = {
	showFullScreenFallback: false,
	modalType: undefined,
	isModalVisible: false
};

const {
    setFullScreenFallback,
    showModal,
    hideModal
} = createActions({
    SET_FULLSCREEN_FALLBACK: (showFullScreenFallback) => ({showFullScreenFallback}),
    SHOW_MODAL: (modalType) => ({modalType}),
    HIDE_MODAL: () => {}
});

const showBackgroundsModal = () => {
	return showModal(MODAL_TYPE_BACKGROUND_SELECTION);
}

const enableFullScreenFallback = () => {
	return setFullScreenFallback(true);
}
const disableFullScreenFallback = () => {
	return setFullScreenFallback(false);
}

const reducers = handleActions({
        SET_FULLSCREEN_FALLBACK: (state: DisplayOptionsState, {payload}: Action<any>): DisplayOptionsState => ({
            ...state,
            showFullScreenFallback: payload.showFullScreenFallback
        }),
        SHOW_MODAL: (state: DisplayOptionsState, {payload}: Action<any>): DisplayOptionsState => ({
            ...state,
            modalType: payload.modalType,
            isModalVisible: true
        }),
        HIDE_MODAL: (state: DisplayOptionsState, {payload}: Action<any>): DisplayOptionsState => ({  
            ...state,
            isModalVisible: false
        })
    },
    initialState
);


const displayOptionsActions = {
    showBackgroundsModal,
    showModal,
    hideModal,
	disableFullScreenFallback,
	enableFullScreenFallback
}

export {
	displayOptionsActions,
	MODAL_TYPE_BACKGROUND_SELECTION
}
export default reducers;