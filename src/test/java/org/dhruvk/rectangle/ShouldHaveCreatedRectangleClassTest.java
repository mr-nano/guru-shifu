package org.dhruvk.rectangle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.nio.file.Paths.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShouldHaveCreatedRectangleClassTest {

    //TODO - unable to add test for empty directory as git won't add it?

    @Test
    void shouldGiveFeedbackForDirectoryContainingNoJavaFiles(@TempDir File someFile) throws Exception {
        Path path = setupDirectoryStructure(someFile, "org/dhruvk/rectangle", "blah.txt");
        assertThat(findFeedbackFor(path), is(Optional.of("NO_JAVA_FILE_FOUND"))); // TODO - is it possible to get this as a key from resource bundle to prevent hardcoding...?
    }

    @Test
    void shouldGiveNoFeedbackForDirectoryContainingRectangleClassFile(@TempDir File someFile) throws Exception {
        Path path = setupDirectoryStructure(someFile, "org/dhruvk/rectangle", "Rectangle.java");
        assertThat(findFeedbackFor(path), is(Optional.empty()));
    }

    @Test
    void shouldGiveFeedbackForDirectoryContainingUnnecessaryClassFile(@TempDir File someFile) throws Exception {
        Path path = setupDirectoryStructure(someFile, "org/dhruvk/rectangle", "Rectangle.java");
        setupDirectoryStructure(someFile, "org/dhruvk/rectangle", "Blah.java");
        Optional<String> expected = Optional.of("UNNECESSARY_FILES_FOUND");
        assertThat(findFeedbackFor(path), is(expected));
    }

    @Test
    void shouldGiveFeedbackForIfNameOfTheClassDoesNotFollowJavaConventions(@TempDir File someFile) throws Exception {
        Path path = setupDirectoryStructure(someFile, "org/dhruvk/rectangle", "rectangle.java");
        Optional<String> expected = Optional.of("JAVA_FILE_NAMING_CONVENTIONS_NOT_FOLLOWED");
        assertThat(findFeedbackFor(path), is(expected));
    }

    @Test
    void shouldReportUnknownScenarioWhenSeeingATypoForNow(@TempDir File someFile) throws Exception {
        Path path = setupDirectoryStructure(someFile, "org/dhruvk/rectangle", "Recangle.java");
        assertThat(findFeedbackFor(path), is(Optional.of("UNKNOWN_SCENARIO")));
    }

    @Test
    void shouldRequireThatTheClientSendsAnAbsolutePath() {
        AssertionError assertionError = Assertions.assertThrows(AssertionError.class, () -> new ShouldHaveCreatedRectangleClass(get("some/relative/directory")));
        assertThat(assertionError.getMessage(), is("Expected absolute path, but looks like you passed a relative path -> some/relative/directory"));
    }

    Optional<String> findFeedbackFor(Path path) {
        return new ShouldHaveCreatedRectangleClass(path).suggestionKey();
    }

    // TODO - primitive obsessions with the strings, should see if there are better abstractions
    // TODO - irritating code...
    Path setupDirectoryStructure(File someTemporaryFile, String packageStructure, String fileNameWithExtension) throws Exception {
        File file = new File(someTemporaryFile.getAbsolutePath() + "/" + packageStructure);
        file.mkdirs();
        file.toPath().resolve(fileNameWithExtension);
        Files.write(file.toPath().resolve(fileNameWithExtension), List.of(""));
        assertTrue(Files.exists(Paths.get(file.getAbsolutePath() + "/" + fileNameWithExtension)));
        return file.toPath();
    }
}