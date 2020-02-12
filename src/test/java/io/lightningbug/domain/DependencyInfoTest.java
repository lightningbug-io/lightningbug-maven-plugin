package io.lightningbug.domain;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DependencyInfoTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDependencyInfoWithNulls0() {
		new DependencyInfo(null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDependencyInfoWithNulls() {
		new DependencyInfo(null, null, null, null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDependencyInfoWithNulls2() {
		new DependencyInfo("", null, null, null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDependencyInfoWithNulls3() {
		new DependencyInfo("test", null, null, null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDependencyInfoWithNulls4() {
		new DependencyInfo("test", "", null, null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDependencyInfoWithNulls5() {
		new DependencyInfo("test", "test", null, null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDependencyInfoWithNulls6() {
		new DependencyInfo("test", "test", "", null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDependencyInfoWithNulls7() {
		new DependencyInfo("test", "test", "test", null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDependencyInfoWithNulls8() {
		new DependencyInfo("test", "test", "test", "", null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDependencyInfoWithNulls9() {
		new DependencyInfo("test", "test", "test", "test", null);
		Assert.fail();
	}

	@Test
	public void testCreateDependencyInfoWithNoNulls() {
		File file;
		try {
			file = folder.newFile();
			DependencyInfo di = new DependencyInfo("test", "test", "test", "test", file);
			Assert.assertNotNull(di);
		} catch (IOException ex) {
			Assert.fail();
		}
	}

	@Test
	public void testGetCanonicalLocation() throws Exception {
		File file;
		try {
			file = folder.newFile();
			DependencyInfo di = new DependencyInfo("test", "test", "test", "test", file);
			Assert.assertNotNull(di.getCanonicalLocation());
		} catch (IOException ex) {
			Assert.fail();
		}
	}

	@Test
	public void testGetGroupId() throws Exception {
		File file;
		try {
			file = folder.newFile();
			DependencyInfo di = new DependencyInfo("test1", "test2", "test3", "test4", file);
			Assert.assertEquals(di.getGroupId(), "test1");
		} catch (IOException ex) {
			Assert.fail();
		}
	}

	@Test
	public void testGetArtifactId() throws Exception {
		File file;
		try {
			file = folder.newFile();
			DependencyInfo di = new DependencyInfo("test1", "test2", "test3", "test4", file);
			Assert.assertEquals(di.getArtifactId(), "test2");
		} catch (IOException ex) {
			Assert.fail();
		}
	}

	@Test
	public void testGetName() throws Exception {
		File file;
		try {
			file = folder.newFile();
			DependencyInfo di = new DependencyInfo("test1", "test2", "test3", "test4", file);
			Assert.assertEquals(di.getName(), "test1:test2:test3");
		} catch (IOException ex) {
			Assert.fail();
		}
	}

	@Test
	public void testGetVersion() throws Exception {
		File file;
		try {
			file = folder.newFile();
			DependencyInfo di = new DependencyInfo("test1", "test2", "test3", "test4", file);
			Assert.assertEquals(di.getVersion(), "test4");
		} catch (IOException ex) {
			Assert.fail();
		}
	}

	@Test
	public void testGetType() throws Exception {
		File file;
		try {
			file = folder.newFile();
			DependencyInfo di = new DependencyInfo("test1", "test2", "test3", "test4", file);
			Assert.assertEquals(di.getType(), "test3");
		} catch (IOException ex) {
			Assert.fail();
		}
	}
}