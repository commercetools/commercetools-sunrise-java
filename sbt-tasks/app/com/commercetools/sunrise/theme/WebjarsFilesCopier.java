package com.commercetools.sunrise.theme;

import play.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

public class WebjarsFilesCopier {
    private static final Logger.ALogger LOGGER = Logger.of(WebjarsFilesCopier.class);
    private static final String WEBJARS_PATH = "META-INF/resources/webjars/";
    private static final FileSystem FILE_SYSTEM = FileSystems.getDefault();

    public static void main(String[] args) {
        if (args.length > 1) {
            final String destBasePath = args[0];
            final List<String> filesToCopy = Arrays.asList(args).subList(1, args.length);
            copyTemplateFiles(destBasePath, filesToCopy);
        } else {
            throw new RuntimeException("Missing arguments, required at least one origin path");
        }
    }

    static void copyTemplateFiles(final String destBasePath, final List<String> filesToCopy) {
        filesToCopy.forEach(fileToCopy -> {
            final String origFilePath = WEBJARS_PATH + fileToCopy;
            final InputStream origResource = Thread.currentThread().getContextClassLoader().getResourceAsStream(origFilePath);
            if (origResource != null) {
                final Path destPath = FILE_SYSTEM.getPath(destBasePath, fileToCopy);
                copyFile(origResource, destPath);
            } else {
                LOGGER.error("Could not find file \"{}\" in classpath", origFilePath);
            }
        });
    }

    private static void copyFile(final InputStream origResource, final Path destPath) {
        try {
            createDirectoryPath(destPath);
            Files.copy(origResource, destPath);
            LOGGER.info("Successfully created \"{}\"", destPath);
        } catch (FileAlreadyExistsException e) {
            LOGGER.error("File \"{}\" already exists, please delete it first if you want to replace it", destPath);
        } catch (IOException e) {
            LOGGER.error("Could not copy file to \"{}\"", destPath, e);
        }
    }

    private static void createDirectoryPath(final Path destPath) throws IOException {
        final Path parent = destPath.getParent();
        if (parent != null) {
            final File parentDir = parent.toFile();
            if (!parentDir.exists()) {
                final boolean mkdirsSucceeded = parentDir.mkdirs();
                if (!mkdirsSucceeded) {
                    throw new IOException("Could not create missing directories in path \"" + destPath + "\"");
                }
            }
        }
    }
}
