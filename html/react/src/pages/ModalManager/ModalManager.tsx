import React, {Component} from 'react';
import { StateRoot } from '../../models';
import { ModalType, displayOptionsActions } from '../../store/displayOptions';
import { connect } from 'react-redux';
import { withRouter, RouteComponentProps } from 'react-router';
import ModalSwitcher from './ModalSwitcher';

interface StateProps {
    modalType: ModalType,
    isModalVisible: boolean;
}

interface DispatchProps {
    hideModal(): void;
    showModal(modalType: ModalType): void;
}

type ModalManagerProps = StateProps & DispatchProps & RouteComponentProps;

class ModalManager extends Component<ModalManagerProps> {
    render() {
        const {
            modalType,
            isModalVisible,
            children
        } = this.props;

        return <React.Fragment>
            <div className={'expandToFit'}>
                {children}
            </div>
            <ModalSwitcher modalType={modalType} isModalVisible={isModalVisible} />
        </React.Fragment>
    }
}

const mapStateToProps = (state: StateRoot) => {
    return {
        isModalVisible: state.displayOptions.isModalVisible,
        modalType: state.displayOptions.modalType
    } as ModalManagerProps
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
    return {
        hideModal: () => dispatch(displayOptionsActions.hideModal()),
        showModal: (modalType: ModalType) => dispatch(displayOptionsActions.showModal(modalType))
    }
}

const ConnectedModalManager = connect(
  mapStateToProps,
  mapDispatchToProps
)(ModalManager);

const ConnectedModalManagerWithRouter = withRouter(
    ConnectedModalManager as any
);

export default ConnectedModalManagerWithRouter;
