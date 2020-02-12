package io.lightningbug.testlisteners;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import io.lightningbug.domain.InfrastructureInfo;
import io.lightningbug.domain.TestExecutionInfo;
import io.lightningbug.domain.TestSuiteInfo;

public class JUnitRunListener extends RunListener {
	private InfrastructureInfo infraInfo;
	private ZonedDateTime startTime;
	private ZonedDateTime endTime;
	private TestSuiteInfo testSuiteInfo;

	/**
	 * Called before any tests have been run.
	 */
	public void testRunStarted(Description description) throws java.lang.Exception {
		
		this.infraInfo = new InfrastructureInfo();
		this.startTime = ZonedDateTime.now();
		System.out.println("This is a suite" + description.isSuite());
		if (description.isSuite()) {
			this.testSuiteInfo = new TestSuiteInfo(description.getDisplayName(), ZonedDateTime.now());
			description.getChildren().forEach(System.out::println);
		}
		System.out.println(this.infraInfo.toString());
		System.out.println(this.startTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
				+ " ::::: Number of tests to execute : " + description.testCount());
	}

	/**
	 * Called when all tests have finished
	 */
	public void testRunFinished(Result result) throws java.lang.Exception {
		this.endTime = ZonedDateTime.now();
		System.out.println("Test Suite Time Elapsed " + Duration.between(startTime, endTime).getSeconds() + " seconds");
		this.testSuiteInfo.setEndTime(ZonedDateTime.now());
		System.out.println(this.testSuiteInfo);
	}

	/**
	 * Called when an atomic test is about to be started.
	 */
	public void testStarted(Description description) throws java.lang.Exception {
		this.testSuiteInfo.addTestExecution(new TestExecutionInfo(description.getDisplayName(), ZonedDateTime.now()));
	}

	/**
	 * Called when an atomic test has finished, whether the test succeeds or fails.
	 */
	public void testFinished(Description description) throws java.lang.Exception {
		System.out.println("Finished execution of test case : " + description.getMethodName());
		if (description.getChildren() != null && description.getChildren().size() == 1) {
			Description child = description.getChildren().get(0);
			System.out.println("This is the child" + child.getDisplayName() + " :" + child.getMethodName());
		}
	}

	/**
	 * Called when an atomic test fails.
	 */
	public void testFailure(Failure failure) throws java.lang.Exception {
		
		System.out.println("Execution of test case failed : " + failure.getMessage());
		System.out.println("Failure " + failure.getTrace());
	}

	/**
	 * Called when a test will not be run, generally because a test method is
	 * annotated with Ignore.
	 */
	public void testIgnored(Description description) throws java.lang.Exception {
		System.out.println("Execution of test case ignored : " + description.getMethodName());
	}
}
