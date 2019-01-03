import React, {Component} from 'react';
import { StateRoot } from '../../models';
import { MODAL_TYPE_BACKGROUND_SELECTION, displayOptionsActions } from '../../store/displayOptions';
import { connect } from 'react-redux';
import { withRouter, RouteComponentProps } from 'react-router';
import ModalSwitcher from './ModalSwitcher';

interface StateProps {
    modalType: string,
    isModalVisible: boolean;
}

interface DispatchProps {
    hideModal(): void;
    showModal(modalType: string): void;
}

type ModalManagerProps = StateProps & DispatchProps & RouteComponentProps;

class ModalManager extends Component<ModalManagerProps> {
    componentDidMount() {
        this.updateFromProps();
        this.updateFromHash(this.props.location.hash);
    }

    componentDidUpdate(prevProps: ModalManagerProps) {
        if (this.props.isModalVisible != prevProps.isModalVisible) {
            this.updateFromProps();
        }
        if (this.props.location.hash != prevProps.location.hash) {
            this.updateFromHash(this.props.location.hash);
        } 
    }

    updateFromHash(hash: string) {
        if (hash === '#background') {
            this.props.showModal(MODAL_TYPE_BACKGROUND_SELECTION);
        } else {
            this.props.hideModal();
        }
    }

    updateFromProps() {
        if (this.props.isModalVisible) {
            if (this.props.location.hash !== '#background') {
                this.props.history.push({...this.props.location, hash: 'background'});
            }
        }
    }

    render() {
        const {
            modalType,
            isModalVisible
        } = this.props;
        return <ModalSwitcher modalType={modalType} isModalVisible={isModalVisible} />
    }
}

const mapStateToProps = (state: StateRoot) => {
    return {
        isModalVisible: state.displayOptions.isModalVisible,
        modalType: state.displayOptions.modalType
    } as ModalManagerProps
}

const mapDispatchToProps = (dispatch: Function) => {
    return {
        hideModal: () => dispatch(displayOptionsActions.hideModal()),
        showModal: (modalType: string) => dispatch(displayOptionsActions.showModal(modalType))
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
