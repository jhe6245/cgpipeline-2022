package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.pull.*;
import at.fhv.sysarch.lab3.pipeline.pull.implementations.FaceColorPairTransformFilter;
import at.fhv.sysarch.lab3.pipeline.pull.implementations.FaceTransformFilter;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

import java.util.*;

public class PullPipelineFactory {



    public static AnimationTimer createPipeline(PipelineData pd) {

        Queue<Face> inputBuffer = new ArrayDeque<>();

        var faceSource = new Source<Face>() {

            @Override
            public boolean hasNext() {
                return !inputBuffer.isEmpty();
            }

            @Override
            public Face next() {
                return inputBuffer.remove();
            }
        };

        var filterViewTransform = new FaceTransformFilter(new Pipe<>(faceSource), Util.modelSpaceToViewSpace(pd, 0));


        var filterBackfaceCulling = new Filter<Face, Face>(new Pipe<>(filterViewTransform)) {

            Face f;

            @Override
            public Face next() {
                if(f != null) {
                    try { return f; }
                    finally { f = null; }
                }

                while(input.hasNext()) {
                    var i = input.next();
                    if(Util.facesCamera(i)) {
                        return i;
                    }
                }

                throw new NoSuchElementException();
            }

            @Override
            public boolean hasNext() {
                if(f != null)
                    return true;

                while(input.hasNext()) {
                    var i = input.next();
                    if(Util.facesCamera(i)) {
                        f = i;
                        return true;
                    }
                }
                return false;
            }
        };

        var filterPaintersAlgorithm = new Filter<Face, Face>(new Pipe<>(filterBackfaceCulling)) {
            final Queue<Face> faces = new PriorityQueue<>(Comparator.comparing(Util::averageZ));

            @Override
            public Face next() {
                input.forEachRemaining(faces::add);

                return faces.remove();
            }

            @Override
            public boolean hasNext() {
                input.forEachRemaining(faces::add);

                return !faces.isEmpty();
            }
        };

        var addColor = new Filter<Face, Pair<Face, Color>>(new Pipe<>(filterPaintersAlgorithm)) {
            @Override
            public Pair<Face, Color> next() {
                return new Pair<>(input.next(), pd.getModelColor());
            }
        };

        Pipe<Pair<Face, Color>> pipeLighting = new Pipe<>(addColor);

        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            pipeLighting = new Pipe<>(new Filter<>(pipeLighting) {
                @Override
                public Pair<Face, Color> next() {
                    var i = input.next();
                    var f = i.fst();
                    var c = i.snd();
                    var alpha = f.getN1().toVec3().getUnitVector().dot(pd.getLightPos().getUnitVector());
                    alpha = Math.max(0, alpha);
                    return new Pair<>(f, Color.BLACK.interpolate(c, alpha));
                }
            });
        }

        var filterProjTransform = new FaceColorPairTransformFilter(pipeLighting, pd.getProjTransform());

        var filterPerspectiveDivision = new Filter<Pair<Face, Color>, Pair<Face, Color>>(new Pipe<>(filterProjTransform)) {
            @Override
            public Pair<Face, Color> next() {
                var n = input.next();
                return new Pair<>(Util.perspectiveDivision(n.fst()),n.snd());
            }
        };

        var filterViewportTransform = new FaceColorPairTransformFilter(new Pipe<>(filterPerspectiveDivision), pd.getViewportTransform());

        return new AnimationRenderer(pd) {
            private float rotationAngle = 0;

            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer). 
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render 
             */
            @Override
            protected void render(float fraction, Model model) {

                rotationAngle += fraction;

                filterViewTransform.setTransform(Util.modelSpaceToViewSpace(pd, rotationAngle));

                var graphics = pd.getGraphicsContext();

                inputBuffer.addAll(model.getFaces());

                filterViewportTransform.forEachRemaining(p -> {

                    var f = p.fst();

                    graphics.setStroke(p.snd());
                    graphics.setFill(p.snd());

                    final int n = 3;
                    var xs = new double[] { f.getV1().getX(), f.getV2().getX(), f.getV3().getX() };
                    var ys = new double[] { f.getV1().getY(), f.getV2().getY(), f.getV3().getY() };

                    switch (pd.getRenderingMode()) {
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
        };
    }
}