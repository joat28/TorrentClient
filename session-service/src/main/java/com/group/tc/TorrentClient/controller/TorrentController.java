package com.group.tc.TorrentClient.controller;

import com.group.tc.TorrentClient.service.TorrentFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TorrentController {

    private final TorrentFileService torrentFileService;
    @PostMapping("/parse-torrent-file")
    public ResponseEntity<?> parseTorrent(@RequestBody MultipartFile file) {
       try {
           log.info("Calling parseTorrent endpoint with file: {}", file.getOriginalFilename());
           return ResponseEntity.status(HttpStatus.OK).body(torrentFileService.parseTorrentFile(file));
       }
       catch(Exception e) {
           log.error("message = Error while parsing torrent file, exception, cause = {}", e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while parsing file" +  e.getMessage());
       }
    }
    @PostMapping("/parse-with-package")
    public ResponseEntity<?> parseTorrentUsingPackage(@RequestBody MultipartFile file) {
        try {
            log.info("Calling parseTorrentUsingPackage endpoint with file: {}", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.OK).body(torrentFileService.parseTorrentFileWithPackage(file));
        }
        catch(Exception e) {
            log.error("message = Error while parsing torrent file using package, exception, cause = {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while parsing file");
        }
    }

//    @GetMapping("/get-peers")
//    public ResponseEntity<?> getPeers(String result) {
//
//        return ResponseEntity.status(HttpStatus.OK).body(torrentFileService.decodePeerInformation(result));
//
//
//    }
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

}