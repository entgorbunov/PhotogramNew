package com.photogram.service;

import com.photogram.dao.UserDao;
import com.photogram.exceptions.ServiceException;
import com.photogram.service.userService.UserServiceForWeb;
import com.photogram.util.PropertiesUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageService {

    private static final AtomicReference<ImageService> IMAGE_SERVICE_ATOMIC_REFERENCE = new AtomicReference<>();

    public static ImageService getImageServiceAtomicReference() {
        IMAGE_SERVICE_ATOMIC_REFERENCE.get();
        if (IMAGE_SERVICE_ATOMIC_REFERENCE.get() == null) {
            ImageService imageService = new ImageService();
            if (IMAGE_SERVICE_ATOMIC_REFERENCE.compareAndSet(null, imageService)) {
                return imageService;
            } else {
                return IMAGE_SERVICE_ATOMIC_REFERENCE.get();
            }
        }
        return IMAGE_SERVICE_ATOMIC_REFERENCE.get();
    }


    private static final UserServiceForWeb USER_SERVICE_FOR_WEB_ATOMIC_REFERENCE = UserServiceForWeb.getUserServiceForWebAtomicReference();
    private static final UserDao USER_DAO_ATOMIC_REFERENCE = UserDao.getUserDaoAtomicReference();

    private final String basePath = PropertiesUtil.get("image.base.url");


    public void upload(String imagePath, InputStream imageContent) {
        if (imageContent == null) {
            return;
        }
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
