import { createSelector } from 'reselect';
import { StateRoot, StringMap } from '../models';
import { PlayablePuzzle } from './playablePuzzles';
import { GeneratedTemplate } from './generatedTemplates';
import { Background } from './backgrounds';

const getBasePlayablePuzzleList = (state: StateRoot) => state.playablePuzzles.resourceList;
const getBackgroundLinkMap = (state: StateRoot) => state.backgrounds.linkMap
const getGeneratedTemplateLinkMap = (state: StateRoot) => state.generatedTemplates.linkMap

export const getPlayablePuzzleList = createSelector(
    [getBasePlayablePuzzleList, getBackgroundLinkMap, getGeneratedTemplateLinkMap],
    (playablePuzzleList: PlayablePuzzle[], backgroundLinkMap: StringMap<Background>, generatedTemplateLinkMap: StringMap<GeneratedTemplate> ) => {
      return playablePuzzleList.map((puzzle: PlayablePuzzle): PlayablePuzzle => ({
          background: backgroundLinkMap[puzzle.links.background],
          generatedTemplate: generatedTemplateLinkMap[puzzle.links.generatedTemplate],
          ...puzzle
      }));
    }
);

export const getPieceCountMap = createSelector(
    [getPlayablePuzzleList],
    (playablePuzzleList) => {
        const pieceCountMap: StringMap<number> = {};
        playablePuzzleList.forEach((puzzle: PlayablePuzzle): any => {
            if (puzzle.generatedTemplate != null) {
                pieceCountMap[puzzle.generatedTemplate.id] = Object.keys(puzzle.generatedTemplate.vertices).length;
            }
          }
        );
        return pieceCountMap;
    }
)
