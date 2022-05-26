package at.fhv.sysarch.lab3.pipeline.pull;

import com.hackoeur.jglm.Mat4;

public abstract class TransformFilter<Tin, Tout> extends Filter<Tin, Tout> {

    private Mat4 transform;

    public TransformFilter(Pipe<Tin> input, Mat4 transform) {
        super(input);
        this.transform = transform;
    }

    public Mat4 getTransform() {
        return transform;
    }

    public void setTransform(Mat4 transform) {
        this.transform = transform;
    }

}
