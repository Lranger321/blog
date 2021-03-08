package main.persistence.service;

import main.dto.response.ImageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

@Service
public class ImageService {

    //TODO Upload image
    public ImageResponse saveImage(MultipartFile multipartFile){
        return null;
    }

}
