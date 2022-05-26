package at.fhv.sysarch.lab3.pipeline.push.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Util;
import at.fhv.sysarch.lab3.pipeline.push.Filter;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class DepthSorting extends Filter<Face, Face> {

    private final Queue<Face> faces = new PriorityQueue<>(Comparator.comparing(Util::averageZ));

    @Override
    public void push(Face item) {
        faces.add(item);
    }

    @Override
    public void flush() {
        while(faces.size() > 0)
            output.push(faces.remove());
        output.flush();
    }

}
