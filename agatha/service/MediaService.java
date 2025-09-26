package com.agatha.agatha.service;

import java.util.List;

public interface MediaService {
    Object getMediaById(Long id);
    List<Object> getAllMedia();
    Object createMedia(Object media);
    Object updateMedia(Long id, Object mediaDetails);
    void deleteMedia(Long id);
    Void playMedia(Long id);
}
