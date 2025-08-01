package com.group.tc.TorrentClient.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.turn.ttorrent.common.Torrent;


import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;


@Slf4j
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
        catch(Exception e) {
//            log.error(e.getMessage());
            throw e;
        }
    }
    public String parseTorrentFileWithPackage(MultipartFile file) throws Exception {
        File fileTemp = null;
        try {
            fileTemp = Files.createTempFile("uploaded-torrentFile", ".torrent").toFile();
            file.transferTo(fileTemp);
            Torrent torrent = Torrent.load(fileTemp);
            String name = torrent.getName();
            long size = torrent.getSize();
            return name + " " + size + " " + torrent.toString();
        }
        catch(Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        finally {
            // Clean up temp file
            if (fileTemp != null && fileTemp.exists()) {
                boolean deleted = fileTemp.delete();
                if (!deleted) fileTemp.deleteOnExit();  // Fallback cleanup
            }
        }
    }
}