package io.lightningbug.domain;

import static org.mockito.Mockito.mock;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Test;

import io.lightningbug.domain.TestExecutionInfo;
import io.lightningbug.domain.TestSuiteInfo;

public class TestSuiteInfoTest {

	private static final String GMT_5 = "GMT+5";
	private static final String LEAP_DAY = "2020-02-29T00:00:00+05:00[GMT+05:00]";
	private static final String TEST = "Test";

	@Test(expected = IllegalArgumentException.class)
	public void createTestSubjectWithNulls() {
		new TestSuiteInfo(null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void createTestSubjectWithNullStartTime() {
		new TestSuiteInfo(TEST, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void createTestSubjectWithNullName() {
		new TestSuiteInfo(null, ZonedDateTime.now());
		Assert.fail();
	}

	@Test
	public void createTestSubjectWithNonNullNameAndStartTime() {
		TestSuiteInfo testSubject = new TestSuiteInfo(TEST, ZonedDateTime.now());
		Assert.assertNotNull(testSubject);
	}

	@Test
	public void testGetStartTime() throws Exception {
		TestSuiteInfo testSubject = new TestSuiteInfo(TEST,
				ZonedDateTime.of(2020, 2, 29, 0, 0, 0, 0, ZoneId.of(GMT_5)));
		Assert.assertEquals(testSubject.getStartTime(), LEAP_DAY);
	}

	@Test
	public void testGetEndTimeWhenNull() throws Exception {
		TestSuiteInfo testSubject = new TestSuiteInfo(TEST,
				ZonedDateTime.of(2020, 2, 29, 0, 0, 0, 0, ZoneId.of(GMT_5)));
		Assert.assertEquals(testSubject.getEndTime(), "");
	}

	
	@Test
	public void testSetEndTime() throws Exception {
		TestSuiteInfo testSubject = new TestSuiteInfo(TEST,
				ZonedDateTime.of(2020, 2, 29, 0, 0, 0, 0, ZoneId.of(GMT_5)));
		testSubject.setEndTime(ZonedDateTime.of(2020, 2, 29, 0, 0, 0, 0, ZoneId.of(GMT_5)));
		Assert.assertEquals(testSubject.getEndTime(), LEAP_DAY);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetEndTimeTwice() throws Exception {
		TestSuiteInfo testSubject = new TestSuiteInfo(TEST,
				ZonedDateTime.of(2020, 2, 29, 0, 0, 0, 0, ZoneId.of(GMT_5)));
		testSubject.setEndTime(ZonedDateTime.of(2020, 2, 29, 0, 0, 0, 0, ZoneId.of(GMT_5)));
		testSubject.setEndTime(ZonedDateTime.of(2020, 2, 28, 0, 0, 0, 0, ZoneId.of(GMT_5)));
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetEndTimeAsNull() throws Exception {
		TestSuiteInfo testSubject = new TestSuiteInfo(TEST,
				ZonedDateTime.of(2020, 2, 29, 0, 0, 0, 0, ZoneId.of(GMT_5)));
		testSubject.setEndTime(null);
		Assert.fail();
	}

	@Test
	public void testGetName() throws Exception {
		TestSuiteInfo testSubject = new TestSuiteInfo(TEST, ZonedDateTime.now());
		Assert.assertEquals(testSubject.getName(), TEST);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddTestExecutionTwice() throws Exception {
		TestSuiteInfo testSubject = new TestSuiteInfo(TEST, ZonedDateTime.now());
		TestExecutionInfo tei = mock(TestExecutionInfo.class);
		testSubject.addTestExecution(tei);
		testSubject.addTestExecution(tei);
		Assert.fail();
	}

	@Test
	public void testGetTestExecutionsWithNull() throws Exception {
		TestSuiteInfo testSubject = new TestSuiteInfo(TEST, ZonedDateTime.now());
		Assert.assertEquals(testSubject.getTestExecutions().size(), 0);
		testSubject.addTestExecution(mock(TestExecutionInfo.class));
		Assert.assertEquals(testSubject.getTestExecutions().size(), 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetTestExecutions() throws Exception {
		TestSuiteInfo testSubject = new TestSuiteInfo(TEST, ZonedDateTime.now());
		testSubject.addTestExecution(null);
		Assert.fail();
	}

}