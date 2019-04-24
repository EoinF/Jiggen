import { createSelector } from 'reselect';
import { StateRoot, StringMap } from '../models';
import { PlayablePuzzle } from './playablePuzzles';
import { Background } from './backgrounds';

export const getSelectedTemplate = (state: StateRoot) => state.templates.linkMap[state.templates.selectedId!]

const getBasePlayablePuzzleLinkMap = (state: StateRoot) => state.playablePuzzles.linkMap;

export const getPlayablePuzzles = createSelector(
    [getBasePlayablePuzzleLinkMap],
    (playablePuzzleMap: StringMap<PlayablePuzzle> ) => {
      return Object.values(playablePuzzleMap);
    }
);
