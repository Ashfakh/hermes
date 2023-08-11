package com.ashfakh.hermes.repository;

import com.ashfakh.hermes.entity.MediaResource;
import org.springframework.data.repository.CrudRepository;

public interface MediaResourceRepository extends CrudRepository<MediaResource, Long> {

    MediaResource findMediaResourceByMediaName(String mediaName);
}
