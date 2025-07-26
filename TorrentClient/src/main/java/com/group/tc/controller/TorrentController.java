package com.group.tc.controller;

import com.group.tc.service.TorrentFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/torrent")
public class TorrentController {

    @Autowired
    private TorrentFileService torrentFileService;

    @GetMapping("/parse")
    public Map<String, Object> parseTorrent(@RequestParam String filePath) throws Exception {
        return torrentFileService.parseTorrentFile(filePath);
    }
    @GetMapping("/health")
    public String health() {
        return "OK";
    }

}