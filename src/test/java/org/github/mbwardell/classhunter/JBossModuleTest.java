package org.github.mbwardell.classhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JBossModuleTest {
    private Path testDataDir;

    @BeforeEach
    public void setup() {
        testDataDir = Paths.get(System.getProperty("testdatadir"));
    }

    @Test
    void testModuleReading() throws Exception {
        Path moduleXml = testDataDir.resolve("org/apache/commons-other/module.xml");
        assertThat(Files.exists(moduleXml)).isTrue();

        JBossModule jm = new JBossModule(moduleXml);
        assertThat(jm.getName()).isEqualTo("org.apache.commons.other");

        List<Path> jars = jm.getJars();
        assertThat(jars.get(1).getFileName()).hasToString("commons-collections4-4.4.jar");

        List<String> classes = jm.getClasses();
        assertThat(classes).hasSize(883).contains("org.apache.commons.compress.utils.Charsets");
    }
}
