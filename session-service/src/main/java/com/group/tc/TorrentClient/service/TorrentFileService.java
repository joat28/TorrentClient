package com.group.tc.TorrentClient.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.turn.ttorrent.common.Torrent;
import javax.xml.bind.DatatypeConverter;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
            log.error("Error parsing torrent file - {}", e.getMessage(), e);
            log.error(e.getMessage());
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

            String infoHashHex = DatatypeConverter.printHexBinary(torrent.getInfoHash());

            byte[] bytes = new byte[infoHashHex.length() / 2];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) Integer.parseInt(infoHashHex.substring(2 * i, 2 * i + 2), 16);
            }
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append('%').append(String.format("%02X", b));
            }
            System.out.println(sb.toString());
            log.info("info hash is :" + sb.toString());

            long size = torrent.getSize();

            return name + " " + size + " " + torrent.toString();
        }
        catch(Exception e) {
            log.error("Error parsing torrent file with package - {}", e.getMessage(), e);
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
//    public static void main(String[] args) throws IOException {
//        // Replace with your raw peer bytes
//        byte[] peers300 = new byte[300]; // fill with your peers300 bytes
//        byte[] peers60 = new byte[60];   // fill with your peers60 bytes
//
//        List<String> allPeers = new ArrayList<>();
//        allPeers.addAll(decodePeers(peers300));
//        allPeers.addAll(decodePeers(peers60));
//
//        for (String peer : allPeers) {
//            System.out.println(peer);
//        }
//    }

    public static List<String> decodePeers(byte[] peers) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i + 5 < peers.length; i += 6) {
            int ip1 = peers[i] & 0xFF;
            int ip2 = peers[i + 1] & 0xFF;
            int ip3 = peers[i + 2] & 0xFF;
            int ip4 = peers[i + 3] & 0xFF;

            int port = ((peers[i + 4] & 0xFF) << 8) | (peers[i + 5] & 0xFF);

            String ip = ip1 + "." + ip2 + "." + ip3 + "." + ip4;
            result.add(ip + ":" + port);
        }
        return result;
    }


    public static void main(String[] args) throws Exception {
        byte[] response = Files.readAllBytes(Paths.get("/Users/prabhatrao/Downloads/tracker_response.bin"));
        int index = 0;

        if (response[index++] != 'd') return; // Not a dictionary

        while (index < response.length) {
            // Read key length
            int colon = index;
            while (response[colon] != ':') colon++;
            int keyLen = Integer.parseInt(new String(response, index, colon - index));
            index = colon + 1;
            String key = new String(response, index, keyLen);
            index += keyLen;

            // If key starts with "peers"
            if (key.startsWith("peers")) {
                // Read length of peer data
                colon = index;
                while (response[colon] != ':') colon++;
                int length = Integer.parseInt(new String(response, index, colon - index));
                index = colon + 1;

                // Decode peers
                byte[] peers = Arrays.copyOfRange(response, index, index + length);
                index += length;
                for (int i = 0; i + 5 < peers.length; i += 6) {
                    int ip1 = peers[i] & 0xFF;
                    int ip2 = peers[i + 1] & 0xFF;
                    int ip3 = peers[i + 2] & 0xFF;
                    int ip4 = peers[i + 3] & 0xFF;
                    int port = ((peers[i + 4] & 0xFF) << 8) | (peers[i + 5] & 0xFF);
                    System.out.println(ip1 + "." + ip2 + "." + ip3 + "." + ip4 + ":" + port);
                }
            } else {
                // Skip non-peers values
                if (response[index] == 'i') {
                    index++; // skip 'i'
                    while (response[index] != 'e') index++;
                    index++; // skip 'e'
                }
            }

            if (response[index] == 'e') break;
        }
    }

}