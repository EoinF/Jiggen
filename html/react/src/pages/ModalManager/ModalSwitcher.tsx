import React, {Component} from 'react';
import { MODAL_TYPE_BACKGROUND_SELECTION } from '../../store/displayOptions';
import BackgroundSelectionModal from '../BackgroundSelectionModal';

interface ModalSwitcherProps {
    modalType: string,
    isModalVisible: boolean;
}

class ModalSwitcher extends Component<ModalSwitcherProps> {
    render() {
        if (this.props.isModalVisible) {
            switch(this.props.modalType) {
                case MODAL_TYPE_BACKGROUND_SELECTION:
                    return <BackgroundSelectionModal/>;
            }
        }
        return null;
    }
}

export default ModalSwitcher;
