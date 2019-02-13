import { createActions, handleActions, Action } from "redux-actions";

export enum ModalType {
    BACKGROUND_SELECT = '#backgrounds',
    TEMPLATE_SELECT = '#templates'
}

const modalTypeFromValue = (value: string): (ModalType | undefined) => {
    return Object.values(ModalType)
        .find((v: ModalType) => v === value);
}

export interface DisplayOptionsState {
    showFullScreenFallback: boolean,
	modalType?: ModalType,
	isModalVisible: boolean
}

const initialState: DisplayOptionsState = {
	showFullScreenFallback: false,
	modalType: undefined,
	isModalVisible: false
};

const {
    setFullscreenFallback,
    showModal,
    hideModal
} = createActions({
    SET_FULLSCREEN_FALLBACK: (showFullScreenFallback) => ({showFullScreenFallback}),
    SHOW_MODAL: (modalType: ModalType) => ({modalType}),
    HIDE_MODAL: () => {}
});

const showBackgroundsModal = () => {
	return showModal(ModalType.BACKGROUND_SELECT);
}
const showTemplatesModal = () => {
	return showModal(ModalType.TEMPLATE_SELECT);
}

const enableFullScreenFallback = () => {
    window.history.pushState('true', 'fullscreen');
	return setFullscreenFallback(true);
}
const disableFullScreenFallback = () => {
	return setFullscreenFallback(false);
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
    showTemplatesModal,
    showModal,
    hideModal,
	disableFullScreenFallback,
	enableFullScreenFallback
}

export {
    displayOptionsActions,
    modalTypeFromValue
}
export default reducers;