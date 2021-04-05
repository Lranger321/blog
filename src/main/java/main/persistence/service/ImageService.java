package main.persistence.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import main.dto.response.ImageResponse;
import main.persistence.service.constants.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

@Service
public class ImageService {

    private final AmazonS3 s3Client;

    private final AmazonClient amazonClient;

    @Autowired
    public ImageService(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
        BasicAWSCredentials credentials = new BasicAWSCredentials(amazonClient.getAccessKey(), amazonClient.getSecretKey());
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(amazonClient.getRegion())
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    public ResponseEntity saveImage(MultipartFile file) {
        HashMap<String, String> errors = errors(file);
        if (errors.isEmpty()) {
            String path = uploadFile(file);
            return ResponseEntity.ok(path);
        }
        return ResponseEntity.ok(new ImageResponse(false, errors));
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

    public String uploadFile(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = "/upload/" + fileName.hashCode();
            uploadFileTos3bucket(String.valueOf(fileName.hashCode()), file);
            file.delete();
        } catch (AmazonClientException | IOException ex) {
            ex.printStackTrace();
        }
        return fileUrl;

    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File toFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(toFile);
        fos.write(file.getBytes());
        fos.close();
        return toFile;
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3Client.putObject(amazonClient.getBucketName(), fileName, file);
    }

    public void deleteFileFromBucket(String filename){
        s3Client.deleteObject(amazonClient.getBucketName(),filename);
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    public byte[] getImage(String fileName) {
        S3Object s3Object = s3Client.getObject(amazonClient.getBucketName(),fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
