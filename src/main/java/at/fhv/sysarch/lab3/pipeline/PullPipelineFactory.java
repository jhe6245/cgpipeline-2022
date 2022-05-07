package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.pull.*;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec4;
import javafx.animation.AnimationTimer;

import java.util.ArrayDeque;
import java.util.Queue;

public class PullPipelineFactory {

    private static Mat4 modelSpaceToViewSpace(PipelineData pd, float rotationAngle) {
        return pd.getModelTranslation()
                .multiply(pd.getViewTransform())
                .multiply(Matrices.rotate(rotationAngle, pd.getModelRotAxis()));
    }

    public static AnimationTimer createPipeline(PipelineData pd) {

        Queue<Face> inputBuffer = new ArrayDeque<>();

        var source = new Source<Face>() {

            @Override
            public boolean hasNext() {
                return !inputBuffer.isEmpty();
            }

            @Override
            public Face next() {
                return inputBuffer.remove();
            }
        };

        var filterViewTransform = new TransformFilter(new Pipe<>(source), modelSpaceToViewSpace(pd, 0));

        // TODO 2. perform backface culling in VIEW SPACE

        // TODO 3. perform depth sorting in VIEW SPACE

        // TODO 4. add coloring (space unimportant)

        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            // 4a. TODO perform lighting in VIEW SPACE
        }

        var filterProjTransform = new TransformFilter(new Pipe<>(filterViewTransform), pd.getProjTransform());

        var filterPerspectiveDivision = new Filter<Face, Face>(new Pipe<>(filterProjTransform)) {

            @Override
            public boolean hasNext() {
                return input.hasNext();
            }

            @Override
            public Face next() {
                var face = input.next();
                return new Face(
                        div(face.getV1()), div(face.getV2()), div(face.getV3()),
                        div(face.getN1()), div(face.getN2()), div(face.getN3())
                );
            }

            Vec4 div(Vec4 v) {
                return v.multiply(1 / v.getZ());
            }

        };

        var filterViewportTransform = new TransformFilter(new Pipe<>(filterPerspectiveDivision), pd.getViewportTransform());

        // returning an animation renderer which handles clearing of the
        // viewport and computation of the fraction
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

                filterViewTransform.setTransform(modelSpaceToViewSpace(pd, rotationAngle));

                var graphics = pd.getGraphicsContext();
                graphics.setStroke(pd.getModelColor());
                graphics.setFill(pd.getModelColor());

                inputBuffer.addAll(model.getFaces());

                filterViewportTransform.forEachRemaining(f -> {

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
                            break;
                    }
                });

            }
        };
    }
}