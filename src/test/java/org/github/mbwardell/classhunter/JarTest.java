/*
Copyright 2021 M. Wardell
        Use of this source code is governed by an MIT-style
        license that can be found in the LICENSE file or at
        https://opensource.org/licenses/MIT
*/
package org.github.mbwardell.classhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JarTest {
    private Path testDataDir;

    @BeforeEach
    public void setup() {
        testDataDir = Paths.get(System.getProperty("testdatadir"));
    }

    @Test
    void testGetClasses() throws Exception {
        Path jar = testDataDir.resolve("org/apache/commons-lang3/commons-lang3-3.11.jar");
        List<String> classes = (new Jar(jar)).getClasses();

        assertThat(classes)
                .hasSize(339)
                .contains("org.apache.commons.lang3.CharSet");
    }
}
