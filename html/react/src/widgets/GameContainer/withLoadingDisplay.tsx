import React, { Component } from "react";
import { Subscription } from "rxjs";
import { gwtAppLoaded$ } from "../../gwtAdapter";
import styles from './GameContainer.module.scss';

import { StateRoot, Resource } from "../../models";
import { connect } from "react-redux";
import { DownloadedImage } from "../../store/downloadedImages";
import { DownloadedTemplate } from "../../store/downloadedTemplates";
import LoadingSpinner from "../LoadingSpinner/LoadingSpinner";

interface LoadingDisplayState {
    isGWTLoading: Boolean;
}

interface DispatchProps {
    downloadImage(resource: Resource): void;
    fetchTemplate(resource: Resource): void;
}
interface StateProps {
    downloadedTemplate: DownloadedTemplate;
    downloadedImage: DownloadedImage;
    isActive: boolean;
}

type LoadingDisplayWrapperProps = StateProps & DispatchProps;

class LoadingDisplayWrapper extends Component<LoadingDisplayWrapperProps, LoadingDisplayState> {
    gwtSubscription?: Subscription;

    state = {
        isGWTLoading: true
    }

    componentDidMount() {
        this.gwtSubscription = gwtAppLoaded$.subscribe(this.onLoad);
    }
    componentWillUnmount() {
        this.gwtSubscription!.unsubscribe();
    }

    onLoad = () => {
        this.setState({
            isGWTLoading: false
        });
    }

    toDataString = (bytes: number) => {
        const kb = bytes / 1024;
        const mb = kb / 1024;

        if (mb > 1) {
            return mb.toFixed(2) + 'MB';
        } else if (kb > 1) {
            return kb.toFixed(2) + 'KB';
        } else {
            return bytes + ' bytes';
        }
    }

    render() {
        if (!this.props.isActive) {
            return null;
        } else {
            if (this.props.downloadedImage && this.props.downloadedImage.isDownloading) {
                const downloadedData = this.toDataString(this.props.downloadedImage.bytesDownloaded);
                const totalData = this.toDataString(this.props.downloadedImage.bytesTotal);

                const dataComponent = this.props.downloadedImage.bytesTotal > 0
                    ? <div>Downloading {downloadedData} / {totalData}</div>
                        : <div>Downloading {downloadedData}</div>

                return <div className={styles.mainContainer}>
                    <div className={styles.loadingContainer}>
                        {dataComponent}
                        <LoadingSpinner/>
                    </div>
                </div>
            } else if (this.props.downloadedTemplate && this.props.downloadedTemplate.isDownloading) {
                return <div className={styles.mainContainer}>
                    <div className={styles.loadingContainer}>
                        Downloading template data
                        <LoadingSpinner/>
                    </div>
                </div>
            } else if (this.state.isGWTLoading) {
                return <div className={styles.mainContainer}>
                    <div className={styles.loadingContainer}>
                        <div>Unpacking the box...</div>
                        <LoadingSpinner/>
                    </div>
                </div>
            } else  {
                return this.props.children;
            }
        }
    }
}

const mapStateToProps = (_state: any, ownProps: any): StateProps => {
    const state = (_state as StateRoot); // Required because we can't change type of _state
    return {
        downloadedTemplate: state.downloadedTemplates.linkMap[state.puzzleSolverScreen.selectedTemplate!], 
        downloadedImage: state.downloadedImages.linkMap[state.puzzleSolverScreen.selectedBackground!],
        isActive: state.puzzleSolverScreen.isActive
    };
}
  
const ConnectedLoadingDisplayWrapper = connect<StateProps, any, any>(
    mapStateToProps
)(LoadingDisplayWrapper);

export default function withLoadingWrapper<P>(BaseComponent: React.ComponentType<P>) {
    return (props: P) => (<ConnectedLoadingDisplayWrapper>
        <BaseComponent {...props}/>
    </ConnectedLoadingDisplayWrapper>);
}
