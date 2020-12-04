package com.helvetica.service;

import java.util.Arrays;
import java.util.Collections;

import static com.helvetica.util.Constants.QUANTITY_OF_WORKERS;
import static com.helvetica.util.Constants.SIZE;

public class TranspositionService {

    public static int[] getTransposed(int[] originalMatrix, int[] workerMatrix) {
        int workerNumber = getWorkerNumber(originalMatrix, workerMatrix);
        final int workerMatrixSize = workerMatrix.length;

        int[] result = new int[workerMatrixSize];

        for (int i = 0; i < workerMatrixSize; i++) {
            int currentElementNum = workerNumber * workerMatrixSize + i;
            result[i] = originalMatrix[(SIZE * currentElementNum % originalMatrix.length) +
                    (int) Math.floor(currentElementNum / SIZE)];
        }

        return result;
    }

    public static int getWorkerNumber(int[] originalMatrix, int[] workerMatrix) {
        return (Collections.indexOfSubList(Arrays.asList(originalMatrix), Arrays.asList(workerMatrix)) + 1) /
                (SIZE / QUANTITY_OF_WORKERS);
    }
}
