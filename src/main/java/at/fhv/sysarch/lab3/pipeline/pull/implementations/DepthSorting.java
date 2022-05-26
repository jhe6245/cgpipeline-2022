package at.fhv.sysarch.lab3.pipeline.pull.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Util;
import at.fhv.sysarch.lab3.pipeline.pull.Filter;
import at.fhv.sysarch.lab3.pipeline.pull.Pipe;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class DepthSorting extends Filter<Face, Face> {

    final Queue<Face> faces = new PriorityQueue<>(Comparator.comparing(Util::averageZ));

    public DepthSorting(Pipe<Face> input) {
        super(input);
    }

    @Override
    public Face next() {
        input.forEachRemaining(faces::add);

        return faces.remove();
    }

    @Override
    public boolean hasNext() {
        input.forEachRemaining(faces::add);

        return !faces.isEmpty();
    }
}
