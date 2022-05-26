package at.fhv.sysarch.lab3.pipeline.push.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Util;
import at.fhv.sysarch.lab3.pipeline.push.Filter;
import at.fhv.sysarch.lab3.pipeline.push.Pipe;
import at.fhv.sysarch.lab3.pipeline.push.Sink;

import java.util.Comparator;
import java.util.PriorityQueue;

public class DepthSorting extends Filter<Face, Face> {

    private final PriorityQueue<Face> faces = new PriorityQueue<>(Comparator.comparing(Util::averageZ));

    @Override
    public void push(Face item) {
        faces.add(item);
    }

    @Override
    public void flush() {
        output.processAll(faces);
        faces.clear();
    }

}
