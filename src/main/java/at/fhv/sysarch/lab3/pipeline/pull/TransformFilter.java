package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;

public class TransformFilter extends Filter<Face, Face> {

    private Mat4 transform;

    public TransformFilter(Pipe<Face> input, Mat4 transform) {
        super(input);
        this.transform = transform;
    }

    public Mat4 getTransform() {
        return transform;
    }

    public void setTransform(Mat4 transform) {
        this.transform = transform;
    }

    @Override
    public boolean hasNext() {
        return input.hasNext();
    }

    @Override
    public Face next() {
        var face = input.next();
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
