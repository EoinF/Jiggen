import React, { Component } from "react";
import { from, Subscription } from "rxjs";
import gwtAdapter from "../../gwtAdapter";
import styles from './GameContainer.module.scss';

import puzzlePieceIcon from './piece-outline-rounded.png';
import { Background } from "../../store/backgrounds";
import { StateRoot, Resource } from "../../models";
import { connect } from "react-redux";
import { DownloadedImage, downloadedImagesActions } from "../../store/downloadedImages";

interface LoadingDisplayState {
    isGWTLoading: Boolean;
}

interface DispatchProps {
    downloadImage(resource: Resource): void
}
interface StateProps {
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
                prevProps.selectedBackground.links.image !== this.props.selectedBackground.links.image
            )
         ) {
            this.props.downloadImage(this.props.selectedBackground);
        }
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
        if (this.state.isGWTLoading) {
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
        } else if (this.props.downloadedImage && this.props.downloadedImage.isDownloading) {
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
        } else {
            return this.props.children;
        }
    }
}

const mapStateToProps = (_state: any, ownProps: any): StateProps => {
    const state = (_state as StateRoot); // Required because we can't change type of _state
    return {
        selectedBackground: state.backgrounds.resourceMap[state.backgrounds.selectedId!],
        downloadedImage: state.downloadedImages.resourceMap[state.backgrounds.selectedId!]
    };
}


const mapDispatchToProps = (dispatch: Function): DispatchProps => {
    return {
      downloadImage: (resource: Resource) => {
        dispatch(downloadedImagesActions.downloadImage(resource))
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
