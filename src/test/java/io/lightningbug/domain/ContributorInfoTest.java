package io.lightningbug.domain;

import org.junit.Assert;
import org.junit.Test;
/**
 * @author Mike Krolak
 * @since 1.0
 */

public class ContributorInfoTest {

	@Test(expected = IllegalArgumentException.class)
	public void testCreateAContributorInfoBothNull() {
		new ContributorInfo(null,null);
		Assert.fail();	//should not reach here
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateAContributorInfoBothBlank() {
		new ContributorInfo("","");
		Assert.fail();	//should not reach here
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateAContributorInfoNameNull() {
		new ContributorInfo(null,"userId");
		Assert.fail();	//should not reach here
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateAContributorInfoNameBlank() {
		new ContributorInfo("","userId");
		Assert.fail();	//should not reach here
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateAContributorInfoUserIdNull() {
		new ContributorInfo("Tester",null);
		Assert.fail();	//should not reach here
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateAContributorInfoUserIdBlank() {
		new ContributorInfo("Tester","");
		Assert.fail();	//should not reach here
	}

	@Test
	public void testCreateAContributorBothNonBlank() {
		ContributorInfo ci = new ContributorInfo("tester", "testId");
		Assert.assertEquals(ci.getName(),"tester");
		Assert.assertEquals(ci.getUserId(), "testId");		
	}
}
