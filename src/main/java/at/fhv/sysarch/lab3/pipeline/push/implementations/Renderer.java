package at.fhv.sysarch.lab3.pipeline.push.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.push.Sink;
import at.fhv.sysarch.lab3.rendering.RenderingMode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayDeque;
import java.util.Queue;

public class Renderer implements Sink<Pair<Face, Color>> {

    
    private final Queue<Pair<Face, Color>> queue = new ArrayDeque<>();

    private final GraphicsContext graphics;
    private final RenderingMode mode;


    public Renderer(GraphicsContext graphics, RenderingMode mode) {
        this.graphics = graphics;
        this.mode = mode;
    }

    @Override
    public void push(Pair<Face, Color> item) {
        var f = item.fst();
        var c = item.snd();

        graphics.setStroke(c);
        graphics.setFill(c);

        final int n = 3;
        var xs = new double[] { f.getV1().getX(), f.getV2().getX(), f.getV3().getX() };
        var ys = new double[] { f.getV1().getY(), f.getV2().getY(), f.getV3().getY() };

        switch (mode) {
            case POINT:
                for(int i = 0; i < n; i++)
                    graphics.strokeLine(xs[i], ys[i], xs[i], ys[i]);
                break;

            case WIREFRAME:
                graphics.strokePolygon(xs, ys, n);
                break;

            case FILLED:
                graphics.fillPolygon(xs, ys, n);
                graphics.strokePolygon(xs, ys, n);
                break;
        }
    }

    @Override
    public void flush() {
    }
}
