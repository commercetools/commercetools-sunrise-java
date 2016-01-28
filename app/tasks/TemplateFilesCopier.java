package tasks;

import play.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class TemplateFilesCopier {
    private static final String WEBJARS_PATH = "META-INF/resources/webjars/";

    public static void main(String[] args) {
        if (args.length > 1) {
            final String destBasePath = args[0];
            final List<String> filesToCopy = Arrays.asList(args).subList(1, args.length);
            copyTemplateFiles(destBasePath, filesToCopy);
        } else {
            throw new RuntimeException("Missing arguments, required at least one origin path");
        }
    }

    private static void copyTemplateFiles(final String destBasePath, final List<String> filesToCopy) {
        filesToCopy.forEach(filePath -> {
            final Path destPath = FileSystems.getDefault().getPath(destBasePath, filePath);
            final String origPath = WEBJARS_PATH + filePath;
            copyFile(destPath, origPath);
        });
    }

    private static void copyFile(final Path destPath, final String origPath) {
        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(origPath);
        if (inputStream != null) {
            try {
                Files.copy(inputStream, destPath);
                Logger.info("Successfully created \"{}\"", destPath);
            } catch (FileAlreadyExistsException e) {
                Logger.error("File \"{}\" already exists, please delete it first if you want to replace it", destPath);
            } catch (IOException e) {
                Logger.error("Could not copy file to \"{}\"", destPath, e);
            }
        } else {
            Logger.error("Could not find file \"{}\" in classpath", origPath);
        }
    }
}
