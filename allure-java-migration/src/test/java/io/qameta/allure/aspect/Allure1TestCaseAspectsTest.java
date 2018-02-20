package io.qameta.allure.aspect;

import io.qameta.allure.Lifecycle;
import io.qameta.allure.model3.Label;
import io.qameta.allure.model3.Link;
import io.qameta.allure.model3.TestResult;
import io.qameta.allure.test.InMemoryResultsWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Issue;
import ru.yandex.qatools.allure.annotations.Issues;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.TestCaseId;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * eroshenkoam
 * 30.04.17
 */
@RunWith(Parameterized.class)
public class Allure1TestCaseAspectsTest {

    private InMemoryResultsWriter results;

    private Lifecycle lifecycle;

    @Before
    public void initLifecycle() {
        results = new InMemoryResultsWriter();
        lifecycle = new Lifecycle(results);
        Allure1TestCaseAspects.setLifecycle(lifecycle);

        final String uuid = UUID.randomUUID().toString();
        final TestResult result = new TestResult().setUuid(uuid);

        lifecycle.startTest(result);

        simpleTest.testSomething();

        lifecycle.stopTest();
        lifecycle.writeTest(uuid);
    }

    @Parameterized.Parameter
    public SimpleTest simpleTest;

    @Parameterized.Parameters
    public static Collection<Object[]> getTests() {
        return Arrays.asList(
                new Object[]{new JunitTest()},
                new Object[]{new TestNgTest()}
        );
    }

    @Test
    public void shouldProcessFeaturesAnnotation() {
        assertThat(results.getAllTestResults())
                .flatExtracting(TestResult::getLabels)
                .filteredOn(label -> label.getName().equals("feature"))
                .extracting(Label::getValue)
                .containsExactlyInAnyOrder("feature1", "feature2");
    }

    @Test
    public void shouldProcessStoriesAnnotation() {
        assertThat(results.getAllTestResults())
                .flatExtracting(TestResult::getLabels)
                .filteredOn(label -> label.getName().equals("story"))
                .extracting(Label::getValue)
                .containsExactlyInAnyOrder("story1", "story2");

    }

    @Test
    public void shouldProcessSeverityAnnotation() {
        assertThat(results.getAllTestResults())
                .flatExtracting(TestResult::getLabels)
                .filteredOn(label -> label.getName().equals("severity"))
                .extracting(Label::getValue)
                .containsExactlyInAnyOrder(SeverityLevel.CRITICAL.value());

    }

    @Test
    public void shouldProcessIssuesAnnotation() {
        assertThat(results.getAllTestResults())
                .flatExtracting(TestResult::getLinks)
                .extracting(Link::getName)
                .containsExactlyInAnyOrder("ISSUE-1", "ISSUE-11", "ISSUE-2", "ISSUE-22", "TEST-1");

    }

    @Test
    public void shouldProcessTitleAnnotation() {
        assertThat(results.getAllTestResults())
                .flatExtracting(TestResult::getName)
                .containsExactlyInAnyOrder("testcase");

        assertThat(results.getAllTestResults())
                .flatExtracting(TestResult::getLabels)
                .filteredOn(label -> label.getName().equals("suite"))
                .extracting(Label::getValue)
                .containsExactlyInAnyOrder("testsuite");
    }

    @Test
    public void shouldProcessDescriptionAnnotation() {
        assertThat(results.getAllTestResults())
                .flatExtracting(TestResult::getDescription)
                .containsExactlyInAnyOrder("testcase description");

    }

    public interface SimpleTest {

        void testSomething();

    }

    @Title("testsuite")
    @Issue("ISSUE-1")
    @Issues(@Issue("ISSUE-11"))
    @Stories("story1")
    @Features("feature1")
    public static class TestNgTest implements SimpleTest {

        @Title("testcase")
        @Description("testcase description")
        @Issue("ISSUE-2")
        @Issues(@Issue("ISSUE-22"))
        @TestCaseId("TEST-1")
        @Stories("story2")
        @Features("feature2")
        @Severity(SeverityLevel.CRITICAL)
        @org.testng.annotations.Test
        public void testSomething() {

        }

    }

    @Title("testsuite")
    @Issue("ISSUE-1")
    @Issues(@Issue("ISSUE-11"))
    @Stories("story1")
    @Features("feature1")
    public static class JunitTest implements SimpleTest {

        @Title("testcase")
        @Description("testcase description")
        @Issue("ISSUE-2")
        @Issues(@Issue("ISSUE-22"))
        @TestCaseId("TEST-1")
        @Stories("story2")
        @Features("feature2")
        @Severity(SeverityLevel.CRITICAL)
        @org.junit.Test
        public void testSomething() {

        }

    }
}
