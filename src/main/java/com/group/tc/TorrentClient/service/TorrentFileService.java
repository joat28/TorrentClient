package com.group.tc.TorrentClient.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.turn.ttorrent.common.Torrent;




import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

@Service
public class TorrentFileService {

    public Map<String, Object> parseTorrentFile(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream()) {
            BencodeDecoder decoder = new BencodeDecoder(is);
            Object result = decoder.decode();
            if (result instanceof Map) {
                return (Map<String, Object>) result;
            } else {
                throw new IllegalArgumentException("Not a valid torrent file");
            }
        }
    }
    public Map<String, Object> parseTorrentFileWithPackage(MultipartFile file) throws Exception {


        Torrent
        System.out.println("Name: " + torrent.getName());
        System.out.println("Piece length: " + torrent.getPieceLength());

    }
}