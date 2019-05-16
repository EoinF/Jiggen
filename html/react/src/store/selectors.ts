import { createSelector } from 'reselect';
import { StateRoot, StringMap } from '../models';
import { PlayablePuzzle } from './playablePuzzles';
import { Template } from './templates';

export const getSelectedTemplate = (state: StateRoot) => state.templates.linkMap[state.templates.selectedId!]

export const getTemplates = (state: StateRoot) => Object.values(state.templates.linkMap);

const getBasePlayablePuzzleLinkMap = (state: StateRoot) => state.playablePuzzles.linkMap;

export const getPlayablePuzzles = createSelector(
    [getBasePlayablePuzzleLinkMap],
    (playablePuzzleMap: StringMap<PlayablePuzzle> ) => {
      return Object.values(playablePuzzleMap);
    }
);


export const getSortedTemplates = createSelector(
  [getTemplates],
  (templates: Template[]) => {
    return templates.sort((a, b) => a.pieces - b.pieces);
  }
)