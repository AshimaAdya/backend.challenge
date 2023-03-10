package com.league.backend.challenge;

import com.league.backend.challenge.exception.InvalidFileException;
import com.league.backend.challenge.exception.InvalidMatrixException;
import com.league.backend.challenge.utils.MatrixService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatrixServiceTest {

    private MatrixService matrixService;

    @Mock
    private MultipartFile mockFile;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        matrixService = new MatrixService();
    }

    @Test
    void readMatrixFromFile_withValidFile_returnsMatrix() throws IOException, InvalidMatrixException, InvalidFileException {
        String csv = "1,2,3\n4,5,6\n7,8,9";
        InputStream stream = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("matrix.csv", "matrix.csv", "text/csv", stream);
        int[][] expectedMatrix = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

        int[][] resultMatrix = matrixService.readMatrixFromFile(mockMultipartFile);

        assertArrayEquals(expectedMatrix, resultMatrix);
    }

    @Test
    void readMatrixFromFile_withEmptyFile_throwsInvalidMatrixException() throws IOException {
        when(mockFile.isEmpty()).thenReturn(true);

        Assertions.assertThrows(InvalidMatrixException.class, () -> matrixService.readMatrixFromFile(mockFile));
    }

    @Test
    void readMatrixFromFile_withNullFile_throwsInvalidMatrixException() throws IOException {
//        when(mockFile.isEmpty()).thenReturn(false);
//        when(mockFile.getContentType()).thenReturn(null);

        Assertions.assertThrows(InvalidMatrixException.class, () -> matrixService.readMatrixFromFile(null));
    }

    @Test
    void readMatrixFromFile_withNonCsvFile_throwsInvalidFileException() throws IOException {
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getContentType()).thenReturn("application/octet-stream");

        Assertions.assertThrows(InvalidFileException.class, () -> matrixService.readMatrixFromFile(mockFile));
    }

    @Test
    void readMatrixFromFile_withInvalidCsv_throwsInvalidMatrixException() throws IOException {
        String invalidCsv = "1,2,3\n4,5\n6,7,8";
        InputStream stream = new ByteArrayInputStream(invalidCsv.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("matrix.csv", "matrix.csv", "text/csv", stream);

        Assertions.assertThrows(InvalidMatrixException.class, () -> matrixService.readMatrixFromFile(mockMultipartFile));
    }

    @Test
    public void testInvertMatrix() {

        int[][] matrix2 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[][] expected2 = {{1, 4, 7}, {2, 5, 8}, {3, 6, 9}};
        int[][] actual2 = MatrixService.invertMatrix(matrix2);
        assertArrayEquals(expected2, actual2);

        // Test with a 1x1 matrix
        int[][] matrix3 = {{5}};
        int[][] expected3 = {{5}};
        int[][] actual3 = MatrixService.invertMatrix(matrix3);
        assertArrayEquals(expected3, actual3);
    }
    @Test
    public void testFlatten() {
        MatrixService service = new MatrixService();

        int[][] matrix1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        String expected1 = "1,2,3,4,5,6,7,8,9";
        String actual1 = service.flatten(matrix1);
        assertEquals(expected1, actual1);


    }


    @Test
    public void testSum() {
        // Test a matrix with all positive integers
        int[][] matrix1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        assertEquals(45, MatrixService.sum(matrix1));

        // Test a matrix with all negative integers
        int[][] matrix2 = {{-1, -2, -3}, {-4, -5, -6}, {-7, -8, -9}};
        assertEquals(-45, MatrixService.sum(matrix2));

        // Test a matrix with a mix of positive and negative integers
        int[][] matrix3 = {{1, -2, 3}, {-4, 5, -6}, {7, -8, 9}};
        assertEquals(5, MatrixService.sum(matrix3));

        // Test a matrix with only one element
        int[][] matrix4 = {{1}};
        assertEquals(1, MatrixService.sum(matrix4));

        // Test a matrix with large numbers that exceed the integer limit
        int[][] matrix6 = {{Integer.MAX_VALUE, Integer.MAX_VALUE}, {Integer.MAX_VALUE, Integer.MAX_VALUE}};
        try {
            MatrixService.sum(matrix6);
            fail("Expected an ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            assertEquals("Sum of the integers in the matrix exceeds the integer limit", e.getMessage());
        }
    }


    @Test
    public void testMultiplyWithValidMatrix() {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int expectedProduct = 362880;
        int actualProduct = MatrixService.multiply(matrix);
        assertEquals(expectedProduct, actualProduct);

        int[][] matrix1 = {{-1, -2, -3}, {-4, -5, -6}, {-7, -8, -9}};
        assertEquals(-362880, MatrixService.multiply(matrix1));

        int[][] matrix6 = {{Integer.MAX_VALUE, Integer.MAX_VALUE}, {Integer.MAX_VALUE, Integer.MAX_VALUE}};
        try {
            MatrixService.multiply(matrix6);
            fail("Expected an ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            assertEquals("Product of integers in the matrix is out of range of the int data type", e.getMessage());
        }
    }



    @Test
    public void testMatrixToString_multipleRowsColumns() {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        String expected = "1,2,3\n4,5,6\n7,8,9\n";
        String actual = MatrixService.matrixToString(matrix);
        assertEquals(expected, actual);
    }

}