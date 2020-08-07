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


const FullTemplateIcon = ({template}: OwnProps) => {
    return (template != null) ? <TemplateDisplay template={template} /> : <MinimalTemplateIcon template={template} />
}

const MinimalTemplateIcon = ({template}: OwnProps) => (
 <div className={`${styles.icon}`}>
        <img src={templateIconSrc} className={styles.iconImage} alt={'template'}/>
        {
            template != null
            && <PieceCountDisplay count={template.pieces}/>
        }
    </div>
);

const TemplateProgressBar = ({downloadedTemplate}: StateProps) => {
    if (downloadedTemplate == null) {
        return <ProgressBar
            current={0}
            total={1}
        />;
    } else if (downloadedTemplate != null && downloadedTemplate.isDownloading) {
        return <ProgressBar
            current={0}
            total={-1}
        />;
    } else {
        return null;
    }
}

class TemplateIcon extends Component<TemplateIconProps> {

    render() {
        return <div className={styles.templateSelectionContainer}>
            <div className={styles.iconFull}>
                <FullTemplateIcon template={this.props.template} />
            </div>
            <div className={`${styles.icon} ${styles.iconMinimal}`}>
                <MinimalTemplateIcon template={this.props.template} />
            </div>
            <TemplateProgressBar downloadedTemplate={this.props.downloadedTemplate}/>
        </div>
    }

}


const mapStateToProps = (_state: any, ownProps: OwnProps) : StateProps => {
    const state = _state as StateRoot;
    return {
        downloadedTemplate: ownProps.template && state.downloadedTemplates.linkMap[ownProps.template.links.self],
    };
}

export default connect<StateProps, any, OwnProps>(mapStateToProps)(TemplateIcon);