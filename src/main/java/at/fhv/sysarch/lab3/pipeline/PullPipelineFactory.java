package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.pull.*;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec4;
import javafx.animation.AnimationTimer;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class PullPipelineFactory {
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
        var applyViewTransform = new TransformFilter(new Pipe<>(source), pd.getViewTransform());

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

        // TODO 6. perform perspective division to screen coordinates

        var applyProjTransform = new TransformFilter(new Pipe<>(applyViewTransform), pd.getProjTransform());

        var applyPerspectiveDivision = new ByFaceFilter(new Pipe<>(applyProjTransform)) {

            Vec4 div(Vec4 v) {
                return new Vec4(
                        v.getX() / v.getW(),
                        v.getY() / v.getW(),
                        v.getZ() / v.getW(),
                        1
                );
            }

            @Override
            protected Face processFace(Face face) {
                return new Face(
                        div(face.getV1()),
                        div(face.getV2()),
                        div(face.getV3()),
                        div(face.getN1()),
                        div(face.getN2()),
                        div(face.getN3())
                );
            }
        };

        var applyViewportTransform = new TransformFilter(new Pipe<>(applyPerspectiveDivision), pd.getViewportTransform());

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
                // TODO compute rotation in radians
                rotationAngle += fraction;

                // TODO create new model rotation matrix using pd.getModelRotAxis and Matrices.rotate
                Mat4 rotationMatrix = Matrices.rotate(rotationAngle, pd.getModelRotAxis());

                // TODO compute updated model-view transformation


                // TODO update model-view filter

                // TODO trigger rendering of the pipeline

                inputModel[0] = model;

                var graphics = pd.getGraphicsContext();
                graphics.setStroke(pd.getModelColor());
                graphics.setFill(pd.getModelColor());

                applyViewportTransform.pull().getFaces().forEach(f -> {

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