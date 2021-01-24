package org.github.mbwardell.classhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class HunterTest {
    private Path testDataDir;

    @BeforeEach
    public void setup() {
        testDataDir = Paths.get(System.getProperty("testdatadir"));
    }

    @Test
    public void testByModule() throws Exception {
        Hunter hunter = new Hunter(testDataDir, Hunter.Method.MODULE);

        String output;
        try (final ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            try (PrintStream printStream = new PrintStream(byteStream, true, StandardCharsets.UTF_8.name())) {
                hunter.hunt(printStream);
            }
            output = byteStream.toString(StandardCharsets.UTF_8.name());
        }

        assertThat(output.split("\n"))
                .hasSize(1222)
                .contains("org.apache.commons.lang3: org.apache.commons.lang3.CharRange$CharacterIterator")
                .contains("org.apache.commons.other: org.apache.commons.compress.MemoryLimitException");
    }

    @Test
    public void testByJar() throws Exception {
        Hunter hunter = new Hunter(testDataDir, Hunter.Method.JAR);

        String output;
        try (final ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            try (PrintStream printStream = new PrintStream(byteStream, true, StandardCharsets.UTF_8.name())) {
                hunter.hunt(printStream);
            }
            output = byteStream.toString(StandardCharsets.UTF_8.name());
        }

        assertThat(output.split("\n"))
                .hasSize(1222)
                .contains("org/apache/commons-lang3/commons-lang3-3.11.jar: org.apache.commons.lang3.ArrayUtils")
                .contains("org/apache/commons-other/commons-collections4-4.4.jar: org.apache.commons.collections4.Factory");
    }
}
