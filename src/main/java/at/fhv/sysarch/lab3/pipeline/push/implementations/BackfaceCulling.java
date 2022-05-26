package at.fhv.sysarch.lab3.pipeline.push.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Util;
import at.fhv.sysarch.lab3.pipeline.push.Filter;

public class BackfaceCulling extends Filter<Face, Face> {

    @Override
    public void push(Face item) {
        if(Util.facesCamera(item))
            output.push(item);
    }

    @Override
    public void flush() {
        output.flush();
    }
}
