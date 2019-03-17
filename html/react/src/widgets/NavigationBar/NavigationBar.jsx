import React from 'react';
import PlainLink from '../PlainLink';

import flask from './flaskC.png'
import puzzleBox from './puzzleBoxC.png';

import styles from './NavigationBar.module.scss';

const PUZZLE_OF_THE_DAY = '/puzzlesOfTheDay';
const CUSTOM = '/custom';

const NavigationBar = ({location}) => {
    const path = location.pathname;
    return <div className={styles.mainContainer}>
        <PlainLink to={PUZZLE_OF_THE_DAY} className={path === PUZZLE_OF_THE_DAY ? styles.selected: ''}>
            <NavIcon imgSrc={puzzleBox} labelText="Daily" />
        </PlainLink>
        <PlainLink to={CUSTOM} className={path === CUSTOM ? styles.selected: ''}>
            <NavIcon imgSrc={flask} labelText="Custom" />
        </PlainLink>
    </div>
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