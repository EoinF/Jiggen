import React, { Component } from "react";

import { Background } from "../../../store/backgrounds";
import { connect } from "react-redux";
import { DownloadedImage } from "../../../store/downloadedImages";
import styles from '../PuzzleCard.module.scss';
import { StateRoot } from "../../../models";
import ProgressBar from "../ProgressBar/ProgressBar";

interface OwnProps {
    background: Background;
}

interface StateProps {
    downloadedBackground: DownloadedImage;
}

type BackgroundIconProps = OwnProps & StateProps;

class BackgroundIcon extends Component<BackgroundIconProps> {
    render() {
        const {background} = this.props;
        let backgroundSrc = '';
        if (background != null) {
            backgroundSrc = background.links['image-thumbnail48x48']
                || background.links['image-compressed']
                || background.links['image'];
        }

        return <div className={styles.icon} >
            <img className={styles.iconImage} src={backgroundSrc} />
            <this.ProgressBar/>
        </div>;
    }

    ProgressBar = () => {
        if (this.props.downloadedBackground == null) {
            return <ProgressBar
                current={0}
                total={1}
            />;
        } else if (this.props.downloadedBackground != null && this.props.downloadedBackground.isDownloading) {
            return <ProgressBar
                current={this.props.downloadedBackground.bytesDownloaded}
                total={this.props.downloadedBackground.bytesTotal}
            />;
        } else {
            return null;
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