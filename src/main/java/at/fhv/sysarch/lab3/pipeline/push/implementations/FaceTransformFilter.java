package at.fhv.sysarch.lab3.pipeline.push.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Util;
import at.fhv.sysarch.lab3.pipeline.push.TransformFilter;
import com.hackoeur.jglm.Mat4;

public class FaceTransformFilter extends TransformFilter<Face, Face> {

    public FaceTransformFilter(Mat4 transform) {
        super(transform);
    }

    @Override
    public void push(Face item) {
        output.push(Util.apply(item, getTransform()));
    }

}
