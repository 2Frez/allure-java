package io.qameta.allure;

import io.qameta.allure.model.Label;
import io.qameta.allure.writer.FileSystemResultsWriter;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

/**
 * The class contains some useful methods to work with {@link AllureLifecycle}.
 */
public final class Allure {

    @SuppressWarnings("PMD.UnusedPrivateField")
    private static final String TXT_EXTENSION = ".txt";
    @SuppressWarnings("PMD.UnusedPrivateField")
    private static final String TEXT_PLAIN = "text/plain";

    private static Lifecycle lifecycle;

    private Allure() {
        throw new IllegalStateException("Do not instance");
    }

    public static Lifecycle getLifecycle() {
        if (Objects.isNull(lifecycle)) {
            final String dir = System.getProperty("allure.results.directory", "allure-results");
            lifecycle = new Lifecycle(new FileSystemResultsWriter(Paths.get(dir)));
        }
        return lifecycle;
    }

    public static void addLabels(final Label... labels) {
        getLifecycle().updateTest(testResult -> testResult.getLabels().addAll(Arrays.asList(labels)));
    }

    public static void addLinks(final io.qameta.allure.model.Link... links) {
        getLifecycle().updateTest(testResult -> testResult.getLinks().addAll(Arrays.asList(links)));
    }

    public static void addDescription(final String description) {
        getLifecycle().updateTest(executable -> executable.setDescription(description));
    }

    public static void addDescriptionHtml(final String descriptionHtml) {
        getLifecycle().updateTest(executable -> executable.setDescriptionHtml(descriptionHtml));
    }

    public static void setLifecycle(final Lifecycle lifecycle) {
        Allure.lifecycle = lifecycle;
    }

    /*
    public static void addAttachment(final String name, final String content) {
        getLifecycle().addAttachment(name, TEXT_PLAIN, TXT_EXTENSION, content.getBytes(StandardCharsets.UTF_8));
    }

    public static void addAttachment(final String name, final String type, final String content) {
        getLifecycle().addAttachment(name, type, TXT_EXTENSION, content.getBytes(StandardCharsets.UTF_8));
    }

    @SuppressWarnings("PMD.UseObjectForClearerAPI")
    public static void addAttachment(final String name, final String type,
                                     final String content, final String fileExtension) {
        getLifecycle().addAttachment(name, type, fileExtension, content.getBytes(StandardCharsets.UTF_8));
    }

    public static void addAttachment(final String name, final InputStream content) {
        getLifecycle().addAttachment(name, null, null, content);
    }

    @SuppressWarnings("PMD.UseObjectForClearerAPI")
    public static void addAttachment(final String name, final String type,
                                     final InputStream content, final String fileExtension) {
        getLifecycle().addAttachment(name, type, fileExtension, content);
    }

    public static CompletableFuture<byte[]> addByteAttachmentAsync(
            final String name, final String type, final Supplier<byte[]> body) {
        return addByteAttachmentAsync(name, type, "", body);
    }

    public static CompletableFuture<byte[]> addByteAttachmentAsync(
            final String name, final String type, final String fileExtension, final Supplier<byte[]> body) {
        final String source = getLifecycle().prepareAttachment(name, type, fileExtension);
        return supplyAsync(body).whenComplete((result, ex) ->
                getLifecycle().writeAttachment(source, new ByteArrayInputStream(result)));
    }

    public static CompletableFuture<InputStream> addStreamAttachmentAsync(
            final String name, final String type, final Supplier<InputStream> body) {
        return addStreamAttachmentAsync(name, type, "", body);
    }

    public static CompletableFuture<InputStream> addStreamAttachmentAsync(
            final String name, final String type, final String fileExtension, final Supplier<InputStream> body) {
        final String source = lifecycle.prepareAttachment(name, type, fileExtension);
        return supplyAsync(body).whenComplete((result, ex) -> lifecycle.writeAttachment(source, result));
    }*/
}
