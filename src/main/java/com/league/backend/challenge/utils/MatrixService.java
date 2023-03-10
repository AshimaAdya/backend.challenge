package com.league.backend.challenge.utils;

import com.league.backend.challenge.exception.InvalidFileException;
import com.league.backend.challenge.exception.InvalidMatrixException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatrixService {


    public static int[][] readMatrixFromFile(MultipartFile file) throws IOException, InvalidMatrixException, InvalidFileException {
        if (file == null || file.isEmpty()) {
            throw new InvalidMatrixException("File is null or empty");
        }
        if (!file.getContentType().equals("text/csv")) {
            throw new InvalidFileException("File is not a CSV file");
        }

        InputStream inputStream = file.getInputStream();
        List<String> lines = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.toList());

        int numRows = lines.size();
        int numCols = lines.get(0).split(",").length;

        for (int i = 0; i < numRows; i++) {
            String[] values = lines.get(i).split(",");
            if (values.length != numCols) {
                throw new InvalidMatrixException("Number of columns in row " + (i+1) + " does not match number of columns in first row");
            }
            for (int j = 0; j < numCols; j++) {
                try {
                    long value = Long.parseLong(values[j]);
                    if (value > Integer.MAX_VALUE) {
                        throw new InvalidMatrixException("Integer value at row " + (i+1) + ", column " + (j+1) + " exceeds the maximum value of integer");
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidMatrixException("Invalid integer value at row " + (i+1) + ", column " + (j+1));
                }
            }
        }

        int[][] matrix = new int[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            String[] values = lines.get(i).split(",");
            for (int j = 0; j < numCols; j++) {
                long value = Long.parseLong(values[j]);
                if (value > Integer.MAX_VALUE) {
                    throw new InvalidMatrixException("Integer value at row " + (i+1) + ", column " + (j+1) + " exceeds the maximum value of integer");
                }
                matrix[i][j] = (int) value;
            }
        }
        return matrix;
    }


    public static int[][] invertMatrix(int[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        int[][] inverted = new int[numCols][numRows];

        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                inverted[i][j] = matrix[j][i];
            }
        }

        return inverted;
    }

    public  String flatten(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : matrix) {
            for (int num : row) {
                sb.append(num).append(",");
            }
        }
        sb.setLength(sb.length() - 1); // remove the last comma
        return sb.toString();
    }

    public static int sum(int[][] matrix) throws ArithmeticException {
        long sum = 0; // Use long to store the sum
        for (int[] row : matrix) {
            for (int num : row) {
                sum += num;
            }
        }
        if (sum > Integer.MAX_VALUE) {
            throw new ArithmeticException("Sum of the integers in the matrix exceeds the integer limit");
        }
        return (int) sum; // Cast the sum back to int
    }
    public static int multiply(int[][] matrix) throws ArithmeticException {
        // Used long here in case product value exceeds Integer Limit
        long product = 1;
        for (int[] row : matrix) {
            for (int value : row) {
                product *= value;
                if (product > Integer.MAX_VALUE) {
                    throw new ArithmeticException("Product of integers in the matrix is out of range of the int data type");
                }
            }
        }
        return (int) product;
    }
    public static String matrixToString(int[][] matrix) {
        StringBuilder sb = new StringBuilder();

        for (int[] row : matrix) {
            for (int i = 0; i < row.length; i++) {
                sb.append(row[i]);
                if (i < row.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
