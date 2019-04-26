import React, { Component } from "react";

import { connect } from "react-redux";
import styles from '../PuzzleCard.module.scss';
import { StateRoot } from "../../../models";
import ProgressBar from "../ProgressBar/ProgressBar";
import { Template } from "../../../store/templates";
import { DownloadedTemplate } from "../../../store/downloadedTemplates";
import templateIconSrc from '../template-icon48x48.png';
import PieceCountDisplay from "../../PieceCountDisplay/PieceCountDisplay";

interface OwnProps {
    template: Template;
}

interface StateProps {
    downloadedTemplate: DownloadedTemplate;
}

type TemplateIconProps = OwnProps & StateProps;

class TemplateIcon extends Component<TemplateIconProps> {

    render() {
        const {template} = this.props;
        

        return <div className={`${styles.icon} ${styles.templateSelectionContainer}`}>
            <img src={templateIconSrc} className={styles.iconImage}/>
            {
                template != null 
                && <PieceCountDisplay count={template.pieces}/> 
            }
            <this.ProgressBar/>
        </div>;
    }

    ProgressBar = () => {
        if (this.props.downloadedTemplate == null) {
            return <ProgressBar
                current={0}
                total={1}
            />;
        } else if (this.props.downloadedTemplate != null && this.props.downloadedTemplate.isDownloading) {
            return <ProgressBar
                current={0}
                total={-1}
            />;
        } else {
            return null;
        }
    }
}


const mapStateToProps = (_state: any, ownProps: OwnProps) : StateProps => {
    const state = _state as StateRoot;
    return {
        downloadedTemplate: ownProps.template && state.downloadedTemplates.linkMap[ownProps.template.links.self],
    };
}

export default connect<StateProps, any, OwnProps>(mapStateToProps)(TemplateIcon);