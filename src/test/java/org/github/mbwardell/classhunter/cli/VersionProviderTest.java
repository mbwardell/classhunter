/*
Copyright 2021 M. Wardell
        Use of this source code is governed by an MIT-style
        license that can be found in the LICENSE file or at
        https://opensource.org/licenses/MIT
*/
package org.github.mbwardell.classhunter.cli;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VersionProviderTest {
    @Test
    void testReadVersion() throws Exception {
        CommandLine.IVersionProvider vp = new VersionProvider();
        String[] versions = vp.getVersion();
        assertEquals(1, versions.length);
        assertEquals("ClassHunter, version: Development", versions[0]);
    }
}
