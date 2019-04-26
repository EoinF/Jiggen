import React, { Component, ChangeEvent } from 'react';
import { connect } from 'react-redux';

import styles from './CreatePuzzlePage.module.scss';

import SelectionWidget from './SelectionWidget';

import templateLogo from '../../assets/template-icon.png';
import backgroundLogo from './Background-icon.png';
import { Background, backgroundsActions } from '../../store/backgrounds';
import { StateRoot } from '../../models';
import { displayOptionsActions } from '../../store/displayOptions';
import { Template, templatesActions } from '../../store/templates';
import { PlainLink, ResponsiveImage } from '../../widgets';
import { customPuzzleActions, CustomPuzzle } from '../../store/customPuzzle';
import { RouteComponentProps } from 'react-router';
import TemplateWidget from '../../widgets/TemplateWidget/TemplateWidget';
import PieceCountDisplay from '../../widgets/PieceCountDisplay/PieceCountDisplay';

interface StateProps {
  customPuzzle: CustomPuzzle;
  selectedTemplate: Template;
  selectedBackground: Background;
}

interface DispatchProps {
  showBackgroundsModal(): void;
  showTemplatesModal(): void;
  setName(name: string): void;
  saveCustomPuzzle(): void;
  selectCustomPuzzle(id: string | null): void;
  fetchBackgroundByLink(link: string): void;
  fetchTemplateByLink(link: string): void;
}

interface CreatePuzzlePageRouteParams {
  id: string;
}

type CreatePuzzlePageProps = StateProps & DispatchProps & RouteComponentProps<CreatePuzzlePageRouteParams>;

class CreatePuzzlePage extends Component<CreatePuzzlePageProps> {

  componentDidMount() {
    const params = this.props.match.params;
    if (params.id !== 'new') {
      this.props.selectCustomPuzzle(params.id);
    } else {
      this.props.selectCustomPuzzle(null);
    }
  }

  componentDidUpdate = (prevProps: CreatePuzzlePageProps) => {
    if (this.props.customPuzzle.template != null && this.props.customPuzzle.template != prevProps.customPuzzle.template) {
      this.props.fetchTemplateByLink(this.props.customPuzzle.template);
    }
    if (this.props.customPuzzle.background != null && this.props.customPuzzle.background != prevProps.customPuzzle.background) {
      this.props.fetchBackgroundByLink(this.props.customPuzzle.background);
    }
  }

  onChangeName = (e: ChangeEvent<HTMLInputElement>) => {
    this.props.setName(e.target.value)
  }

  render() {
    const {
      customPuzzle,
      selectedTemplate,
      selectedBackground
    } = this.props;

    const isReady = selectedTemplate != null 
      && selectedBackground != null
      && customPuzzle.name.length > 0;

    return (
        <div className={styles.mainContainer}>
        <div className={styles.contentContainer}>
            <div className={styles.inputField}>
              <div className={styles.inputContainer}>
                <input 
                  maxLength={24}
                  value={customPuzzle.name}
                  onChange={this.onChangeName}
                />
              </div>
            </div>
            <div className={styles.selectionContainer}>
                <SelectionWidget
                  selection={selectedTemplate}
                  fallbackImageSrc={templateLogo}
                  notSelectedCaption='Choose a Template'
                  onClick={this.props.showTemplatesModal}
                >
                  {
                    selectedTemplate && <div className={styles.templateSelectionContainer}>
                      <ResponsiveImage 
                        src={selectedTemplate.links['image-compressed'] || selectedTemplate.links.image} 
                        alt={selectedTemplate.name}
                      />
                      <PieceCountDisplay count={selectedTemplate.pieces} />
                    </div>
                  }
                </SelectionWidget>
                <SelectionWidget
                  selection={selectedBackground}
                  fallbackImageSrc={backgroundLogo}
                  notSelectedCaption='Choose a Background'
                  onClick={this.props.showBackgroundsModal}
                >
                  {selectedBackground && <ResponsiveImage 
                      src={selectedBackground.links['image-compressed'] || selectedBackground.links.image} 
                      alt={selectedBackground.name}
                    />
                  }
                </SelectionWidget>
            </div>
            <div className={styles.buttonControls}>
              { isReady ? ( 
                <PlainLink to="/custom">
                  <button onClick={this.props.saveCustomPuzzle} className={styles.save}>✓</button>
              </PlainLink> ) : (
                <button disabled onClick={this.props.saveCustomPuzzle} className={styles.save}>✓</button>
                )
              }
              <PlainLink to="/custom">
                <button className={styles.cancel}>X</button>
              </PlainLink>
            </div>
          </div>
        </div>
    );
  }
}

const mapStateToProps = (state: StateRoot): StateProps => {
  return {
    customPuzzle: state.customPuzzle.currentPuzzle,
    selectedTemplate: state.templates.linkMap[state.customPuzzle.currentPuzzle.template!],
    selectedBackground: state.backgrounds.linkMap[state.customPuzzle.currentPuzzle.background!]
  };
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
    setName: (name: string) => dispatch(customPuzzleActions.setName(name)),
    selectCustomPuzzle: (id: string) => dispatch(customPuzzleActions.selectPuzzle(id)),
    showBackgroundsModal: () => dispatch(displayOptionsActions.showBackgroundsModal()),
    showTemplatesModal: () => dispatch(displayOptionsActions.showTemplatesModal()),
    saveCustomPuzzle: () => dispatch(customPuzzleActions.savePuzzle()),
    fetchBackgroundByLink: (link: string) => dispatch(backgroundsActions.fetchByLink(link)),
    fetchTemplateByLink: (link: string) => dispatch(templatesActions.fetchByLink(link))
  }
}

export default  connect(
  mapStateToProps,
  mapDispatchToProps
)(CreatePuzzlePage);
