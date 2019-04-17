import React from 'react';
import styles from './NavigationBar.module.scss';

interface NavIconProps {
    imgSrc: string;
    labelText: string;
}

export const NavIcon = ({imgSrc, labelText}: NavIconProps) => (
    <div className={styles.navButton}>
        <div className={styles.imageContainer}>
            <img src={imgSrc} width="48px" height="48px"/>
        </div>
        <label>{labelText}</label>
    </div>
)
