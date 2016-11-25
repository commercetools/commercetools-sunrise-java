package com.commercetools.sunrise.theme;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class WebjarsFilesCopierTest {

    private static final String DEST_PATH = WebjarsFilesCopierTest.class.getClass().getResource("/").getPath() + "webjarsFilesCopierDestFolder";

    @Before
    @After
    public void deleteFolderWithAllCopiedFiles() throws Exception {
        final File directory = new File(DEST_PATH);
        FileUtils.deleteDirectory(directory);
    }

    @Test
    public void copiesFile() throws Exception {
        testCopy("file", copiedFile -> assertThat(copiedFile).exists().hasContent("level1"));
    }

    @Test
    public void copiesFileWithExistingDirectory() throws Exception {
        testCopy("folder/file", copiedFile -> assertThat(copiedFile).exists().hasContent("level2"));
    }

    @Test
    public void copiesFileWithNonExistingDirectory() throws Exception {
        testCopy("folder/folder/file", copiedFile -> assertThat(copiedFile).exists().hasContent("level3"));
    }

    @Test
    public void copiesFileFromAJarPackage() throws Exception {
        testCopy("inside/file", copiedFile -> assertThat(copiedFile).exists().hasContent("inside jar"));
    }

    @Test
    public void failsSilentlyOnInvalidFile() throws Exception {
        testCopy("wrong/file", copiedFile -> assertThat(copiedFile).doesNotExist());
    }

    @Test
    public void copiesMultipleFiles() throws Exception {
        testCopy(asList("file", "folder/file", "folder/folder/file"), copiedFiles ->
                copiedFiles.forEach(copiedFile -> assertThat(copiedFile).exists()));
    }

    @Test
    public void honorsFolderNamesWithSpaces() throws Exception {
        testCopy("folder/folder/evil & folder/folder 5/file", copiedFile -> assertThat(copiedFile).exists().hasContent("level 5"));
    }

    private void testCopy(final String originPath, final Consumer<File> test) {
        test.accept(copyFiles(singletonList(originPath)).get(0));
    }

    private void testCopy(final List<String> originPaths, final Consumer<List<File>> test) {
        test.accept(copyFiles(originPaths));
    }

    private static List<File> copyFiles(final List<String> originPaths) {
        WebjarsFilesCopier.copyTemplateFiles(DEST_PATH, originPaths);
        return originPaths.stream()
                .map(originPath -> new File(DEST_PATH + File.separator + originPath))
                .collect(toList());
    }
}
