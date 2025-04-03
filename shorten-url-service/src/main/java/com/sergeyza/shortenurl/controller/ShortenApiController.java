package com.sergeyza.shortenurl.controller;

import com.sergeyza.shortenurl.api.ShortenApi;
import com.sergeyza.shortenurl.model.ShortenRequest;
import com.sergeyza.shortenurl.model.ShortenResponse;
import com.sergeyza.shortenurl.core.service.ShortenUrlService;
import com.sergeyza.shortenurl.persistence.entity.UrlMapEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class ShortenApiController implements ShortenApi {

    private final ShortenUrlService shortenUrlService;

    public ShortenApiController(ShortenUrlService shortenUrlService) {
        this.shortenUrlService = shortenUrlService;
    }

    @Override
    public ResponseEntity<ShortenResponse> shortenUrl(ShortenRequest shortenRequest) {
        ShortenResponse response = new ShortenResponse();
        response.setLongUrl(shortenRequest.getLongUrl().toString());
        try {
            if (!isValidUrl(shortenRequest.getLongUrl().toString())) {
                response.error("Invalid URL format");
                return ResponseEntity.badRequest().body(response);
            }
            UrlMapEntity saved = shortenUrlService.shortenUrl(shortenRequest.getLongUrl().toString());
            response.setShortUrl("https://sergey.za/" + saved.getShortToken());
            return ResponseEntity.ok(response);
        } catch (Exception exception) {
            response.error("Internal server error: " + exception.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private boolean isValidUrl(String url) {
        try {
            URI uri = URI.create(url);
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (Exception exception) {
            return false;
        }
    }
}
