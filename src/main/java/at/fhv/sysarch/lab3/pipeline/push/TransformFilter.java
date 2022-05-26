package at.fhv.sysarch.lab3.pipeline.push;

import com.hackoeur.jglm.Mat4;

public abstract class TransformFilter<I, O> extends Filter<I, O> {
    protected Mat4 transform;

    protected TransformFilter(Mat4 transform) {

        this.transform = transform;
    }

    public Mat4 getTransform() {
        return transform;
    }

    public void setTransform(Mat4 transform) {
        this.transform = transform;
    }

    @Override
    public void flush() {
        output.flush();
    }
}
