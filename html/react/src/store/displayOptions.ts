import { createActions, handleActions, Action } from "redux-actions";

export enum ModalType {
    BACKGROUND_SELECT,
    TEMPLATE_SELECT,
    PUZZLE_COMPLETE
}

export interface DisplayOptionsState {
	modalType?: ModalType,
	isModalVisible: boolean
}

const initialState: DisplayOptionsState = {
	modalType: undefined,
	isModalVisible: false
};

const {
    showModal,
    hideModal
} = createActions({
    SHOW_MODAL: (modalType: ModalType) => ({modalType}),
    HIDE_MODAL: () => {}
});

const showBackgroundsModal = () => {
	return showModal(ModalType.BACKGROUND_SELECT);
}
const showTemplatesModal = () => {
	return showModal(ModalType.TEMPLATE_SELECT);
}

const reducers = handleActions({
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
    hideModal
}

export {
    displayOptionsActions
}
export default reducers;