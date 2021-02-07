/*
Copyright 2021 M. Wardell
        Use of this source code is governed by an MIT-style
        license that can be found in the LICENSE file or at
        https://opensource.org/licenses/MIT
*/
package org.github.mbwardell.classhunter;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JBossModule {

    private final Path moduleXmlPath;
    private List<String> classes = new ArrayList<>();
    private List<Path> jars;
    private String name;
    private boolean loaded;

    public JBossModule(Path moduleXmlPath) {
        this.moduleXmlPath = moduleXmlPath;
    }

    List<String> getClasses() throws ModuleReadException {
        load();
        return classes;
    }

    List<Path> getJars() throws ModuleReadException {
        load();
        return jars;
    }

    String getName() throws ModuleReadException {
        load();
        return name;
    }

    private void load() throws ModuleReadException {
        if (!loaded) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(moduleXmlPath.toFile());
                name = doc.getDocumentElement().getAttribute("name");

                jars = new ArrayList<>();
                NodeList resourceRoots = doc.getDocumentElement().getElementsByTagName("resource-root");
                for (int i = 0; i < resourceRoots.getLength(); i++) {
                    Node resourceRoot = resourceRoots.item(i);
                    if (resourceRoot.getNodeType() == Node.ELEMENT_NODE) {
                        String jarName = resourceRoot.getAttributes().getNamedItem("path").getNodeValue();
                        Path jarFile = moduleXmlPath.getParent().resolve(jarName);
                        jars.add(jarFile);
                    }
                }

                classes = new ArrayList<>();
                for (Path jar : jars) {
                    Jar jarFile = new Jar(jar);
                    classes.addAll(jarFile.getClasses());
                }

            } catch (Exception e) {
                throw new ModuleReadException("Failed to read module", e);
            }
            loaded = true;
        }
    }

    static class ModuleReadException extends Exception {
        public ModuleReadException(String message, Exception e) {
            super(message, e);
        }
    }
}
