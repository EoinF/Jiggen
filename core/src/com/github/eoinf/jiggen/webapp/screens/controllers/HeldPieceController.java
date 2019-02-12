package com.github.eoinf.jiggen.webapp.screens.controllers;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.webapp.graphics.WorldBoundedCamera;
import com.github.eoinf.jiggen.webapp.screens.models.ConnectedPuzzlePieces;
import com.github.eoinf.jiggen.webapp.screens.models.PuzzlePiece;
import com.github.eoinf.jiggen.webapp.screens.models.PuzzlePieceTemplate;

public class HeldPieceController {
    private static float PIECE_CONNECT_THRESHOLD = 15;

    private PuzzleViewModel puzzleViewModel;
    private PuzzleViewController puzzleViewController;

    public HeldPieceController(PuzzleViewModel puzzleViewModel, PuzzleViewController puzzleViewController, WorldBoundedCamera camera) {
        this.puzzleViewModel = puzzleViewModel;
        this.puzzleViewController = puzzleViewController;
    }

    private Vector2 tryConnect(ConnectedPuzzlePieces connectedPieces, ConnectedPuzzlePieces otherConnectedPieces) {
        Vector2 scales = puzzleViewModel.getScalesObservable().getValue();
        for (PuzzlePiece piece : connectedPieces.getConnectedPieces()) {
            for (PuzzlePiece otherPiece: otherConnectedPieces.getConnectedPieces()) {
                PuzzlePieceTemplate template = piece.getPuzzlePieceTemplate();
                PuzzlePieceTemplate otherTemplate = otherPiece.getPuzzlePieceTemplate();
                if (piece.getPuzzleGraphTemplate().hasEdge(template, otherTemplate)) {
                    return tryConnectIndividualPieces(piece, otherPiece, template, otherTemplate, scales);
                }
            }
        }
        return null;
    }

    private Vector2 tryConnectIndividualPieces(PuzzlePiece piece, PuzzlePiece otherPiece,
                                               PuzzlePieceTemplate template, PuzzlePieceTemplate otherTemplate,
                                               Vector2 scales) {
        float expectedXDelta = (template.x() - otherTemplate.x()) * scales.x;
        float expectedYDelta = (template.y() - otherTemplate.y()) * scales.y;
        float currentXDelta = piece.x() - otherPiece.x();
        float currentYDelta = piece.y() - otherPiece.y();
        float errorX = expectedXDelta - currentXDelta;
        float errorY = expectedYDelta - currentYDelta;

        if (Math.abs(errorX) + Math.abs(errorY) < PIECE_CONNECT_THRESHOLD) {
            return new Vector2(errorX, errorY);
        }
        return null;
    }


    private Vector2 fitInWorldBounds(Vector2 position, float width, float height) {
        Vector2 newPosition = position.cpy();
        GridPoint2 worldBounds = puzzleViewModel.getWorldBoundsObservable().getValue();
        if (newPosition.x + width > worldBounds.x) {
            newPosition.x = worldBounds.x - width;
        } else if (newPosition.x < 0) {
            newPosition.x = 0;
        }
        if (newPosition.y + height > worldBounds.y) {
            newPosition.y = worldBounds.y - height;
        } else if (newPosition.y < 0) {
            newPosition.y = 0;
        }
        return newPosition;
    }

    void dropPiece() {
        ConnectedPuzzlePieces connectedPieces = puzzleViewModel.getHeldPieceObservable().getValue().getPiecesHeld();
        if (connectedPieces != null) {
            for (ConnectedPuzzlePieces otherConnectedPieces : puzzleViewModel.getConnectedPiecesListObservable().getValue()) {
                if (connectedPieces != otherConnectedPieces) {
                    Vector2 error = tryConnect(connectedPieces, otherConnectedPieces);
                    if (error != null) {
                        puzzleViewController.combineConnectedPieces(connectedPieces, otherConnectedPieces, error);
                        puzzleViewModel.setHeldPiece(null, null);
                        return;
                    }
                }
            }

            Vector2 newPosition = fitInWorldBounds(connectedPieces.getPosition(),
                    connectedPieces.getWidth(), connectedPieces.getHeight());

            puzzleViewController.setGroupPosition(connectedPieces, newPosition);
            puzzleViewModel.setHeldPiece(null, null);
        }
    }
}
