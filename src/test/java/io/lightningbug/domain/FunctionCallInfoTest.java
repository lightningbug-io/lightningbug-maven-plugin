package io.lightningbug.domain;

import org.junit.Test;

import org.junit.Assert;


public class FunctionCallInfoTest {

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls1() {
		new FunctionCallInfo(null, null, null, 0);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls2() {
		 new FunctionCallInfo("", null, null, 0);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls3() {
		new FunctionCallInfo("test", null, null, 0);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls4() {
		 new FunctionCallInfo("test", "", null, 0);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls5() {
		 new FunctionCallInfo("test", "test", null, 0);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls6() {
		new FunctionCallInfo("test", "test", "", 0);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls7() {
		new FunctionCallInfo("test", "test", "test", 0);
		Assert.fail();
	}

	@Test
	public void testCreateWithNoNulls() {
		FunctionCallInfo fi = new FunctionCallInfo("test", "test", "test", 1);
		Assert.assertNotNull(fi);
	}

	@Test
	public void testGetLineNumber() throws Exception {
		FunctionCallInfo fi = new FunctionCallInfo("test", "test", "test", 1);
		Assert.assertEquals(fi.getLineNumber(), 1);
	}

	@Test
	public void testGetPackageName() throws Exception {
		FunctionCallInfo fi = new FunctionCallInfo("test", "test", "test", 1);
		Assert.assertEquals(fi.getPackageName(), "test");
	}

	@Test
	public void testGetCodeName() throws Exception {
		FunctionCallInfo fi = new FunctionCallInfo("test", "test", "test", 1);
		Assert.assertEquals(fi.getCodeName(), "test");
	}

	@Test
	public void testGetFunctionName() throws Exception {
		FunctionCallInfo fi = new FunctionCallInfo("test", "test", "test", 1);
		Assert.assertEquals(fi.getFunctionName(), "test");
	}
}