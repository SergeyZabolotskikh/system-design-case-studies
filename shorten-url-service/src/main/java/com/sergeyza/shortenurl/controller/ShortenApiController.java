package com.sergeyza.shortenurl.controller;

import com.sergeyza.shortenurl.api.ShortenApi;
import com.sergeyza.shortenurl.model.ShortenRequest;
import com.sergeyza.shortenurl.model.ShortenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortenApiController implements ShortenApi {

    @Override
    public ResponseEntity<ShortenResponse> shortenUrl(ShortenRequest shortenRequest) {
        String inputUrl = String.valueOf(shortenRequest.getLongUrl());

        ShortenResponse response = new ShortenResponse();
        response.setLongUrl(inputUrl);
        response.setShortUrl("https://sho.rt/abc123");  // Dummy response

        return ResponseEntity.ok(response);
    }
}
