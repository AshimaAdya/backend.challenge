package com.league.backend.challenge.controller;


import com.league.backend.challenge.exception.InvalidFileException;
import com.league.backend.challenge.exception.InvalidMatrixException;
import com.league.backend.challenge.utils.MatrixService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
public class MatrixController {

    @Autowired
    MatrixService matrixService;

    @PostMapping("/echo")
    public ResponseEntity<String> handleFileUpload(MultipartFile file) throws IOException {
        try {
            int[][] matrix = matrixService.readMatrixFromFile(file);
            String matrixString= matrixService.matrixToString(matrix);
            return ResponseEntity.ok(matrixString);
        } catch (InvalidMatrixException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidFileException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/invert")
    public ResponseEntity<String> invertMatrix(@RequestParam("file") MultipartFile file) {
        try {
            int[][] matrix = matrixService.readMatrixFromFile(file);
            String matrixString= matrixService.matrixToString(matrix);
            int[][] invertedMatrix = matrixService.invertMatrix(matrix);
            String invertedCsv = matrixService.matrixToString(invertedMatrix);
            return ResponseEntity.ok(invertedCsv);
        } catch (IOException | InvalidFileException e) {
            // Handle IO error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading file: " + e.getMessage());
        } catch (InvalidMatrixException e) {
            // Handle invalid matrix error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/flatten")
    public ResponseEntity<String> flattenMatrix(@RequestParam("file") MultipartFile file) {
        try {
            int[][] matrix = matrixService.readMatrixFromFile(file);
            String flattenedMatrix = matrixService.flatten(matrix);
            return ResponseEntity.ok(flattenedMatrix);

        } catch (IOException | InvalidFileException e) {
            // Handle IO error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading file: " + e.getMessage());
        } catch (InvalidMatrixException e) {
            // Handle invalid matrix error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/sum")
    public ResponseEntity<Integer> sumMatrixValues(@RequestParam("file") MultipartFile file) {
        try {
            int[][] matrix = matrixService.readMatrixFromFile(file);
            int sum = matrixService.sum(matrix);
            return ResponseEntity.ok(sum);

        } catch (IOException | InvalidFileException e) {
            // Handle IO error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(-1);
        } catch (InvalidMatrixException e) {
            // Handle invalid matrix error
            return ResponseEntity.badRequest().body(-1);
        }
    }

    @PostMapping("/multiply")
    public ResponseEntity<Integer> multiplyMatrixValues(@RequestParam("file") MultipartFile file) {
        try {
            int[][] matrix = matrixService.readMatrixFromFile(file);
            int multiply = matrixService.multiply(matrix);
            return ResponseEntity.ok(multiply);

        } catch (IOException | InvalidFileException e) {
            // Handle IO error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(-1);
        } catch (InvalidMatrixException e) {
            // Handle invalid matrix error
            return ResponseEntity.badRequest().body(-1);
        }
    }



}
