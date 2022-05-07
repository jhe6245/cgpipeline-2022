package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import com.hackoeur.jglm.Mat4;

public class TransformFilter extends ByFaceFilter {

    private final Mat4 transform;

    public TransformFilter(Pipe<Model> input, Mat4 transform) {
        super(input);
        this.transform = transform;
    }

    @Override
    protected Face processFace(Face face) {
        return new Face(
                transform.multiply(face.getV1()),
                transform.multiply(face.getV2()),
                transform.multiply(face.getV3()),
                transform.multiply(face.getN1()),
                transform.multiply(face.getN2()),
                transform.multiply(face.getN3())
        );
    }
}
