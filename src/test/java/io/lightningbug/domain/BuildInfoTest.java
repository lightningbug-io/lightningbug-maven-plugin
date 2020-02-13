package io.lightningbug.domain;

import static org.mockito.Mockito.mock;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mike Krolak
 * @since 1.0
 */

public class BuildInfoTest {
	/**
	 * 
	 */
	private static final String GMT_5 = "GMT+5";
	/**
	 * 
	 */
	private static final String END_TIME = "2020-02-29T00:00:05+05:00[GMT+05:00]";
	/**
	 * 
	 */
	private static final String START_TIME = "2020-02-29T00:00:00+05:00[GMT+05:00]";
	private static final InfrastructureInfo infrastructureInfo = new InfrastructureInfo();
	private static ProjectInfo projectInfo;
	private static ZonedDateTime startTime;
	private static ZonedDateTime endTime;

	@Before
	public void beforeAllTestMethods() {
		projectInfo = mock(ProjectInfo.class);
		startTime = ZonedDateTime.of(2020, 2, 29, 0, 0, 0, 0, ZoneId.of(GMT_5));
		endTime = ZonedDateTime.of(2020, 2, 29, 0, 0, 5, 0, ZoneId.of(GMT_5));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewBuildInfoWithNullInfrastructureInf() {
		new BuildInfo(null, projectInfo, startTime, endTime);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewBuildInfoWithNullProjectInf() {
		new BuildInfo(infrastructureInfo, null, startTime, endTime);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewBuildInfoWithNullStartTime() {
		new BuildInfo(infrastructureInfo, projectInfo, null, endTime);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewBuildInfoWithNullEndTime() {
		new BuildInfo(infrastructureInfo, projectInfo, startTime, null);
		Assert.fail();
	}

	@Test
	public void testAllNonNullBuildInfo() {
		BuildInfo bi = new BuildInfo(infrastructureInfo, projectInfo, startTime, endTime);
		Assert.assertNotNull(bi);
	}

	@Test
	public void testGetStartTime() {
		BuildInfo bi = new BuildInfo(infrastructureInfo, projectInfo, startTime, endTime);
		Assert.assertEquals(bi.getStartTime(), START_TIME);
	}

	@Test
	public void testGetEndTime() {
		BuildInfo bi = new BuildInfo(infrastructureInfo, projectInfo, startTime, endTime);
		Assert.assertEquals(bi.getEndTime(), END_TIME);
	}

	@Test
	public void testGetInfrastructureInfo() {
		BuildInfo bi = new BuildInfo(infrastructureInfo, projectInfo, startTime, endTime);
		Assert.assertNotNull(bi.getInfrastructureInfo());
	}

	@Test
	public void testGetProjectInfo() {
		BuildInfo bi = new BuildInfo(infrastructureInfo, projectInfo, startTime, endTime);
		Assert.assertNotNull(bi.getProjectInfo());
	}

}