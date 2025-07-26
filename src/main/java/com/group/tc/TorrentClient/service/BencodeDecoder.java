package com.group.tc.TorrentClient.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

public class BencodeDecoder {
    private final InputStream in;


    public BencodeDecoder(InputStream in) {
        this.in = in;
    }

    public Object decode() throws IOException {
        int c = in.read();
        if (c == -1) throw new EOFException();
        if (c == 'i') return decodeInteger();
        if (c == 'l') return decodeList();
        if (c == 'd') return decodeDictionary();
        if (Character.isDigit(c)) return decodeString(c);
        throw new IOException("Invalid bencode format");
    }

    private Long decodeInteger() throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = in.read()) != 'e') {
            if (c == -1) throw new EOFException();
            sb.append((char) c);
        }
        return Long.parseLong(sb.toString());
    }

    private String decodeString(int firstDigit) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append((char) firstDigit);
        int c;
        while ((c = in.read()) != ':') {
            if (c == -1) throw new EOFException();
            sb.append((char) c);
        }
        int length = Integer.parseInt(sb.toString());
        byte[] buf = new byte[length];
        int read = in.read(buf);
        if (read != length) throw new EOFException();
        return new String(buf);
    }

    private List<Object> decodeList() throws IOException {
        List<Object> list = new ArrayList<>();
        int c;
        while ((c = in.read()) != 'e') {
            if (c == -1) throw new EOFException();
            list.add(decodeElement(c));
        }
        return list;
    }

    private Map<String, Object> decodeDictionary() throws IOException {
        Map<String, Object> map = new LinkedHashMap<>();
        int c;
        while ((c = in.read()) != 'e') {
            if (c == -1) throw new EOFException();
            String key = (String) decodeElement(c);
            Object value = decode();
            map.put(key, value);
        }
        return map;
    }

    private Object decodeElement(int c) throws IOException {
        if (c == 'i') return decodeInteger();
        if (c == 'l') return decodeList();
        if (c == 'd') return decodeDictionary();
        if (Character.isDigit(c)) return decodeString(c);
        throw new IOException("Invalid bencode element");
    }
}