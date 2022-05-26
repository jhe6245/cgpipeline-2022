package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec4;

public class Util {

    public static float averageZ(Face f) {
        return (f.getV1().getZ() + f.getV2().getZ() + f.getV3().getZ()) / 3;
    }

    public static Mat4 modelSpaceToViewSpace(PipelineData pd, float rotationAngle) {
        return pd.getModelTranslation()
                .multiply(pd.getViewTransform())
                .multiply(Matrices.rotate(rotationAngle, pd.getModelRotAxis()));
    }
    
    public static Face apply(Face face, Mat4 transform) {
        return new Face(
                transform.multiply(face.getV1()),
                transform.multiply(face.getV2()),
                transform.multiply(face.getV3()),
                transform.multiply(face.getN1()),
                transform.multiply(face.getN2()),
                transform.multiply(face.getN3())
        );
    }

    public static Vec4 perspectiveDivision(Vec4 v) {
        return v.multiply(1 / v.getW());
    }

    private static Vec4 d(Vec4 v) {
        return perspectiveDivision(v);
    }

    public static Face perspectiveDivision(Face f) {
        return new Face(
                d(f.getV1()), d(f.getV2()), d(f.getV3()),
                d(f.getN1()), d(f.getN2()), d(f.getN3())
        );
    }

    public static boolean facesCamera(Face f) {
        return f.getV1().dot(f.getN1()) < 0;
    }

}
