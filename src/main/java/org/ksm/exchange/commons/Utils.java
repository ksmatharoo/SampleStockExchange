package org.ksm.exchange.commons;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class Utils {

    public static List<String> readTextFile(String fileName) {
        List<String> list = new ArrayList<>();
        Class klass = Utils.class;
        String classname = klass.getSimpleName() + ".class";
        String classPath = klass.getResource(classname).toString();
        if (classPath.startsWith("jar")) {
            final String pathInsideJar = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/" + fileName;
            log.info("Reading resources from jar {}", pathInsideJar);
            try {
                InputStream inputStream = new URL(pathInsideJar).openStream();
                list = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines().collect(Collectors.toList());
                ;

            } catch (IOException e) {
                log.error("error while reading file from jar {}", e.toString());
            }

        } else {
            try {
                String path = "src/main/resources/" + fileName;
                log.info("Reading local resources from {}", path);
                list = Files.lines(Paths.get(path))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                log.error("error while reading file from file system {}", e.toString());
            }
        }
        return list;
    }

}
