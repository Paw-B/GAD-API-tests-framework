package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;


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