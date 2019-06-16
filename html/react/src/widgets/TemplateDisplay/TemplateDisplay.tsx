import React, { Component } from "react";
import { Template } from "../../store/templates";
import PieceCountDisplay from "../PieceCountDisplay/PieceCountDisplay";
import styles from './TemplateDisplay.module.scss';

interface TemplateDisplayProps {
    template: Template;
}

export default class TemplateDisplay extends Component<TemplateDisplayProps> {
    render() {
        const template = this.props.template;

        return <div className={styles.mainContainer}>
            <img src={template.links.image} className={styles.templateImage}/>
            <div> {
                template != null 
                    && <PieceCountDisplay count={template.pieces}/> 
            } </div>
        </div>;
    }
}