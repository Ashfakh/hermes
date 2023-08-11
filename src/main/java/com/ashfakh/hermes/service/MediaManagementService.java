package com.ashfakh.hermes.service;

import com.ashfakh.hermes.client.WAHttpClient;
import com.ashfakh.hermes.dto.MediaDTO;
import com.ashfakh.hermes.entity.MediaResource;
import com.ashfakh.hermes.repository.MediaResourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class MediaManagementService {

    private final MediaResourceRepository mediaResourceRepository;

    private final WAHttpClient waHttpClient;

    @Autowired
    public MediaManagementService(MediaResourceRepository mediaResourceRepository, WAHttpClient waHttpClient) {
        this.mediaResourceRepository = mediaResourceRepository;
        this.waHttpClient = waHttpClient;
    }

    public MediaDTO getMediaExternalId(String mediaName, Long expiryTime) {
        MediaResource mediaResource = mediaResourceRepository.findMediaResourceByMediaName(mediaName);
        String externalId = null;
        if (mediaResource != null) {
            if (System.currentTimeMillis() - mediaResource.getUpdatedAt() < expiryTime) {
                externalId = mediaResource.getExternalId();
            } else {
                try {
                    externalId = uploadNewMedia(mediaResource.getPublicUrl(), mediaResource.getType());
                    if (externalId != null && !externalId.isEmpty()) {
                        mediaResource.setExternalId(externalId);
                        mediaResource.setUpdatedAt(System.currentTimeMillis());
                        mediaResourceRepository.save(mediaResource);
                    } else {
                        log.error("External Id is null or empty");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return MediaDTO.builder().mediaId(externalId).caption(mediaResource.getCaption()).mediaType(mediaResource.getType()).build();
        } else {
            log.error("Media Resource not found for media name: {}", mediaName);
        }
        return MediaDTO.builder().mediaId(externalId).caption(null).build();

    }

    public String uploadNewMedia(String mediaUrl, String mediaType) throws IOException {
        String localFilePath = "/var/tmp/vid.mp4";
//        String localFilePath = "/Users/ashfakh/Desktop/vid.mp4";
        InputStream in = new URL(mediaUrl).openStream();
        Files.copy(in, Paths.get(localFilePath), StandardCopyOption.REPLACE_EXISTING);
        in.close();
        String externalId = waHttpClient.uploadMedia(localFilePath, mediaType);
        System.out.println("File downloaded successfully.");
        return externalId;
    }
}
