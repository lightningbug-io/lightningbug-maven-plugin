package io.lightningbug.domain;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class BlameInfoTest {

	private static final int SAMPLE_LINE_NUMBER = 1;
	private static final String SAMPLE_COMMIT_ID = "d0fdbd2ac4be8406569f0174899246f6b673fb41";
	private static final int SAMPLE_TIMESTAMP = 1582140257;

	@Test(expected = IllegalArgumentException.class)
	public void testCreateBlameInfoWithAllNulls() {
		new BlameInfo(null, null, 0, 0);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateBlameInfoWithNullCommitId() {
		new BlameInfo(Mockito.mock(ContributorInfo.class), null, 0, 0);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateBlameInfoWithEmptyCommitId() {
		new BlameInfo(Mockito.mock(ContributorInfo.class), "", 0, 0);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateBlameInfoWithNonPositiveLineNum() {
		new BlameInfo(Mockito.mock(ContributorInfo.class), SAMPLE_COMMIT_ID, 0, 0);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateBlameInfoWithNonPositiveTimestamp() {
		new BlameInfo(Mockito.mock(ContributorInfo.class), SAMPLE_COMMIT_ID, SAMPLE_LINE_NUMBER, 0);
		Assert.fail();
	}

	@Test
	public void testCreateBlameInfo() {
		BlameInfo bi = new BlameInfo(Mockito.mock(ContributorInfo.class), SAMPLE_COMMIT_ID, SAMPLE_LINE_NUMBER, SAMPLE_TIMESTAMP);
		Assert.assertNotNull(bi);
	}

	@Test
	public void testGetLine() throws Exception {
		BlameInfo bi = new BlameInfo(Mockito.mock(ContributorInfo.class), SAMPLE_COMMIT_ID, SAMPLE_LINE_NUMBER, SAMPLE_TIMESTAMP);
		Assert.assertEquals(SAMPLE_LINE_NUMBER, bi.getLine());
	}

	@Test
	public void testGetDeveloper() throws Exception {
		BlameInfo bi = new BlameInfo(Mockito.mock(ContributorInfo.class), SAMPLE_COMMIT_ID, SAMPLE_LINE_NUMBER, SAMPLE_TIMESTAMP);
		Assert.assertNotNull(bi.getDeveloper());
	}

	@Test
	public void testGetTimestamp() throws Exception {
		BlameInfo bi = new BlameInfo(Mockito.mock(ContributorInfo.class), SAMPLE_COMMIT_ID, SAMPLE_LINE_NUMBER, SAMPLE_TIMESTAMP);
		Assert.assertEquals(SAMPLE_TIMESTAMP, bi.getTimestamp());
	}

	@Test
	public void testGetCommitId() throws Exception {
		BlameInfo bi = new BlameInfo(Mockito.mock(ContributorInfo.class), SAMPLE_COMMIT_ID, SAMPLE_LINE_NUMBER, SAMPLE_TIMESTAMP);
		Assert.assertEquals(SAMPLE_COMMIT_ID, bi.getCommitId());
	}
}