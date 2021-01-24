package org.github.mbwardell.classhunter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Jar {
    private final Path jar;

    public Jar(Path jar) {
        this.jar = jar;
    }

    public List<String> getClasses() throws IOException {
        List<String> classNames = new ArrayList<String>();
        ZipInputStream zip = new ZipInputStream(Files.newInputStream(jar));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                String className = entry.getName().replace('/', '.'); // including ".class"
                classNames.add(className.substring(0, className.length() - ".class".length()));
            }
        }
        return classNames;
    }
}
