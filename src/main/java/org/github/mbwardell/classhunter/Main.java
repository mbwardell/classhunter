/*
Copyright 2021 M. Wardell
        Use of this source code is governed by an MIT-style
        license that can be found in the LICENSE file or at
        https://opensource.org/licenses/MIT
*/
package org.github.mbwardell.classhunter;

import org.github.mbwardell.classhunter.cli.VersionProvider;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "classhunter", mixinStandardHelpOptions = true, versionProvider=VersionProvider.class)
public class Main implements Callable<Integer> {
    @CommandLine.Option(names = {"-d", "--directory"}, description = "directory to search")
    Path searchDirectoryOption;

    @CommandLine.ArgGroup(multiplicity = "1")
    SearchMethod searchMethod;

    public static void main(String... args) {
        CommandLine app = new CommandLine(new Main());
        int exitCode = app.execute(args);
        System.exit(exitCode);
    }

    @Override
    @SuppressWarnings("java:S106")
    public Integer call() {
        try {
            Hunter.Method method = searchMethod.byJar ? Hunter.Method.JAR : Hunter.Method.MODULE;
            Path searchDirectoryPath = searchDirectoryOption == null ? Paths.get(".") : searchDirectoryOption;

            Hunter hunter = new Hunter(searchDirectoryPath, method);
            hunter.hunt(System.out);
            return 0;
        } catch (Exception ex) {
            System.out.println("Error hunting");
            ex.printStackTrace();
            return 1;
        }
    }

    static class SearchMethod {
        @CommandLine.Option(names = {"-m", "--modules"}, description = "Search by module")
        boolean byModule;

        @CommandLine.Option(names = {"-j", "--jars"}, description = "Search by jar")
        boolean byJar;
    }
}
