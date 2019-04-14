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
    displayOptionsActions,
    modalTypeFromValue
}
export default reducers;