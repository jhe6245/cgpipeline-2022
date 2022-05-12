package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;

public class Transform {
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
}
