package listeners;

import org.testng.*;

public class RetryAnalyzer implements IRetryAnalyzer {

	private int attempt = 0;
	private static final int MAX_RETRY = 2; // up to 2 retries (3 runs)

	@Override
	public boolean retry(ITestResult result) {
		if (attempt < MAX_RETRY) {
			attempt++;
			return true;
		}
		return false;
	}
}