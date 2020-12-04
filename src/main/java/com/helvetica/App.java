package com.helvetica;

import com.helvetica.process.impl.MainProcess;
import com.helvetica.process.impl.WorkerProcess;
import mpi.MPI;

import java.util.Random;
import java.util.stream.IntStream;

import static com.helvetica.util.Constants.*;

public class App {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws InterruptedException {

//        int [][] matrix = generate();
        int [][] matrix = EXAMPLE;

        Thread.sleep(100);
        MPI.Init(args);
        if (MPI.COMM_WORLD.Rank() == MAIN_RANK) {
            executeMainProcess(matrix);
        } else {
            executeWorkerProcess(matrix);
        }

        MPI.Finalize();
    }

    private static void executeWorkerProcess(int[][] matrix) {
        new WorkerProcess().process(matrix);
    }

    private static void executeMainProcess(int[][] matrix) {
        new MainProcess().process(matrix);
    }

    private static int[][] generate() {

        return IntStream.range(INTEGER_ZERO, SIZE)
                .mapToObj(index -> generateRow())
                .toArray(int[][]::new);
    }

    private static int[] generateRow() {

        return IntStream.range(INTEGER_ZERO, SIZE)
                .map(index -> RANDOM.nextInt(20))
                .toArray();
    }

}
