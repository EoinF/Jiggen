import React from 'react';

import flask from './flaskC.png'
import puzzleBox from './puzzleBoxC.png';

import styles from './NavigationBar.module.scss';
import PlainNavLink from '../PlainNavLink/PlainNavLink';

const PUZZLE_OF_THE_DAY = '/daily';
const CUSTOM = '/custom';

const NavigationBar = ({location}) => {
    if ([PUZZLE_OF_THE_DAY, CUSTOM].find(p => p === location.pathname) != null) {
        return <div className={styles.fakeContainer}>
            <div className={styles.mainContainer}>
                <PlainNavLink to={PUZZLE_OF_THE_DAY} activeClassName={styles.selected}>
                    <NavIcon imgSrc={puzzleBox} labelText="Daily" />
                </PlainNavLink>
                <PlainNavLink to={CUSTOM} activeClassName={styles.selected}>
                    <NavIcon imgSrc={flask} labelText="Custom" />
                </PlainNavLink>
            </div>
        </div>
    } else {
        return null;
    }
};

const NavIcon = ({imgSrc, labelText}) => (
    <div className={styles.navButton}>
        <div className={styles.imageContainer}>
            <img src={imgSrc} width="48px" height="48px"/>
        </div>
        <label>{labelText}</label>
    </div>
)

export default NavigationBar;