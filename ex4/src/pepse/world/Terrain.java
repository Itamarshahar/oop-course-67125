package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.List;

public class Terrain {
    private float groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    public Terrain(Vector2 windowDimensions, int seed){
        this.groundHeightAtX0 = windowDimensions.x()*(2/3); // TODO verify
        // logic

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
        //לפי התיעוד במחלקהגשדs
        return groundHeightAtX0; }
//    public List<Block> createInRange(int minX, int maxX) {
//        Block block = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
//        block.setTag("ground");
//
//
//    }

}
