package com.github.eoinf.jiggen.views.Screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.github.eoinf.jiggen.views.PuzzlePieceGroup;
import io.reactivex.functions.Consumer;

public class PuzzleView extends Group {
    private HeldPuzzlePiece heldPuzzlePiece;

    public PuzzleView(PuzzleViewModel puzzleViewModel) {
        puzzleViewModel.getHeldPieceObservable().subscribe(new Consumer<HeldPuzzlePiece>() {
            @Override
            public void accept(HeldPuzzlePiece acceptedPiece) {
                heldPuzzlePiece = acceptedPiece;
            }
        });
        puzzleViewModel.getPuzzlePieceAddedObservable().subscribe(new Consumer<PuzzlePieceGroup>() {
            @Override
            public void accept(PuzzlePieceGroup pieceGroup) {
                addActor(pieceGroup);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
