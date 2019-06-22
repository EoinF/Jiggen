import React from 'react';
import styles from './ProgressBar.module.scss';
import downloadIcon from '../../../assets/download-icon.png';

interface ProgressBarProps {
    current: number;
    total: number;
}

const ProgressBar = ({current, total}: ProgressBarProps) => {
    let barContents;
    if (total === -1) {
        barContents = <div className={styles.progressBarInnerIndeterminate} />
    } else {
        const percentDownloaded = 100 * current / total;
        barContents = <div className={styles.progressBarInner} style={{width: `${percentDownloaded}%`}} />
    }
    return <div className={styles.progressBarContainer}>
        <div className={styles.imageContainer}>
            <img className={styles.downloadIcon} src={downloadIcon} alt="downloading..." />
        </div>
        <div className={styles.progressBar}>
            {barContents}
        </div>
    </div>;
}

export default ProgressBar;