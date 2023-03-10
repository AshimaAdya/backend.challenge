package com.league.backend.challenge;

import com.league.backend.challenge.controller.MatrixController;
import com.league.backend.challenge.exception.InvalidFileException;
import com.league.backend.challenge.exception.InvalidMatrixException;
import com.league.backend.challenge.utils.MatrixService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatrixControllerTest {

    @Mock
    MatrixService matrixService;

    @InjectMocks
    MatrixController matrixController;

    @Test
    void handleFileUpload_whenInvalidMatrixExceptionIsThrown_shouldReturnBadRequest() throws IOException, InvalidMatrixException, InvalidFileException {
        // given
        MultipartFile file = new MockMultipartFile("test.csv", "test.csv", "text/csv", "".getBytes());
        String expectedErrorMessage = "File is null or empty";

        // when
        when(matrixService.readMatrixFromFile(file)).thenThrow(new InvalidMatrixException(expectedErrorMessage));
        ResponseEntity<String> response = matrixController.handleFileUpload(file);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(expectedErrorMessage, response.getBody());
    }

    @Test
    void handleFileUpload_whenInvalidFileExceptionIsThrown_shouldReturnBadRequest() throws IOException, InvalidMatrixException, InvalidFileException {
        // given
        MultipartFile file = new MockMultipartFile("test.csv", "test.csv", "text/plain", "".getBytes());
        String expectedErrorMessage = "Invalid file";

        // when
        when(matrixService.readMatrixFromFile(file)).thenThrow(new InvalidFileException(expectedErrorMessage));
        ResponseEntity<String> response = matrixController.handleFileUpload(file);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(expectedErrorMessage, response.getBody());
    }

    @Test
    void invertMatrix_whenValidFileIsUploaded_shouldReturnInvertedMatrix() throws IOException, InvalidMatrixException, InvalidFileException {
        // given
        MultipartFile file = new MockMultipartFile("test.csv", "test.csv", "text/csv", "".getBytes());
        int[][] originalMatrix = {{1, 2}, {3, 4}};
        int[][] invertedMatrix = {{1, 3}, {2, 4}};

        // when
        when(matrixService.readMatrixFromFile(file)).thenReturn(originalMatrix);
        when(matrixService.invertMatrix(originalMatrix)).thenReturn(invertedMatrix);
        ResponseEntity<String> response = matrixController.invertMatrix(file);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("[-2,1],[1.5,-0.5]", response.getBody());
    }

    @Test
    void flattenMatrix_whenValidFileIsUploaded_shouldReturnFlattenedMatrix() throws IOException, InvalidMatrixException, InvalidFileException {
        // given
        MultipartFile file = new MockMultipartFile("test.csv", "test.csv", "text/csv", "".getBytes());
        int[][] matrix = {{1, 2}, {3, 4}};
        String flattenedMatrix = "1,2,3,4";

        // when
        when(matrixService.readMatrixFromFile(file)).thenReturn(matrix);
        when(matrixService.flatten(matrix)).thenReturn(flattenedMatrix);
        ResponseEntity<String> response = matrixController.flattenMatrix(file);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(flattenedMatrix, response.getBody());
    }
}

