import { createSelector } from 'reselect';
import { StateRoot, StringMap } from '../models';
import { PlayablePuzzle } from './playablePuzzles';
import { GeneratedTemplate } from './generatedTemplates';
import { Background } from './backgrounds';

export const getSelectedTemplate = (state: StateRoot) => state.templates.linkMap[state.templates.selectedId!]

const getBasePlayablePuzzleLinkMap = (state: StateRoot) => state.playablePuzzles.linkMap;
const getBackgroundLinkMap = (state: StateRoot) => state.backgrounds.linkMap;

const getGeneratedTemplateLinkMap = (state: StateRoot) => state.generatedTemplates.linkMap;

export const getPlayablePuzzleMap = createSelector(
    [getBasePlayablePuzzleLinkMap, getBackgroundLinkMap, getGeneratedTemplateLinkMap],
    (playablePuzzleMap: StringMap<PlayablePuzzle>, backgroundLinkMap: StringMap<Background>, generatedTemplateLinkMap: StringMap<GeneratedTemplate> ) => {
      return playablePuzzleMap;
    }
);

export const getPieceCountMap = createSelector(
    [getPlayablePuzzleMap, getGeneratedTemplateLinkMap],
    (playablePuzzleList, generatedTemplateLinkMap) => {
        const pieceCountMap: StringMap<number> = {};
        Object.values(playablePuzzleList).forEach((puzzle: PlayablePuzzle): any => {
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
    [getSelectedTemplate, getGeneratedTemplateLinkMap],
    (selectedTemplate, generatedTemplates) => {
        if (selectedTemplate != null) {
            return Object.values(generatedTemplates)
                .filter((generatedTemplate: GeneratedTemplate) => 
                    generatedTemplate.links.templateFile == selectedTemplate.links.self);
        } else {
            return [];
        }
    })
