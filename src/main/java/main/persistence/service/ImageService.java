package main.persistence.service;

import main.dto.response.ImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;

@Service
public class ImageService {

    public ResponseEntity saveImage(MultipartFile file) {
        HashMap<String, String> errors = errors(file);
        if (errors.isEmpty()) {
            try {
                Path path = randomPath(file.getOriginalFilename());
                Files.write(path, file.getBytes());
                return ResponseEntity.ok(randomPath(path.toString()));
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.ok(new ImageResponse(false, errors));
            }
        }
        return ResponseEntity.ok(new ImageResponse(false, errors));
    }

    public Path randomPath(String originalName) {
        StringBuilder builder = new StringBuilder();
        String firstFilePath = randomString();
        String secondFilePath = randomString();
        File firstFile = new File("upload\\" + firstFilePath);
        if (!firstFile.exists()) {
            firstFile.mkdir();
        }
        File secondFile = new File("upload\\" + firstFilePath + "\\" + secondFilePath);
        if (!secondFile.exists()) {
            secondFile.mkdir();
        }
        builder.append("upload\\")
                .append(firstFilePath)
                .append("\\")
                .append(secondFilePath)
                .append("\\")
                .append(originalName);
        Path filePath = Paths.get(builder.toString());
        return filePath;
    }

    private HashMap<String, String> errors(MultipartFile image) {
        HashMap<String, String> errors = new HashMap<>();
        if (image.getSize() / 1000000 > 5) {
            errors.put("image", "Размер файла превышает допустимый размер");
        }
        String[] imageSplit = image.getOriginalFilename().split("\\.");
        if (imageSplit[imageSplit.length - 1].equals("jpg") || imageSplit[imageSplit.length - 1].equals("png")) {
            errors.put("file", "Файл не допустимого расширения");
        }
        return errors;
    }

    private String randomString() {
        return new Random().ints(97, 122)
                .limit(2)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
