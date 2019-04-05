import { createSelector } from 'reselect';
import { StateRoot, StringMap } from '../models';
import { PlayablePuzzle } from './playablePuzzles';
import { GeneratedTemplate } from './generatedTemplates';
import { Background } from './backgrounds';

export const getSelectedTemplate = (state: StateRoot) => state.templates.linkMap[state.templates.selectedId!]

const getBasePlayablePuzzleList = (state: StateRoot) => state.playablePuzzles.resourceList;
const getBackgroundLinkMap = (state: StateRoot) => state.backgrounds.linkMap

const getGeneratedTemplateList = (state: StateRoot) => state.generatedTemplates.resourceList;
const getGeneratedTemplateLinkMap = (state: StateRoot) => state.generatedTemplates.linkMap;

export const getPlayablePuzzleList = createSelector(
    [getBasePlayablePuzzleList, getBackgroundLinkMap, getGeneratedTemplateLinkMap],
    (playablePuzzleList: PlayablePuzzle[], backgroundLinkMap: StringMap<Background>, generatedTemplateLinkMap: StringMap<GeneratedTemplate> ) => {
      return playablePuzzleList;
    }
);

export const getPieceCountMap = createSelector(
    [getPlayablePuzzleList, getGeneratedTemplateLinkMap],
    (playablePuzzleList, generatedTemplateLinkMap) => {
        const pieceCountMap: StringMap<number> = {};
        playablePuzzleList.forEach((puzzle: PlayablePuzzle): any => {
            const generatedTemplate = generatedTemplateLinkMap[puzzle.links.generatedTemplate];
            if (generatedTemplate != null) {
                pieceCountMap[puzzle.links.self] = Object.keys(generatedTemplate.vertices).length;
            }
          }
        );
        return pieceCountMap;
    }
)

export const getGeneratedTemplatesForTemplate = createSelector(
    [getSelectedTemplate, getGeneratedTemplateList],
    (selectedTemplate, generatedTemplates) => {
        if (selectedTemplate != null) {
            return generatedTemplates
                .filter((generatedTemplate: GeneratedTemplate) => 
                    generatedTemplate.links.templateFile == selectedTemplate.links.self);
        } else {
            return [];
        }
    })
