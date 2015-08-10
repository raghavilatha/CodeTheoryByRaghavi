package com.sweety.listener;

import java.util.List;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.Utils;

import com.sweety.util.ErrorUtil;
public class TestsListenerAdapter implements IInvokedMethodListener {

	@SuppressWarnings("unchecked")
	@Override
	public void afterInvocation(final IInvokedMethod method,
			final ITestResult result) {
		Reporter.setCurrentTestResult(result);

		if (method.isTestMethod()) {

			final List<Throwable> verificationFailures = ErrorUtil.getVerificationFailures();
			// if there are verification failures...
			if (verificationFailures.size() != 0) {
				// set the test to failed
				result.setStatus(ITestResult.FAILURE);

				// if there is an assertion failure add it to
				// verificationFailures
				if (result.getThrowable() != null) {
					verificationFailures.add(result.getThrowable());
				}

				final int size = verificationFailures.size();
				// if there's only one failure just set that
				if (size == 1) {
					result.setThrowable(verificationFailures.get(0));
				} else {
					// create a failure message with all failures and stack
					// traces (except last failure)
					final StringBuffer failureMessage = new StringBuffer(
							"Multiple failures (").append(size).append("):nn");
					for (int i = 0; i < size - 1; i++) {
						failureMessage.append("Failure ").append(i + 1)
								.append(" of ").append(size).append(":n");
						final Throwable t = verificationFailures.get(i);
						final String fullStackTrace = Utils
								.stackTrace(t, false)[1];
						failureMessage.append(fullStackTrace).append("nn");
					}

					// final failure
					final Throwable last = verificationFailures.get(size - 1);
					failureMessage.append("Failure ").append(size)
							.append(" of ").append(size).append(":n");
					failureMessage.append(last.toString());

					// set merged throwable
					final Throwable merged = new Throwable(
							failureMessage.toString());
					merged.setStackTrace(last.getStackTrace());

					result.setThrowable(merged);

				}
			}

		}

	}

	@Override
	public void beforeInvocation(final IInvokedMethod arg0,
			final ITestResult arg1) {
	}

}
