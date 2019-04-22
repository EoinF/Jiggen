package com.github.eoinf.jiggen.webapp.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PixmapUtils {
    public static Pixmap stretchPixmap(Pixmap srcMap, GridPoint2 dstBounds) {
        Pixmap dstMap = new Pixmap(dstBounds.x, dstBounds.y, Pixmap.Format.RGBA8888);
        stretchPixmap(srcMap, new GridPoint2(0, 0), new GridPoint2(srcMap.getWidth(), srcMap.getHeight()), dstMap, dstBounds);
        return dstMap;
    }

    public static Pixmap stretchPixmap(Pixmap srcMap, GridPoint2 srcOffset, GridPoint2 srcBounds,
                                       Pixmap dstMap, GridPoint2 dstBounds) {
        dstMap.drawPixmap(srcMap,
                srcOffset.x, srcOffset.y,
                srcBounds.x, srcBounds.y,
                0, 0,
                dstBounds.x, dstBounds.y
        );
        return dstMap;
    }

    public static Vector2 getMinimumScaleToFixAspectRatio(int a, int b, int c, int d) {
        //
        // The variables s1 and s2 are the scales required to transform the aspect ratio
        // The variables a and b are the width and height of the current texture
        // The variables c and d are the width and height of the target texture
        // s1 * a(x) + s2 * b(y) = c(x) + d(y)
        //
        // ad >= bc  ->  s1 = 1
        // bc >= ad  ->  s2 = 1
        //
        //
        float s1; float s2;
        float ad = a * d;
        float bc = b * c;

        if (ad >= bc) {
            s1 = 1;
            s2 = ad / bc;
        } else {
            s2 = 1;
            s1 = bc / ad;
        }

        return new Vector2(s1, s2);
    }


    public static void setBackgroundColour(Table table, Color colour) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(colour);
        pixmap.fill();
        Drawable background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        table.setBackground(background);
        pixmap.dispose();
    }

}
