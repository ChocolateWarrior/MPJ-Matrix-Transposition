package com.helvetica.util;

public interface ArrayUtils {

    static int[][] generate(final int dimension) {
        int[][] matrix = new int[dimension][dimension];
        populateMatrix(matrix);
        return matrix;
    }

    static int[][] generateEmpty(final int dimension) {
        return new int[dimension][dimension];
    }

    private static void populateMatrix(int[][] matrix) {
        for (int cols = 0; cols < matrix.length; cols++) {
            for (int rows = 0; rows < matrix.length; rows++) {
                matrix[cols][rows] = (int) (Math.random() * 200 - 100);
            }
        }
    }

    static int[] flattenArray(int[][] array, int height, int width) {
        int[] flatArray = new int[height * width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                flatArray[i * width + j] = array[i][j];
            }
        }

        return flatArray;
    }

    static void printMatrix(int[][] matrix) {
        final int dimension = matrix.length;
        for (int[] col : matrix) {
            StringBuilder currentRow = new StringBuilder();
            for (int row = 0; row < dimension; row++) {
                currentRow.append(col[row]).append(" ");
            }
            System.out.println(currentRow);
        }
    }

    static void printFlattened(int[] array, int dimension) {
        System.out.println();
        for (int i = 0; i < array.length; i++) {
            System.out.print(" " + array[i]);
            if ((i + 1) % dimension == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }
}
