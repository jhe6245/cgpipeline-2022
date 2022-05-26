package at.fhv.sysarch.lab3.pipeline.pull.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.pull.Source;
import at.fhv.sysarch.lab3.rendering.RenderingMode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer {
    private final Source<Pair<Face, Color>> source;
    private final GraphicsContext graphics;
    private final RenderingMode mode;

    public Renderer(Source<Pair<Face, Color>> source, GraphicsContext graphics, RenderingMode mode) {
        this.source = source;
        this.graphics = graphics;
        this.mode = mode;
    }

    public void renderAll() {
        source.forEachRemaining(p -> {

            var f = p.fst();

            graphics.setStroke(p.snd());
            graphics.setFill(p.snd());

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
        });
    }
}
