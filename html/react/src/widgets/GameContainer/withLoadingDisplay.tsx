import React, { Component } from "react";
import { from, Subscription } from "rxjs";
import gwtAdapter from "../../gwtAdapter";
import styles from './GameContainer.module.scss';

import puzzlePieceIcon from './piece-outline-rounded.png';
import { StateRoot, Resource } from "../../models";
import { connect } from "react-redux";
import { DownloadedImage } from "../../store/downloadedImages";
import { GeneratedTemplate } from "../../store/generatedTemplates";
import { Template } from "../../store/templates";

interface LoadingDisplayState {
    isGWTLoading: Boolean;
}

interface DispatchProps {
    downloadImage(resource: Resource): void;
    fetchTemplate(resource: Resource): void;
}
interface StateProps {
    selectedGeneratedTemplate: GeneratedTemplate;
    selectedTemplate: Template;
    downloadedImage: DownloadedImage;
}

type LoadingDisplayWrapperProps = StateProps & DispatchProps;

class LoadingDisplayWrapper extends Component<LoadingDisplayWrapperProps, LoadingDisplayState> {
    gwtSubscription?: Subscription;

    state = {
        isGWTLoading: true
    }

    componentDidMount() {
        const onGwtLoaded$ = from(gwtAdapter.onGwtLoadedPromise);
        this.gwtSubscription = onGwtLoaded$.subscribe(this.onLoad);
    }
    componentWillUnmount() {
        this.gwtSubscription!.unsubscribe();
    }

    isGeneratedTemplateReady = (generatedTemplate: GeneratedTemplate) => {
        return generatedTemplate.vertices != null;
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
        if (this.props.downloadedImage && this.props.downloadedImage.isDownloading) {
            const downloadedData = this.toDataString(this.props.downloadedImage.bytesDownloaded);
            const totalData = this.toDataString(this.props.downloadedImage.bytesTotal);

            const dataComponent = this.props.downloadedImage.bytesTotal > 0
                ? <div>Downloading {downloadedData} / {totalData}</div>
                    : <div>Downloading {downloadedData}</div>

            return <div className={styles.mainContainer}>
                <div className={styles.loadingContainer}>
                    {dataComponent}
                    <img width='32px' height='32px'
                        src={puzzlePieceIcon}
                        className={styles.loadingSpinner}
                        alt='Loading spinner'
                    />
                </div>
            </div>
        } else if (this.props.selectedGeneratedTemplate && !this.isGeneratedTemplateReady(this.props.selectedGeneratedTemplate)) {
            return <div className={styles.mainContainer}>
                <div className={styles.loadingContainer}>
                    Downloading template data
                    <img width='32px' height='32px'
                        src={puzzlePieceIcon}
                        className={styles.loadingSpinner}
                        alt='Loading spinner'
                    />
                </div>
            </div>
        } else if (this.state.isGWTLoading) {
            return <div className={styles.mainContainer}>
                <div className={styles.loadingContainer}>
                    <div>Unpacking the box...</div>
                    <img width='32px' height='32px'
                        src={puzzlePieceIcon}
                        className={styles.loadingSpinner}
                        alt='Loading spinner'
                    />
                </div>
            </div>
        } else  {
            return this.props.children;
        }
    }
}

const mapStateToProps = (_state: any, ownProps: any): StateProps => {
    const state = (_state as StateRoot); // Required because we can't change type of _state
    const selectedTemplate = state.generatedTemplates.linkMap[state.puzzleSolverScreen.selectedTemplate!]
    return {
        selectedTemplate,
        selectedGeneratedTemplate: selectedTemplate && state.generatedTemplates.linkMap[selectedTemplate.links.generatedTemplate!], 
        downloadedImage: state.downloadedImages.linkMap[state.puzzleSolverScreen.selectedBackground!],
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
