package at.fhv.sysarch.lab3.pipeline.pull;

import com.hackoeur.jglm.Mat4;

public abstract class TransformFilter<I, O> extends Filter<I, O> {

    private Mat4 transform;

    public TransformFilter(Pipe<I> input, Mat4 transform) {
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
