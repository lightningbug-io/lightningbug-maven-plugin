package io.lightningbug.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.mockito.Mockito;

import org.junit.Assert;

public class FunctionInfoTest {

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls1() {
		new FunctionInfo(null, 0, 0, 0, null, null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls2() {
		new FunctionInfo("", 0, 0, 0, null, null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls3() {
		new FunctionInfo("test", 0, 0, 0, null, null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls4() {
		new FunctionInfo("test", 1, 0, 0, null, null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls5() {
		new FunctionInfo("test", 1, 1, 0, null, null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls6() {
		new FunctionInfo("test", 1, 1, 1, null, null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls7() {
		new FunctionInfo("test", 1, 1, 1, new ArrayList<String>(), null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls8() {
		new FunctionInfo("test", 1, 1, 1, new ArrayList<String>(), "", null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls9() {
		new FunctionInfo("test", 1, 1, 1, new ArrayList<String>(), "void", null);
		Assert.fail();
	}

	@Test
	public void testCreateWithNoNulls() {
		FunctionInfo fi = new FunctionInfo("test", 1, 1, 1, new ArrayList<String>(), "void", new AtomicInteger(1));
		Assert.assertNotNull(fi);
	}

	@Test
	public void testGetFunctionCalls() throws Exception {
		FunctionInfo fi = new FunctionInfo("tester", 10, 1, 10, new ArrayList<String>(), "void", new AtomicInteger(1));
		fi.addFunctionCall(Mockito.mock(FunctionCallInfo.class));
		Assert.assertEquals(fi.getFunctionCalls().size(), 1);
	}

	@Test
	public void testGetDevelopers() throws Exception {
		FunctionInfo fi = new FunctionInfo("tester", 10, 1, 10, new ArrayList<String>(), "void", new AtomicInteger(1));
		fi.addDeveloper(Mockito.mock(ContributorInfo.class));
		Assert.assertEquals(fi.getDevelopers().size(), 1);
	}

	@Test
	public void testGetStartLine() throws Exception {
		FunctionInfo fi = new FunctionInfo("tester", 10, 1, 10, new ArrayList<String>(), "void", new AtomicInteger(1));
		Assert.assertEquals(fi.getStartLine(), 1);
	}

	@Test
	public void testGetEndLine() throws Exception {
		FunctionInfo fi = new FunctionInfo("tester", 10, 1, 10, new ArrayList<String>(), "void", new AtomicInteger(1));
		Assert.assertEquals(fi.getEndLine(), 10);
	}

	@Test
	public void testGetParameters() throws Exception {
		List<String> params = new ArrayList<String>();
		params.add("int");
		FunctionInfo fi = new FunctionInfo("tester", 10, 1, 10, params, "void", new AtomicInteger(1));
		Assert.assertEquals(fi.getParameters().size(), 1);
	}

	@Test
	public void testGetReturnType() throws Exception {
		FunctionInfo fi = new FunctionInfo("tester", 10, 1, 10, new ArrayList<String>(), "void", new AtomicInteger(1));
		Assert.assertEquals(fi.getReturnType(), "void");
	}

	@Test
	public void testGetName() throws Exception {
		FunctionInfo fi = new FunctionInfo("tester", 10, 1, 10, new ArrayList<String>(), "void", new AtomicInteger(1));
		Assert.assertEquals(fi.getName(), "tester");
	}

	@Test
	public void testGetLoc() throws Exception {
		FunctionInfo fi = new FunctionInfo("tester", 10, 1, 10, new ArrayList<String>(), "void", new AtomicInteger(1));
		Assert.assertEquals(fi.getLoc(), 10);
	}

	@Test
	public void testGetCyclotomicComplexity() throws Exception {
		FunctionInfo fi = new FunctionInfo("tester", 10, 1, 10, new ArrayList<String>(), "void", new AtomicInteger(1));
		Assert.assertEquals(fi.getCyclotomicComplexity().get(), 1);
	}

}