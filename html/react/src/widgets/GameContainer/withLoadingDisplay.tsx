import React, { Component } from "react";
import { from, Subscription } from "rxjs";
import gwtAdapter from "../../gwtAdapter";
import styles from './GameContainer.module.scss';

import puzzlePieceIcon from './piece-outline-rounded.png';
import { Background } from "../../store/backgrounds";
import { StateRoot, Resource } from "../../models";
import { connect } from "react-redux";
import { DownloadedImage, downloadedImagesActions } from "../../store/downloadedImages";
import { GeneratedTemplate, generatedTemplatesActions } from "../../store/generatedTemplates";

interface LoadingDisplayState {
    isGWTLoading: Boolean;
}

interface DispatchProps {
    downloadImage(resource: Resource): void;
    fetchGeneratedTemplate(resource: Resource): void;
}
interface StateProps {
    selectedGeneratedTemplate: GeneratedTemplate;
    selectedBackground: Background;

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

    componentDidUpdate(prevProps: LoadingDisplayWrapperProps, prevState: LoadingDisplayState) {
        if (prevProps.selectedBackground != this.props.selectedBackground &&
            (
                prevProps.selectedBackground == null || 
                prevProps.selectedBackground.links.self !== this.props.selectedBackground.links.self
            )
         ) {
            this.props.downloadImage(this.props.selectedBackground);
        }

        if (prevProps.selectedGeneratedTemplate != this.props.selectedGeneratedTemplate &&
            (
                prevProps.selectedGeneratedTemplate == null || 
                prevProps.selectedGeneratedTemplate.links.self !== this.props.selectedGeneratedTemplate.links.self
            )
            && !this.isGeneratedTemplateReady(this.props.selectedGeneratedTemplate)
         ) {
            this.props.fetchGeneratedTemplate(this.props.selectedGeneratedTemplate);
        }
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
    return {
        selectedGeneratedTemplate: state.generatedTemplates.linkMap[state.puzzleSolverScreen.selectedGeneratedTemplate!],
        selectedBackground: state.backgrounds.linkMap[state.puzzleSolverScreen.selectedBackground!],
        downloadedImage: state.downloadedImages.linkMap[state.puzzleSolverScreen.selectedBackground!],
    };
}


const mapDispatchToProps = (dispatch: Function): DispatchProps => {
    return {
      downloadImage: (resource: Resource) => {
        dispatch(downloadedImagesActions.downloadImage(resource))
      },
      fetchGeneratedTemplate: (resource: Resource) => {
        dispatch(generatedTemplatesActions.fetchByLink(resource.links.self))
      }
    };
  }

  
const ConnectedLoadingDisplayWrapper = connect<StateProps, any, any>(
    mapStateToProps,
    mapDispatchToProps
)(LoadingDisplayWrapper);

export default function withLoadingWrapper<P>(BaseComponent: React.ComponentType<P>) {
    return (props: P) => (<ConnectedLoadingDisplayWrapper>
        <BaseComponent {...props}/>
    </ConnectedLoadingDisplayWrapper>);
}
