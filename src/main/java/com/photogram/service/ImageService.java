package com.photogram.service;

import com.photogram.exceptions.ServiceException;
import com.photogram.util.PropertiesUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageService {
    @Getter
    private static final ImageService INSTANCE = new ImageService();

    private final String basePath = PropertiesUtil.get("image.base.url");



    public void upload(String imagePath, InputStream imageContent) {

        Path imageFullPath = Path.of(basePath, imagePath);
        try (imageContent) {
            Files.createDirectories(imageFullPath.getParent());
            Files.write(imageFullPath, imageContent.readAllBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new ServiceException("Failed to upload image", e);
        }
    }


    public Optional<InputStream> get(String imagePath) {
        Path imageFullPath = Path.of(basePath, imagePath);
        try {
            return Files.exists(imageFullPath)
                    ? Optional.of(Files.newInputStream(imageFullPath))
                    : Optional.empty();
        } catch (IOException e) {
            throw new ServiceException("Failed to get file content", e);
        }
    }
}
