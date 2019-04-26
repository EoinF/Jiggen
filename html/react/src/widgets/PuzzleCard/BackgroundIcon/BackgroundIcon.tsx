import React, { Component } from "react";

import { Background } from "../../../store/backgrounds";
import { connect } from "react-redux";
import { DownloadedImage, downloadedImagesActions } from "../../../store/downloadedImages";
import styles from '../PuzzleCard.module.scss';
import { StateRoot } from "../../../models";

interface OwnProps {
    background: Background;
}

interface StateProps {
    downloadedBackground: DownloadedImage;
}

type BackgroundIconProps = OwnProps & StateProps;

class BackgroundIcon extends Component<BackgroundIconProps> {

    render() {
        const {background, downloadedBackground} = this.props;
        let backgroundSrc = '';
        if (background != null) {
            backgroundSrc = background.links['image-thumbnail48x48']
                || background.links['image-compressed']
                || background.links['image'];
        }

        return <div className={styles.icon} >
            <img src={backgroundSrc} />
            <ProgressBar downloadedBackground={downloadedBackground}/>
        </div>;
    }
}

interface ProgressBarProps {
    downloadedBackground: DownloadedImage;
}

const ProgressBar = ({downloadedBackground}: ProgressBarProps) => {
    if (downloadedBackground == null || !downloadedBackground.isDownloading) {
        return null;
    } else {
        if (downloadedBackground != null && downloadedBackground.bytesTotal === -1) {
            return <div>
                Loading...
            </div>
        } else {
            const percentDownloaded = 100 * downloadedBackground.bytesDownloaded / downloadedBackground.bytesTotal;
            return <div className={styles.progressBarContainer}>
                <div className={styles.progressBar}>
                    <div className={styles.progressBarInner} style={{width: `${percentDownloaded}%`}} />
                </div>
            </div>
        }
    }
}

const mapStateToProps = (_state: any, ownProps: OwnProps) : StateProps => {
    const state = _state as StateRoot;
    return {
        downloadedBackground: ownProps.background && state.downloadedImages.linkMap[ownProps.background.links.self],
    };
}

export default connect<StateProps, any, OwnProps>(mapStateToProps)(BackgroundIcon);