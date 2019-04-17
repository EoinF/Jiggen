import React from 'react';

import flask from './flaskC.png'
import puzzleBox from './puzzleBoxC.png';

import styles from './NavigationBar.module.scss';
import PlainNavLink from '../PlainNavLink/PlainNavLink';
import { RouteComponentProps } from 'react-router';
import { NavIcon } from './NavIcon';
import CurrentPuzzleNavIcon from './CurrentPuzzleNavIcon';

const PUZZLE_OF_THE_DAY = '/daily';
const CUSTOM = '/custom';

type NavigationBarProps = RouteComponentProps;

const NavigationBar = ({location}: NavigationBarProps) => {
    if ([PUZZLE_OF_THE_DAY, CUSTOM].find(p => p === location.pathname) != null) {
        return <div className={styles.fakeContainer}>
            <div className={styles.mainContainer}>
                <CurrentPuzzleNavIcon />
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

export default NavigationBar;