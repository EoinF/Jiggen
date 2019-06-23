import React, {Component} from 'react';
import { ModalType } from '../../store/displayOptions';
import BackgroundSelectionModal from '../BackgroundSelectionModal';
import TemplateSelectionModal from '../TemplateSelectionModal/TemplateSelectionModal';
import PuzzleCompleteModal from '../../widgets/PuzzleCompleteModal/PuzzleCompleteModal';

interface ModalSwitcherProps {
    modalType: ModalType,
    isModalVisible: boolean;
}

class ModalSwitcher extends Component<ModalSwitcherProps> {
    render() {
        if (this.props.isModalVisible) {
            switch(this.props.modalType) {
                case ModalType.BACKGROUND_SELECT:
                    return <BackgroundSelectionModal/>;
                case ModalType.TEMPLATE_SELECT:
                    return <TemplateSelectionModal/>;
                case ModalType.PUZZLE_COMPLETE:
                    return <PuzzleCompleteModal/>
            }
        }
        return null;
    }
}

export default ModalSwitcher;
