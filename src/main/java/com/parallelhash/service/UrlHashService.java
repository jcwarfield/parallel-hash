package com.parallelhash.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This service reads in a text file containing a list of URLs,
 * downloads the url content, and performs an MD5 on the
 * content in parallel.
 */
@Service
public class UrlHashService {

    @Value("${max.thread.count}")
    private Integer maxThreadCount = 1;

    @Value("${url.input.file}")
    private String urlInputFile = "";

    private final String HASH_ALGORITHM = "MD5";

    /**
     * Reads the input text file of URLs and outputs
     * the list of calculated hashes to stdout in the order they
     * appear in the input file. url content are read and
     * hashes are calculated in parallel.
     * Throws:      ExecutionException.
     * Throws:      InterruptedException.
     */
    public void generateUrlHashes() {

        List<String> urlList = inputFileToList();

        int availProcs = Runtime.getRuntime().availableProcessors();

        if(maxThreadCount == null || maxThreadCount == 0) {
            maxThreadCount = Math.min(urlList.size(), availProcs);
        } else if (maxThreadCount > urlList.size()) {
            maxThreadCount = urlList.size();
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool(maxThreadCount);

        ForkJoinTask<List<String>> hashes = forkJoinPool.submit(() -> urlList.parallelStream().map(url -> {
            try {
                return getUrlHash(url);
            } finally {
                forkJoinPool.shutdown();
            }
        }).collect(Collectors.toList()));

        try {
            hashes.get().forEach(System.out::println);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the URLs in the input file into a list for processing
     * @return      list of url strings
     * Throws:      URISyntaxException – if url is malformed.
     * Throws:      IOException – if an I/O error occurs.
     */
    protected List<String> inputFileToList() {
        List<String> urlList = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        try  {
            Stream<String> lines = Files.lines(Path.of(String.valueOf(new File(classLoader.getResource(urlInputFile).toURI()))));
            urlList = lines.collect(Collectors.toList());
        } catch (URISyntaxException | IOException e){
            e.printStackTrace();
        }
        return urlList;
    }

    /**
     * Reads url content and calculates hash
     * @param  url  an absolute url
     * @return      hash string
     */
    private String getUrlHash(String url) {
        System.out.printf("Executing request to url %s on thread %s%n", url, Thread.currentThread().getName());
        return calculateHash(getUrlContent(url));
    }

    /**
     * Gets content from url as InputStream
     * @param  url  an absolute url
     * @return      InputStream
     * Throws:      IOException – if an I/O error occurs.
     */
    private InputStream getUrlContent(String url) {
        InputStream inputStream = null;
        try {
             inputStream = new URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * Converts InputStream to byte array in preparation
     * for hashing then hashes with supplied algorithm specifier
     * @param  inputStream  stream containing content to be hashed
     * @return              hash string
     * Throws:              IOException – if an I/O error occurs.
     * Throws:              NoSuchAlogrithmException – if invalid algorithm is specified.
     */
    protected String calculateHash(InputStream inputStream) {
        byte[] digest = new byte[0];
        try {
            byte[] streamBytes = inputStream.readAllBytes();
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            messageDigest.update(streamBytes);
            digest = messageDigest.digest();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return DatatypeConverter.printHexBinary(digest).toLowerCase();
    }
}
