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
            <img src={puzzleBox} />
        </PlainLink>
        <PlainLink to={CUSTOM} className={path === CUSTOM ? styles.selected: ''}>
            <img src={flask} />
        </PlainLink>
    </div>
};

export default NavigationBar;