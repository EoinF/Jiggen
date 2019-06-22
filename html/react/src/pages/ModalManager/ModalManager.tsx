import React, {Component} from 'react';
import { StateRoot } from '../../models';
import { ModalType, displayOptionsActions, modalTypeFromValue } from '../../store/displayOptions';
import { connect } from 'react-redux';
import { withRouter, RouteComponentProps } from 'react-router';
import ModalSwitcher from './ModalSwitcher';

interface StateProps {
    modalType: string,
    isModalVisible: boolean;
}

interface DispatchProps {
    hideModal(): void;
    showModal(modalType: ModalType): void;
}

type ModalManagerProps = StateProps & DispatchProps & RouteComponentProps;

class ModalManager extends Component<ModalManagerProps> {
    componentDidMount() {
        this.updateFromProps();
        this.updateFromHash(this.props.location.hash);
    }

    componentDidUpdate(prevProps: ModalManagerProps) {
        if (this.props.isModalVisible !== prevProps.isModalVisible) {
            this.updateFromProps();
        }
        if (this.props.location.hash !== prevProps.location.hash) {
            this.updateFromHash(this.props.location.hash);
        } 
    }

    updateFromHash(hash: string) {
        const modalType = modalTypeFromValue(hash);
        if (modalType != null) {
            this.props.showModal(modalType);
        } else {
            this.props.hideModal();
        }
    }

    updateFromProps() {
        if (this.props.isModalVisible) {
            const modalTypeFromHash = modalTypeFromValue(this.props.location.hash);
            if (modalTypeFromHash == null) {
                this.props.history.push({...this.props.location, hash: this.props.modalType});
            }
        }
    }

    render() {
        const {
            modalType,
            isModalVisible,
            children
        } = this.props;
        // Hide the background content if on mobile
        let backgroundContentClassName = 'expandToFit'; //isModalVisible ? 'hiddenIfMobile expandToFit': 'expandToFit';

        return <React.Fragment>
            <div className={backgroundContentClassName}>
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
