/*
Copyright 2021 M. Wardell
        Use of this source code is governed by an MIT-style
        license that can be found in the LICENSE file or at
        https://opensource.org/licenses/MIT
*/
package org.github.mbwardell.classhunter;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Hunter {
    private final Path searchDirectory;
    private final Method method;

    public Hunter(Path searchDirectory, Method method) {
        this.method = method;
        this.searchDirectory = searchDirectory;
    }

    void hunt(PrintStream outputStream) throws IOException, JBossModule.ModuleReadException {
        if (method == Method.JAR) {
            huntJars(outputStream);
        } else if (method == Method.MODULE) {
            huntModules(outputStream);
        }
    }

    private void huntModules(PrintStream outputStream) throws IOException, JBossModule.ModuleReadException {
        Path moduleFilename = Paths.get("module.xml");
        try (Stream<Path> pathStream = Files.walk(searchDirectory)) {
            List<Path> moduleFiles = pathStream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().equals(moduleFilename))
                    .sorted(Comparator.comparing(Path::toString))
                    .collect(Collectors.toList());

            for (Path modulePath : moduleFiles) {
                JBossModule jbossModule = new JBossModule(modulePath);
                List<String> sortedClasses = jbossModule.getClasses().stream().sorted().collect(Collectors.toList());
                for (String className : sortedClasses) {
                    outputStream.printf("%s: %s%n", jbossModule.getName(), className);
                }
            }
        }
    }

    private void huntJars(PrintStream outputStream) throws IOException {
        try (Stream<Path> pathStream = Files.walk(searchDirectory)) {
            List<Path> jarPaths = pathStream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith("jar"))
                    .sorted(Comparator.comparing(Path::toString))
                    .collect(Collectors.toList());

            for (Path jarPath : jarPaths) {
                Jar jar = new Jar(jarPath);
                jar.getClasses().stream().sorted().forEach(className ->
                        outputStream.printf("%s: %s%n", searchDirectory.relativize(jarPath), className));
            }
        }
    }

    public enum Method {MODULE, JAR}
}
