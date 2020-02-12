package io.lightningbug.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mike Krolak
 * @since 1.0
 */

public class CodeInfoTest {

	@Test(expected = IllegalArgumentException.class)
	public void createNewCodeInfoWithNull() {
		new CodeInfo(null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNewCodeInfoWithEmpty() {
		new CodeInfo("");
		Assert.fail();
	}

	@Test
	public void testCreateNewCodeNonEmpty() {
		CodeInfo ci = new CodeInfo("test");
		Assert.assertNotNull(ci);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddContributorNull() {
		CodeInfo ci = new CodeInfo("test");
		ci.addContributor(null);
		Assert.fail();
	}

	@Test
	public void testAddContributor() {
		ContributorInfo contributor = mock(ContributorInfo.class);
		CodeInfo ci = new CodeInfo("test");
		assertTrue(ci.addContributor(contributor));
		Assert.assertEquals(ci.getCurrentContributors().size(), 1);
	}

	@Test
	public void testAddContributorTwice() {
		ContributorInfo contributor = mock(ContributorInfo.class);
		CodeInfo ci = new CodeInfo("test");
		assertTrue(ci.addContributor(contributor));
		assertFalse(ci.addContributor(contributor));
		Assert.assertEquals(ci.getCurrentContributors().size(), 1);
	}

	@Test
	public void testAddCyclotomicComplexity() {
		CodeInfo ci = new CodeInfo("test");
		Assert.assertEquals(ci.getCyclotomicComplexity().get(), 0);
		ci.addCyclotomicComplexiy(1);
		Assert.assertEquals(ci.getCyclotomicComplexity().get(), 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddExternalDependecyNull() {
		CodeInfo ci = new CodeInfo("test");
		ci.addExternalDependencies(null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddExternalDependecyEmpty() {
		CodeInfo ci = new CodeInfo("test");
		ci.addExternalDependencies("");
		Assert.fail();
	}

	@Test
	public void testAddExternalDependecy() {
		CodeInfo ci = new CodeInfo("test");
		Assert.assertEquals(ci.getExternalDependencies().size(), 0);
		ci.addExternalDependencies("tester");
		Assert.assertEquals(ci.getExternalDependencies().size(), 1);
	}

	@Test
	public void testIsReflexive() {
		CodeInfo ci1 = new CodeInfo("test");
		Assert.assertEquals(ci1, ci1);
		Assert.assertEquals(ci1.hashCode(), ci1.hashCode());
	}

	@Test
	public void testIsReflexiveWithOtherNull() {
		CodeInfo ci1 = new CodeInfo("test");
		Assert.assertNotEquals(ci1, null);
	}

	@Test
	public void testIsReflexiveWithNull() {
		CodeInfo ci1 = new CodeInfo("test");
		Assert.assertNotEquals(null, ci1);
	}

	@Test
	public void testIsEqual() {
		CodeInfo ci1 = new CodeInfo("test");
		CodeInfo ci2 = new CodeInfo("test");
		Assert.assertEquals(ci1, ci2);
		Assert.assertEquals(ci1.hashCode(), ci2.hashCode());
	}

	@Test
	public void testIsNotSameClass() {
		CodeInfo ci1 = new CodeInfo("test");
		Assert.assertNotEquals(ci1, "Test");
		Assert.assertNotEquals(ci1.hashCode(), "Test".hashCode());
	}

	@Test
	public void testIsNotEqual() {
		CodeInfo ci1 = new CodeInfo("test");
		CodeInfo ci2 = new CodeInfo("test1");
		Assert.assertNotEquals(ci1, ci2);
		Assert.assertNotEquals(ci1.hashCode(), ci2.hashCode());
	}

	@Test
	public void testIsNotEqualContributor() {
		CodeInfo ci1 = new CodeInfo("test");
		ci1.addContributor(mock(ContributorInfo.class));
		CodeInfo ci2 = new CodeInfo("test");
		Assert.assertNotEquals(ci1, ci2);
		Assert.assertNotEquals(ci1.hashCode(), ci2.hashCode());
	}

	@Test
	public void testIsNotEqualFunction() {
		CodeInfo ci1 = new CodeInfo("test");
		ci1.addFunction(mock(FunctionInfo.class));
		CodeInfo ci2 = new CodeInfo("test");
		Assert.assertNotEquals(ci1, ci2);
		Assert.assertNotEquals(ci1.hashCode(), ci2.hashCode());
	}

	@Test
	public void testIsNotEqualDependecy() {
		CodeInfo ci1 = new CodeInfo("test");
		ci1.addExternalDependencies("Test");
		CodeInfo ci2 = new CodeInfo("test");
		Assert.assertNotEquals(ci1, ci2);
		Assert.assertNotEquals(ci1.hashCode(), ci2.hashCode());
	}
}
