package io.lightningbug.domain;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;
import org.junit.Assert;

public class TestExecutionInfoTest {

	@Test(expected = IllegalArgumentException.class)
	public void TestExecutionInfoConstructorWithEmptyName() {
		new TestExecutionInfo("", ZonedDateTime.now());
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestExecutionInfoConstructorWithNullName() {
		new TestExecutionInfo(null, ZonedDateTime.now());
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestExecutionInfoConstructorWithNullTime() {
		new TestExecutionInfo("test", null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestExecutionInfoConstructorWithNulls() {
		new TestExecutionInfo(null, null);
		Assert.fail();
	}

	@Test
	public void TestExecutionInfoConstructor() {
		TestExecutionInfo testSubject = new TestExecutionInfo("test", ZonedDateTime.now());
		Assert.assertNotNull(testSubject);
	}

	@Test
	public void testGetandSetStartTime() throws Exception {
		ZonedDateTime now = ZonedDateTime.now();
		TestExecutionInfo testSubject = new TestExecutionInfo("test", now);
		Assert.assertEquals(now.format(DateTimeFormatter.ISO_ZONED_DATE_TIME), testSubject.getStartTime());
	}

	@Test
	public void testGetEndTime() throws Exception {
		ZonedDateTime now = ZonedDateTime.now();
		TestExecutionInfo testSubject = new TestExecutionInfo("test", now);
		testSubject.setEndTime(now);
		Assert.assertEquals(now.format(DateTimeFormatter.ISO_ZONED_DATE_TIME), testSubject.getEndTime());
	}

	@Test
	public void testGetEndTimeWhenUnset() throws Exception {
		ZonedDateTime now = ZonedDateTime.now();
		TestExecutionInfo testSubject = new TestExecutionInfo("test", now);
		Assert.assertEquals("", testSubject.getEndTime());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetEndTimeToNull() throws Exception {
		ZonedDateTime now = ZonedDateTime.now();
		TestExecutionInfo testSubject = new TestExecutionInfo("test", now);
		testSubject.setEndTime(null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetEndTimeWhenAlreadySet() throws Exception {
		ZonedDateTime now = ZonedDateTime.now();
		TestExecutionInfo testSubject = new TestExecutionInfo("test", now);
		testSubject.setEndTime(now);
		testSubject.setEndTime(now);
		Assert.fail();
	}

	@Test
	public void testIsSignalWhenUnsetIsFalse() throws Exception {
		ZonedDateTime now = ZonedDateTime.now();
		TestExecutionInfo testSubject = new TestExecutionInfo("test", now);
		Assert.assertEquals(false, testSubject.isSignal());
	}

	@Test
	public void testIsSignalWhenSetTrue() throws Exception {
		ZonedDateTime now = ZonedDateTime.now();
		TestExecutionInfo testSubject = new TestExecutionInfo("test", now);
		testSubject.setSignal(true);
		Assert.assertEquals(true, testSubject.isSignal());
	}

	@Test
	public void testIsSignalWhenSetFalse() throws Exception {
		ZonedDateTime now = ZonedDateTime.now();
		TestExecutionInfo testSubject = new TestExecutionInfo("test", now);
		testSubject.setSignal(false);
		Assert.assertEquals(false, testSubject.isSignal());
	}

	@Test
	public void testGetName() throws Exception {
		ZonedDateTime now = ZonedDateTime.now();
		TestExecutionInfo testSubject = new TestExecutionInfo("test", now);
		Assert.assertEquals("test", testSubject.getName());
	}

	@Test
	public void testGetMutationScoreWhenUnsetIsZero() throws Exception {
		ZonedDateTime now = ZonedDateTime.now();
		TestExecutionInfo testSubject = new TestExecutionInfo("test", now);
		Assert.assertEquals(0, testSubject.getMutationScore());
	}

	@Test
	public void testGetMutationScoreWhenSetGreaterThanZero() throws Exception {
		int mutationScore = 4;
		ZonedDateTime now = ZonedDateTime.now();
		TestExecutionInfo testSubject = new TestExecutionInfo("test", now);
		testSubject.setMutationScore(mutationScore);
		Assert.assertEquals(mutationScore, testSubject.getMutationScore());
	}

}