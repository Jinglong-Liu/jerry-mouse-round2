package com.github.ljl.jerrymouse.support.classloader;

import com.github.ljl.jerrymouse.utils.FileUtils;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-06 19:35
 **/

public class WebApplicationClassLoader extends URLClassLoader {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final ClassLoader parent;
    private final String baseDir;
    private final Path classPath;
    private final List<Path> jarPaths;


    /**
     *
     * @param urls: urls[0] is baseDir
     * @param parent: classLoader
     */
    @SneakyThrows
    public WebApplicationClassLoader(@NotNull URL[] urls, @NotNull ClassLoader parent) {
        super(urls, parent);
        this.parent = parent;
        this.baseDir = urls[0].getPath();
        this.classPath =  Paths.get(FileUtils.buildFullPath(baseDir,  "/WEB-INF/classes/"));
        Path libPath = Paths.get(FileUtils.buildFullPath(baseDir,  "/WEB-INF/lib/"));
        if(FileUtils.exists(libPath.toFile().getAbsolutePath())) {
            this.jarPaths = Files.walk(libPath)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".jar"))
                    .collect(Collectors.toList());
        } else {
            jarPaths = Collections.emptyList();
        }
    }

    @Override
    public Class<?> loadClass(String className) {
        try {
            Class<?> clazz = parent.loadClass(className);
            if (Objects.isNull(clazz)) {
                throw new ClassNotFoundException();
            } else {
                return clazz;
            }
        } catch (ClassNotFoundException e) {
            try {
                return findClass(className);
            } catch (ClassNotFoundException ignored) {

            }
        }
        return null;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        try {
            Class clazz = parent.loadClass(className);
            if (Objects.isNull(clazz)) {
                throw new ClassNotFoundException();
            } else {
                return clazz;
            }
        } catch (ClassNotFoundException ignored) {

        }
        // Convert class name to file path
        String classFile = className.replace('.', '/') + ".class";
        Path classFilePath = classPath.resolve(classFile);

        if (Files.exists(classFilePath)) {
            try {
                byte[] classData = Files.readAllBytes(classFilePath);
                return defineClass(className, classData, 0, classData.length);
            } catch (IOException e) {
                throw new ClassNotFoundException("Could not load class " + className, e);
            }
        }

        for (Path jarPath : jarPaths) {
            try (JarFile jarFile = new JarFile(jarPath.toFile())) {
                JarEntry entry = jarFile.getJarEntry(classFile);
                if (entry != null) {
                    try (InputStream inputStream = jarFile.getInputStream(entry)) {
                        byte[] classData = toByteArray(inputStream);
                        return defineClass(className, classData, 0, classData.length);
                    }
                }
            } catch (IOException e) {
                System.out.println("load jar file err" + jarPath.getFileName());
                // Continue to the next JAR file
            }
        }


        // throw new ClassNotFoundException("Class " + className + " not found");
        logger.warn("Class " + className + "not found");
        return null;
    }

    private List<URL> findResourcesInDirectory(File dir) throws IOException {
        List<URL> resources = new ArrayList<>();
        if (Objects.isNull(dir)) {
            return resources;
        }

        if (dir.isDirectory()) {
            Files.walk(dir.toPath())
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            resources.add(path.toUri().toURL());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    });
        }
        return resources;
    }
    private List<URL> getClassesDirResources(String name) throws IOException {
        List<URL> resources = new ArrayList<>();
        File directory = new File(this.classPath.toFile().getAbsolutePath(), name);
        if (directory.exists()) {
            logger.debug(directory.getAbsolutePath() + " exist!!!");
            if (directory.isDirectory()) {
                // 查找指定目录及其子目录中的资源
                Files.walk(directory.toPath()).forEach(path -> {
                    File file = path.toFile();
                    if (file.exists()) {
                        try {
                            resources.add(file.toURI().toURL());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        return resources;
    }
    private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
}
