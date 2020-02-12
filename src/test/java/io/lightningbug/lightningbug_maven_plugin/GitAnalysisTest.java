package io.lightningbug.lightningbug_maven_plugin;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class GitAnalysisTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testGetBlameWithNullFile() throws Exception {
		File file = null;
		List<String> result;
		result = GitAnalysis.getBlame(file);
		Assert.assertEquals(result.size(), 0);
	}

	@Test
	public void testGetBlameWithDirectory() throws Exception {
		List<String> result;
		result = GitAnalysis.getBlame(folder.newFolder());
		Assert.assertEquals(result.size(), 0);
	}
}