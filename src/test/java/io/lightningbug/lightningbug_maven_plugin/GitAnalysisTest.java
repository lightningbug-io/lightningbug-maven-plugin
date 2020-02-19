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
	}

	@Test
	public void testGetBlameWithDirectory() throws Exception {
	}
}