package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.pull.*;
import at.fhv.sysarch.lab3.utils.MatrixUtils;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec4;
import javafx.animation.AnimationTimer;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class PullPipelineFactory {

    private static Mat4 modelToViewTransform(PipelineData pd, float rotationAngle) {
        return pd.getModelTranslation()
                .multiply(pd.getViewTransform())
                .multiply(Matrices.rotate(rotationAngle, pd.getModelRotAxis()));
    }

    public static AnimationTimer createPipeline(PipelineData pd) {
        // TODO: pull from the source (model)

        final Model[] inputModel = {null};

        var source = new Source<Model>() {

            @Override
            public Model pull() {
                return inputModel[0];
            }
        };

        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates

        var filterViewTransform = new TransformFilter(new Pipe<>(source), modelToViewTransform(pd, 0));

        // TODO 2. perform backface culling in VIEW SPACE

        // TODO 3. perform depth sorting in VIEW SPACE

        // TODO 4. add coloring (space unimportant)

        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            // 4a. TODO perform lighting in VIEW SPACE
            
            // 5. TODO perform projection transformation on VIEW SPACE coordinates
        } else {
            // 5. TODO perform projection transformation
        }

        var filterProjTransform = new TransformFilter(new Pipe<>(filterViewTransform), pd.getProjTransform());

        // TODO 6. perform perspective division to screen coordinates
        var filterPerspectiveDivision = new PerFaceFilter(new Pipe<>(filterProjTransform)) {

            Vec4 div(Vec4 v) {
                return v.multiply(1 / v.getZ());
            }

            @Override
            protected Face processFace(Face face) {
                return new Face(
                        div(face.getV1()), div(face.getV2()), div(face.getV3()),
                        div(face.getN1()), div(face.getN2()), div(face.getN3())
                );
            }
        };

        var filterViewportTransform = new TransformFilter(new Pipe<>(filterPerspectiveDivision), pd.getViewportTransform());

        // TODO 7. feed into the sink (renderer)

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

                filterViewTransform.setTransform(modelToViewTransform(pd, rotationAngle));


                inputModel[0] = model;

                var graphics = pd.getGraphicsContext();
                graphics.setStroke(pd.getModelColor());
                graphics.setFill(pd.getModelColor());

                filterViewportTransform.pull().getFaces().forEach(f -> {

                    Supplier<Stream<Vec4>> vertices = () -> Stream.of(f.getV1(), f.getV2(), f.getV3());
                    var xs = vertices.get().mapToDouble(Vec4::getX).toArray();
                    var ys = vertices.get().mapToDouble(Vec4::getY).toArray();
                    final int n = 3;

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




                /*
                model.getFaces().forEach(f -> {
                    Stream.of(f.getV1(), f.getV2(), f.getV3()).forEach(vertex -> {
                        pd.getGraphicsContext().fillOval(vertex.getX() * 50 + 200, 200-vertex.getY() * 50, 1, 1);
                    });
                });
                */
            }
        };
    }
}