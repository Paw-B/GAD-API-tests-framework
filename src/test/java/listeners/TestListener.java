package listeners;

import java.lang.reflect.*;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;

@Log4j2
public class TestListener implements ITestListener, IAnnotationTransformer {

	// attaches RetryAnalyzer to all @Test automatically
	@Override
	public void transform(ITestAnnotation annotation, Class testClass,
			Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}

	@Override
	public void onTestStart(ITestResult result) {
		log.info("▶ Test started: {}", result.getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		log.info("✅ Passed: {}", result.getName());
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		log.warn("⏭ Skipped: {}", result.getName());
	}

	// Tests recovered by RetryAnalyzer (failed then passed) must not fail the build
	@Override
	public void onFinish(ITestContext context) {
		List<ITestResult> recovered = new ArrayList<>();
		for (ITestResult failed : context.getFailedTests().getAllResults()) {
			boolean passedOnRetry = context.getPassedTests().getAllResults().stream()
					.anyMatch(passed -> passed.getMethod().getMethodName().equals(failed.getMethod().getMethodName())
							&& Arrays.equals(passed.getParameters(), failed.getParameters()));
			if (passedOnRetry) {
				recovered.add(failed);
				log.warn("⚑ Flaky (passed on retry): {}", failed.getName());
			}
		}
		recovered.forEach(result -> context.getFailedTests().removeResult(result));
	}
}