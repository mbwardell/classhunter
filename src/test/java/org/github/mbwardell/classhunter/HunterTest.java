/*
Copyright 2021 M. Wardell
        Use of this source code is governed by an MIT-style
        license that can be found in the LICENSE file or at
        https://opensource.org/licenses/MIT
*/
package org.github.mbwardell.classhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class HunterTest {
    private Path testDataDir;

    @BeforeEach
    public void setup() {
        testDataDir = Paths.get(System.getProperty("testdatadir"));
    }

    @Test
    void testByModule() throws Exception {
        Hunter hunter = new Hunter(testDataDir, Hunter.Method.MODULE);

        String output;
        try (final ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            try (PrintStream printStream = new PrintStream(byteStream, true, StandardCharsets.UTF_8.name())) {
                hunter.hunt(printStream);
            }
            output = byteStream.toString(StandardCharsets.UTF_8.name());
        }

        assertThat(output.split(System.lineSeparator()))
                .hasSize(1222)
                .contains(fixPaths("org.apache.commons.lang3: org.apache.commons.lang3.CharRange$CharacterIterator"))
                .contains(fixPaths("org.apache.commons.other: org.apache.commons.compress.MemoryLimitException"));
    }

    @Test
    void testByJar() throws Exception {
        Hunter hunter = new Hunter(testDataDir, Hunter.Method.JAR);

        String output;
        try (final ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            try (PrintStream printStream = new PrintStream(byteStream, true, StandardCharsets.UTF_8.name())) {
                hunter.hunt(printStream);
            }
            output = byteStream.toString(StandardCharsets.UTF_8.name());
        }

        assertThat(output.split(System.lineSeparator()))
                .hasSize(1222)
                .contains(fixPaths("org/apache/commons-lang3/commons-lang3-3.11.jar: org.apache.commons.lang3.ArrayUtils"))
                .contains(fixPaths("org/apache/commons-other/commons-collections4-4.4.jar: org.apache.commons.collections4.Factory"));
    }

    private String fixPaths(String in) {
        return in.replace("/", File.separator);
    }
}
