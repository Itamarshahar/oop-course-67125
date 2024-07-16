package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private final float groundHeightAtX0;
    private final String GROUND_TAG = "ground";
    private static final int TERRAIN_DEPTH = 20;
    private final NoiseGenerator noiseGenerator;
    private final Vector2 windowDimensions;
    public static final float RATIO = (float) (2.0 / 3.0);

    public Terrain(Vector2 windowDimensions, int seed) {

        this.groundHeightAtX0 = windowDimensions.y() * RATIO;
        this.windowDimensions = windowDimensions;
       this.noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
    }

    public float groundHeightAt(float x) {
        // TODO
        // לקראת ההגשה, על תוואי הקרקע להיות מעניין יותר. הנה דרך שלא תעבוד: גובה האדמה בכל
        //קואורדינטה יהיה אקראי. נכון, הקרקע תהיה אחרת בכל הרצה, אבל היא לא תהיה רציפה. דרך אחרת להפוך
        //את תוואי הקרקע לפחות צפוי היא פשוט להשתמש בהרכבה של פונקציות סינוס עם גדלים, זמני מחזור,
        //ופאזות שונים. כדי לקבל בכל פעם קרקע אחרת, אפשר להפוך את הפאזות לאקראיות.
        //הצורך בפונקציה שהיא אקראית למראה מחד, ורציפה מאידך, הוא צורך נפוץ. פונקציה כזו נקראת "פונקציית
        //Perlin . ורבים וטובות מימשו סוגים רבים של פונקציות כאלו ,)smooth noise-function( " רעש חלקה
        //הוא אלגוריתם פופולרי לפונקציה כזו. Noise
        //Perlin Noise שבעזרתה תוכלו לייצר NoiseGenerator בקבצים המסופקים לכם תוכלו למצוא את המחלקה
        //לפי התיעוד במחלקהגשד
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * 7);

        return groundHeightAtX0 + noise;
    }

    public List<Block> createInRange(int minX, int maxX) {

        List<Block> blockList = new ArrayList<>();
        minX -= (minX%Block.SIZE);
        for (int x = minX; x < maxX; x += Block.SIZE) {
            double topY = Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE;
            for (float y = windowDimensions.y(); y > topY; y -= Block.SIZE){
                Block block = new Block(Vector2.of(x, y),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                block.setTag(GROUND_TAG);
                blockList.add(block);
            }
        }
        return blockList;
    }

}
