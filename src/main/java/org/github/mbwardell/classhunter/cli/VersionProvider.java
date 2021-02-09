/*
Copyright 2021 M. Wardell
        Use of this source code is governed by an MIT-style
        license that can be found in the LICENSE file or at
        https://opensource.org/licenses/MIT
*/
package org.github.mbwardell.classhunter.cli;

import org.github.mbwardell.classhunter.Main;
import picocli.CommandLine;

import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class VersionProvider implements CommandLine.IVersionProvider {
    @Override
    public String[] getVersion() throws Exception {
        try (InputStream manifestStream = Main.class.getResource("/META-INF/MANIFEST.MF").openStream()) {
            Manifest manifest = new Manifest(manifestStream);
            Attributes attributes = manifest.getMainAttributes();
            String title = attributes.getValue("Implementation-Title");
            String version = attributes.getValue("Implementation-Version");
            String message = String.format("%s, version: %s", title, version);
            return new String[]{message};
        }
    }
}
