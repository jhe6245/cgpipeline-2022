package at.fhv.sysarch.lab3.pipeline.pull.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Util;
import at.fhv.sysarch.lab3.pipeline.pull.Filter;
import at.fhv.sysarch.lab3.pipeline.pull.Pipe;

import java.util.NoSuchElementException;

public class BackfaceCulling extends Filter<Face, Face> {

    private Face f;

    public BackfaceCulling(Pipe<Face> input) {
        super(input);
    }

    @Override
    public Face next() {
        if(f != null) {
            try { return f; }
            finally { f = null; }
        }

        while(input.hasNext()) {
            var i = input.next();
            if(Util.facesCamera(i)) {
                return i;
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public boolean hasNext() {
        if(f != null)
            return true;

        while(input.hasNext()) {
            var i = input.next();
            if(Util.facesCamera(i)) {
                f = i;
                return true;
            }
        }
        return false;
    }
}
