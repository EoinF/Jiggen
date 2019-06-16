import React, { Component } from "react";

import { connect } from "react-redux";
import styles from '../PuzzleCard.module.scss';
import { StateRoot } from "../../../models";
import ProgressBar from "../ProgressBar/ProgressBar";
import { Template } from "../../../store/templates";
import { DownloadedTemplate } from "../../../store/downloadedTemplates";
import templateIconSrc from '../template-icon48x48.png';
import PieceCountDisplay from "../../PieceCountDisplay/PieceCountDisplay";
import TemplateDisplay from "../../TemplateDisplay/TemplateDisplay";

interface OwnProps {
    template: Template | null;
}

interface StateProps {
    downloadedTemplate: DownloadedTemplate | null;
}

type TemplateIconProps = OwnProps & StateProps;

class TemplateIcon extends Component<TemplateIconProps> {

    render() {
        return <div className={styles.templateSelectionContainer}>
            <div className={styles.iconFull}>
                <this.FullTemplateIcon/>
            </div>
            <div className={`${styles.icon} ${styles.iconMinimal}`}>
                <this.MinimalTemplateIcon/>
            </div>
            <this.ProgressBar/>
        </div>
    }

    FullTemplateIcon = () => {
        const {template} = this.props;
        return template != null ? <TemplateDisplay template={template} /> : <this.MinimalTemplateIcon/>
    }

    MinimalTemplateIcon = () => {
        const {template} = this.props;
        return <div className={`${styles.icon}`}>
            <img src={templateIconSrc} className={styles.iconImage}/>
            {
                template != null 
                && <PieceCountDisplay count={template.pieces}/> 
            }
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