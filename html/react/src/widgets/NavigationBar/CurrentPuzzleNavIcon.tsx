import React from 'react';
import styles from './NavigationBar.module.scss';

import { connect } from "react-redux";
import { StateRoot } from "../../models";
import { Background } from '../../store/backgrounds';
import PlainNavLink from '../PlainNavLink/PlainNavLink';

interface StateProps {
    selectedBackground: Background;
}

type CurrentPuzzleNavIconProps = StateProps;

const CurrentPuzzleNavIcon = ({selectedBackground}: CurrentPuzzleNavIconProps) => {
    if (selectedBackground != null) {
        const imgSrc = selectedBackground.links['image-thumbnail48x48'] ||
            selectedBackground.links['image-compressed'] ||
            selectedBackground.links.image;
        return <PlainNavLink to={'/play'} activeClassName={styles.selected}>
            <div className={styles.currentPuzzleNavButton}>
                <div className={styles.imageMargin}/>
                <img src={imgSrc}/> 
                <label>Play</label>
            </div>
        </PlainNavLink>
    } else {
        return null
    }
}
const mapStateToProps = (_state: any): StateProps => {
    const state = _state as StateRoot;
    return {
        selectedBackground: state.backgrounds.linkMap[state.puzzleSolverScreen.selectedBackground!]
    }
}

export default connect<StateProps>(mapStateToProps)(CurrentPuzzleNavIcon);
