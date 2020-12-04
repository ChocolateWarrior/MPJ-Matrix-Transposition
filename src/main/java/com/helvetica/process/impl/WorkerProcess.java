package com.helvetica.process.impl;

import com.helvetica.process.Process;
import com.helvetica.service.TranspositionService;
import com.helvetica.util.ArrayUtils;
import mpi.MPI;

import static com.helvetica.util.Constants.*;

public class WorkerProcess implements Process {

    @Override
    public void process(int[][] originalMatrix) {

        final int[] original = ArrayUtils.flattenArray(originalMatrix, SIZE, SIZE);
        final int[] matrix = receiveMatrix();
        final int[] results = TranspositionService.getTransposed(original, matrix);

        sendResults(results);
    }

    private int[] receiveMatrix() {

        int[][] sendBuffer = new int[INTEGER_ZERO][INTEGER_ZERO];

        int[][] matrix = new int[SIZE / QUANTITY_OF_WORKERS][SIZE];

        int[] sendCounts = new int[INTEGER_ZERO];
        int[] displacement = new int[INTEGER_ZERO];

        MPI.COMM_WORLD.Scatterv(sendBuffer, INTEGER_ZERO, sendCounts, displacement, MPI.OBJECT,
                matrix, INTEGER_ZERO, SIZE / QUANTITY_OF_WORKERS, MPI.OBJECT, MAIN_RANK);

        return ArrayUtils.flattenArray(matrix, SIZE / QUANTITY_OF_WORKERS, SIZE);
    }

    private void sendResults(final int[] results) {

        int[] receiveBuffer = new int[INTEGER_ZERO];
        int[] receiveCounts = new int[INTEGER_ZERO];
        int[] displacement = new int[INTEGER_ZERO];

        MPI.COMM_WORLD.Gatherv(results, INTEGER_ZERO, SIZE / QUANTITY_OF_WORKERS * SIZE, MPI.INT,
                receiveBuffer, INTEGER_ZERO, receiveCounts, displacement, MPI.INT, MAIN_RANK);
    }

}
