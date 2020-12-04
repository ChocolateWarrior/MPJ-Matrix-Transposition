package com.helvetica.process.impl;

import com.helvetica.process.Process;
import com.helvetica.util.ArrayUtils;
import mpi.MPI;

import java.util.stream.IntStream;

import static com.helvetica.util.Constants.*;

public class MainProcess implements Process {

    @Override
    public void process(int[][] originalMatrix) {
        delegate(originalMatrix);
        int[] result = receiveResult();
        ArrayUtils.printFlattened(result, SIZE);
    }

    private void delegate(final int[][] matrix) {
        sendMatrix(matrix);
    }

    private void sendMatrix(final int[][] matrix) {

        int[][] receiveBuffer = new int[INTEGER_ZERO][INTEGER_ZERO];

        int[] sendCounts = getCounts();
        int[] displacement = getDisplacement();

        MPI.COMM_WORLD.Scatterv(matrix, INTEGER_ZERO, sendCounts, displacement, MPI.OBJECT,
                receiveBuffer, INTEGER_ZERO, INTEGER_ZERO, MPI.OBJECT, MAIN_RANK);
    }

    private int[] receiveResult() {

        int[] result = new int[SIZE * SIZE];
        int[] sendBuffer = new int[INTEGER_ZERO];

        int[] receiveCounts = getReceiveCounts();
        int[] displacement = getReceiveDisplacement();

        MPI.COMM_WORLD.Gatherv(sendBuffer, INTEGER_ZERO, INTEGER_ZERO, MPI.INT, result, INTEGER_ZERO,
                receiveCounts, displacement, MPI.INT, MAIN_RANK);

        return result;
    }

    private int[] getReceiveDisplacement() {
        final int[] displacement = new int[QUANTITY_OF_PROCESSES];

        displacement[MAIN_RANK] = INTEGER_ZERO;

        IntStream.range(INTEGER_ONE, QUANTITY_OF_PROCESSES)
                .forEach(index -> displacement[index] = (index - INTEGER_ONE) * (SIZE / QUANTITY_OF_WORKERS * SIZE));

        return displacement;
    }

    private int[] getReceiveCounts() {

        final int[] sendCounts = new int[QUANTITY_OF_PROCESSES];
        sendCounts[MAIN_RANK] = INTEGER_ZERO;

        IntStream.range(INTEGER_ONE, QUANTITY_OF_PROCESSES)
                .forEach(index -> sendCounts[index] = SIZE / QUANTITY_OF_WORKERS * SIZE);

        return sendCounts;
    }

    private int[] getCounts() {

        final int[] sendCounts = new int[QUANTITY_OF_PROCESSES];
        sendCounts[MAIN_RANK] = INTEGER_ZERO;

        IntStream.range(INTEGER_ONE, QUANTITY_OF_PROCESSES)
                .forEach(index -> sendCounts[index] = SIZE / QUANTITY_OF_WORKERS);

        return sendCounts;
    }

    private int[] getDisplacement() {

        final int[] displacement = new int[QUANTITY_OF_PROCESSES];

        displacement[MAIN_RANK] = INTEGER_ZERO;

        IntStream.range(INTEGER_ONE, QUANTITY_OF_PROCESSES)
                .forEach(index -> displacement[index] = (index - INTEGER_ONE) * (SIZE / QUANTITY_OF_WORKERS));

        return displacement;
    }

}
