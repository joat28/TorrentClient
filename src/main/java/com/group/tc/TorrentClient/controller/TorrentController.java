package com.group.tc.TorrentClient.controller;

import com.group.tc.TorrentClient.service.TorrentFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@Component
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TorrentController {
    private TorrentFileService torrentFileService;

    @PostMapping("/parse-torrent-file")
    public ResponseEntity<?> parseTorrent(@RequestBody MultipartFile file) {
       try {
           return ResponseEntity.status(HttpStatus.OK).body(torrentFileService.parseTorrentFile(file));
       }
       catch(Exception e) {
//           log.error("message = Error while parsing torrent file, exception, cause = {}", e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while parsing file");
       }
    }
    @PostMapping("/parse-with-package")
    public ResponseEntity<String> parseTorrentUsingPackage(@RequestBody MultipartFile file) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(torrentFileService.parseTorrentFileWithPackage(file));
        }
        catch(Exception e) {
//            log.error("message = Error while parsing torrent file using package, exception, cause = {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while parsing file");
        }
    }
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

}