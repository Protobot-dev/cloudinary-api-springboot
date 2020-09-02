package com.cloudinary.cloud.controller;

import com.cloudinary.cloud.dao.MessageDao;
import com.cloudinary.cloud.model.Image;
import com.cloudinary.cloud.service.CloudinaryService;
import com.cloudinary.cloud.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cloudinary")
public class MainController {

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    ImageService imageService;

    public ResponseEntity<List<Image>> list(){
        List<Image> list = imageService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam MultipartFile multipartFile) throws IOException {
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if(bi == null){
            return new ResponseEntity(new MessageDao("unavailible image"), HttpStatus.BAD_REQUEST);
        }
        Map result = cloudinaryService.upload(multipartFile);
        Image image = new Image((String)result.get("original filename"),
                                (String)result.get("url"),
                                (String)result.get("public_id"));
        imageService.save(image);
        return new ResponseEntity(new MessageDao("image uploaded"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) throws IOException {
        if(!imageService.exists(id))
            return new ResponseEntity(new MessageDao("dont exist"), HttpStatus.NOT_FOUND);
        Image image = imageService.getImageById(id).get();
        Map result = cloudinaryService.delete(image.getImageId());
        imageService.delete(id);
        return new ResponseEntity(new MessageDao("image deleted"), HttpStatus.OK);
    }

}

