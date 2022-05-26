package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.pull.*;
import at.fhv.sysarch.lab3.pipeline.pull.implementations.*;
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

        var viewTransform = new FaceTransformFilter(new Pipe<>(faceSource), Util.modelSpaceToViewSpace(pd, 0));

        var backfaceCulling = new BackfaceCulling(new Pipe<>(viewTransform));

        var depthSorting = new DepthSorting(new Pipe<>(backfaceCulling));

        var addColor = new AddColor(new Pipe<>(depthSorting), pd.getModelColor());

        Pipe<Pair<Face, Color>> pipeLighting = new Pipe<>(addColor);

        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            pipeLighting = new Pipe<>(new Lighting(pipeLighting, pd.getLightPos()));
        }

        var projTransform = new FaceColorPairTransformFilter(pipeLighting, pd.getProjTransform());

        var perspectiveDivision = new PerspectiveDivision(new Pipe<>(projTransform));

        var filterViewportTransform = new FaceColorPairTransformFilter(new Pipe<>(perspectiveDivision), pd.getViewportTransform());

        var renderer = new Renderer(new Pipe<>(filterViewportTransform), pd.getGraphicsContext(), pd.getRenderingMode());

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

                viewTransform.setTransform(Util.modelSpaceToViewSpace(pd, rotationAngle));

                inputBuffer.addAll(model.getFaces());
                renderer.renderAll();
            }
        };
    }
}