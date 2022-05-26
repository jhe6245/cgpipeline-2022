package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.push.Filter;
import at.fhv.sysarch.lab3.pipeline.push.Pipe;
import at.fhv.sysarch.lab3.pipeline.push.implementations.*;
import javafx.animation.AnimationTimer;

public class PushPipelineFactory {

    public static AnimationTimer createPipeline(PipelineData pd) {

        var input = new Filter<Model, Face>() {
            @Override
            public void push(Model item) {
                item.getFaces().forEach(output::push);
                output.flush();
            }
        };

        var modelSpaceToViewSpace = new FaceTransformFilter(Util.modelSpaceToViewSpace(pd, 0));
        input.setOutput(new Pipe<>(modelSpaceToViewSpace));

        var backfaceCulling = new BackfaceCulling();
        modelSpaceToViewSpace.setOutput(new Pipe<>(backfaceCulling));

        var depthSorting = new DepthSorting();
        backfaceCulling.setOutput(new Pipe<>(depthSorting));

        var addColor = new AddColor(pd.getModelColor());
        depthSorting.setOutput(new Pipe<>(addColor));

        var projTransform = new FaceColorPairTransformFilter(pd.getProjTransform());

        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            var lighting = new Lighting(pd.getLightPos());
            lighting.setOutput(new Pipe<>(projTransform));

            addColor.setOutput(new Pipe<>(lighting));
        } else {
            addColor.setOutput(new Pipe<>(projTransform));
        }

        var perspectiveDivision = new PerspectiveDivision();
        projTransform.setOutput(new Pipe<>(perspectiveDivision));

        var viewportTransform = new FaceColorPairTransformFilter(pd.getViewportTransform());
        perspectiveDivision.setOutput(new Pipe<>(viewportTransform));

        var renderer = new Renderer(pd.getGraphicsContext(), pd.getRenderingMode());
        viewportTransform.setOutput(new Pipe<>(renderer));

        return new AnimationRenderer(pd) {
            private float rotationAngle;

            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer). 
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render 
             */
            @Override
            protected void render(float fraction, Model model) {
                rotationAngle += fraction;

                modelSpaceToViewSpace.setTransform(Util.modelSpaceToViewSpace(pd, rotationAngle));

                input.push(model);
            }
        };
    }
}