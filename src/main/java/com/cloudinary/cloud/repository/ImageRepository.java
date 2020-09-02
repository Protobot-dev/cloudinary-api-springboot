package com.cloudinary.cloud.repository;

import com.cloudinary.cloud.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ImageRepository extends MongoRepository<Image, String> {

    List<Image> findByOrderById();
}
