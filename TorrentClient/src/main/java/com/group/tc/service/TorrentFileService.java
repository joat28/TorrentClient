package com.group.tc.service;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Map;

@Service
public class TorrentFileService {

    public Map<String, Object> parseTorrentFile(String filePath) throws Exception {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            BencodeDecoder decoder = new BencodeDecoder(fis);
            Object result = decoder.decode();
            if (result instanceof Map) {
                return (Map<String, Object>) result;
            } else {
                throw new IllegalArgumentException("Not a valid torrent file");
            }
        }
    }
}