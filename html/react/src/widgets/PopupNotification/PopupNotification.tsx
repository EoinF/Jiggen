import React, { Component } from "react";
import styles from './PopupNotification.module.scss';

class PopupNotification extends Component {
    state = {
        isVisible: true
    }

    onClick = () => {
        this.setState({ isVisible: false });
    }
    render() {
        if (this.state.isVisible) {
            return <div className={styles.mainContainer} onClick={this.onClick}>
                <div className={styles.popupContainer}>{this.props.children}</div>
            </div>
        } else {
            return null;
        }
    }
}

export default PopupNotification;