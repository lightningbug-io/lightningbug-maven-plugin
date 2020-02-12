package io.lightningbug.domain;

import static org.mockito.Mockito.mock;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.maven.project.MavenProject;
import org.junit.Assert;
import org.junit.Test;

import io.lightningbug.domain.CodeInfo;
import io.lightningbug.domain.ContributorInfo;
import io.lightningbug.domain.DependencyInfo;
import io.lightningbug.domain.ProjectInfo;

public class ProjectInfoTest {

	@Test(expected = IllegalArgumentException.class)
	public void testCreateProjectInfoWith4Nulls() {
		new ProjectInfo(null, null, null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateProjectInfoWith3NullsOneBlank() {
		new ProjectInfo("", null, null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateProjectInfoWith3Nulls() {
		new ProjectInfo("Test", null, null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateProjectInfoWith2Nulls1Blank() {
		new ProjectInfo("Test", "", null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateProjectInfoWith1NullString1NullDate() {
		new ProjectInfo("Test", "Test", null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateProjectInfoWith1EmptyString1NullDate() {
		new ProjectInfo("Test", "Test", "", null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateProjectInfoWith1NullDate() {
		new ProjectInfo("Test", "Test", "Test", null);
		Assert.fail();
	}

	@Test
	public void testCreateProjectInfo3Strings1Date() {
		ProjectInfo pi = new ProjectInfo("Test", "Test", "Test", ZonedDateTime.now());
		Assert.assertNotNull(pi);
	}

	@Test
	public void testCreateProjectInfo2Strings1Empty1Date() {
		ProjectInfo pi = new ProjectInfo("Test", "Test", "", ZonedDateTime.now());
		Assert.assertNotNull(pi);
	}

	@Test
	public void testCreateProjectInfo2Strings1Null1Date() {
		ProjectInfo pi = new ProjectInfo("Test", "Test", null, ZonedDateTime.now());
		Assert.assertNotNull(pi);
	}

	@Test
	public void testCreateProjectInfoWithNoNulls() {
		ProjectInfo pi = new ProjectInfo("Test", "Test", "Test", ZonedDateTime.now());
		Assert.assertNotNull(pi);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateProjectInfoWith2Nulls() {
		new ProjectInfo(null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateProjectInfoWithMavenProjectNull() {
		new ProjectInfo(null, ZonedDateTime.now());
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateProjectInfoWithStartTimetNull() {
		new ProjectInfo(mock(MavenProject.class), null);
		Assert.fail();
	}

	@Test
	public void testCreateProjectInfoNonNulls() {
		ProjectInfo pi = new ProjectInfo(mock(MavenProject.class), ZonedDateTime.now());
		Assert.assertNotNull(pi);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddClassWithNull() throws Exception {
		ProjectInfo pi = new ProjectInfo(mock(MavenProject.class), ZonedDateTime.now());
		pi.addClass(null);
		Assert.fail();
	}

	@Test
	public void testAddClassNonNull() throws Exception {
		ProjectInfo pi = new ProjectInfo(mock(MavenProject.class), ZonedDateTime.now());
		pi.addClass(mock(CodeInfo.class));
		Assert.assertEquals(pi.getClasses().size(), 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddTestClassWithNull() throws Exception {
		ProjectInfo pi = new ProjectInfo(mock(MavenProject.class), ZonedDateTime.now());
		pi.addTestClass(null);
		Assert.fail();
	}

	@Test
	public void testAddTestClassNonNull() throws Exception {
		ProjectInfo pi = new ProjectInfo(mock(MavenProject.class), ZonedDateTime.now());
		pi.addTestClass(mock(CodeInfo.class));
		Assert.assertEquals(pi.getTestClasses().size(), 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddDeveloperNull() throws Exception {
		ProjectInfo pi = new ProjectInfo(mock(MavenProject.class), ZonedDateTime.now());
		pi.addDeveloper(null);
		Assert.fail();
	}

	@Test
	public void testAddDeveloperNonNull() throws Exception {
		ProjectInfo pi = new ProjectInfo(mock(MavenProject.class), ZonedDateTime.now());
		pi.addDeveloper(mock(ContributorInfo.class));
		Assert.assertEquals(pi.getDevelopers().size(), 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddTesterNull() throws Exception {
		ProjectInfo pi = new ProjectInfo(mock(MavenProject.class), ZonedDateTime.now());
		pi.addTester(null);
		Assert.fail();
	}

	@Test
	public void testAddTesterNonNull() throws Exception {
		ProjectInfo pi = new ProjectInfo(mock(MavenProject.class), ZonedDateTime.now());
		pi.addTester(mock(ContributorInfo.class));
		Assert.assertEquals(pi.getTesters().size(), 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddTransitiveDependecyNull() throws Exception {
		ProjectInfo pi = new ProjectInfo(mock(MavenProject.class), ZonedDateTime.now());
		pi.addTransitiveDependency(null);
		Assert.fail();
	}

	@Test
	public void testAddTransitiveDependecyNonNull() throws Exception {
		ProjectInfo pi = new ProjectInfo(mock(MavenProject.class), ZonedDateTime.now());
		pi.addTransitiveDependency(mock(DependencyInfo.class));
		Assert.assertEquals(pi.getTransitiveDependencies().size(), 1);
	}

	@Test
	public void testGetName() throws Exception {
		ProjectInfo pi = new ProjectInfo("Test", "Test", "Test", ZonedDateTime.now());
		Assert.assertEquals(pi.getName(), "Test");
	}

	@Test
	public void testGetVersion() throws Exception {
		ProjectInfo pi = new ProjectInfo("Test", "Test", "Test", ZonedDateTime.now());
		Assert.assertEquals(pi.getVersion(), "Test");
	}

	@Test
	public void testGetOrganization() throws Exception {
		ProjectInfo pi = new ProjectInfo("Test", "Test", "Test", ZonedDateTime.now());
		Assert.assertEquals(pi.getOrganization(), "Test");
	}

	@Test
	public void testGetOrganizationNull() throws Exception {
		ProjectInfo pi = new ProjectInfo("Test", "Test", null, ZonedDateTime.now());
		Assert.assertEquals(pi.getOrganization(), "");
	}

	@Test
	public void testEqualsWhenReflexive() {
		ZonedDateTime date = ZonedDateTime.now();
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", "Test", date);
		Assert.assertEquals(pi1, pi1);
		Assert.assertEquals(pi1.hashCode(), pi1.hashCode());
	}

	@Test
	public void testEqualsWhenDifferentClass() {
		ZonedDateTime date = ZonedDateTime.now();
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", null, date);
		Assert.assertNotEquals(pi1,"Test");
		Assert.assertNotEquals(pi1.hashCode(), "Test".hashCode());
	}
	
	@Test
	public void testEqualsWhenOrganizationNull() {
		ZonedDateTime date = ZonedDateTime.now();
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", null, date);
		ProjectInfo pi2 = new ProjectInfo("Test", "Test", "Test", date);
		Assert.assertNotEquals(pi1, pi2);
		Assert.assertNotEquals(pi1.hashCode(), pi2.hashCode());
	}

	@Test
	public void testEqualsWhenEqual() {
		ZonedDateTime date = ZonedDateTime.now();
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", "Test", date);
		ProjectInfo pi2 = new ProjectInfo("Test", "Test", "Test", date);
		Assert.assertEquals(pi1, pi2);
	}

	@Test
	public void testEqualsWhenClassNonNull() {
		ZonedDateTime date = ZonedDateTime.now();
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", "Test", date);
		ProjectInfo pi2 = new ProjectInfo("Test", "Test", "Test", date);
		pi1.addClass(mock(CodeInfo.class));
		Assert.assertNotEquals(pi1, pi2);
		Assert.assertNotEquals(pi1.hashCode(), pi2.hashCode());
	}

	@Test
	public void testEqualsWhenTestClassNonNull() {
		ZonedDateTime date = ZonedDateTime.now();
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", "Test", date);
		ProjectInfo pi2 = new ProjectInfo("Test", "Test", "Test", date);
		pi2.addTestClass(mock(CodeInfo.class));
		Assert.assertNotEquals(pi1, pi2);
		Assert.assertNotEquals(pi1.hashCode(), pi2.hashCode());
	}
	
	@Test
	public void testEqualsWhenDevelopersNonNul() {
		ZonedDateTime date = ZonedDateTime.now();
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", "Test", date);
		ProjectInfo pi2 = new ProjectInfo("Test", "Test", "Test", date);
		pi2.addDeveloper(mock(ContributorInfo.class));
		Assert.assertNotEquals(pi1, pi2);
		Assert.assertNotEquals(pi1.hashCode(), pi2.hashCode());
	}

	@Test
	public void testEqualsWhenTestersNonNul() {
		ZonedDateTime date = ZonedDateTime.now();
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", "Test", date);
		ProjectInfo pi2 = new ProjectInfo("Test", "Test", "Test", date);
		pi2.addTester(mock(ContributorInfo.class));
		Assert.assertNotEquals(pi1, pi2);
		Assert.assertNotEquals(pi1.hashCode(), pi2.hashCode());
	}

	@Test
	public void testEqualsWhenDependenciesNonNul() {
		ZonedDateTime date = ZonedDateTime.now();
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", "Test", date);
		ProjectInfo pi2 = new ProjectInfo("Test", "Test", "Test", date);
		pi2.addTransitiveDependency(mock(DependencyInfo.class));
		Assert.assertNotEquals(pi1, pi2);
		Assert.assertNotEquals(pi1.hashCode(), pi2.hashCode());
	}

	@Test
	public void testEqualsWhenNotEqualName() {
		ZonedDateTime date = ZonedDateTime.now();
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", "Test", date);
		ProjectInfo pi2 = new ProjectInfo("Test1", "Test", "Test", date);
		Assert.assertNotEquals(pi1, pi2);
		Assert.assertNotEquals(pi1.hashCode(), pi2.hashCode());
	}

	@Test
	public void testEqualsWhenNotEqualVersion() {
		ZonedDateTime date = ZonedDateTime.now();
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", "Test", date);
		ProjectInfo pi2 = new ProjectInfo("Test", "Test1", "Test", date);
		Assert.assertNotEquals(pi1, pi2);
		Assert.assertNotEquals(pi1.hashCode(), pi2.hashCode());
	}

	@Test
	public void testEqualsWhenNotEqualOrganization() {
		ZonedDateTime date = ZonedDateTime.now();
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", "Test", date);
		ProjectInfo pi2 = new ProjectInfo("Test", "Test", "Test1", date);
		Assert.assertNotEquals(pi1, pi2);
		Assert.assertNotEquals(pi1.hashCode(), pi2.hashCode());
	}

	@Test
	public void testEqualsWhenNulllOrganization() {
		ZonedDateTime date = ZonedDateTime.now();
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", "Test", date);
		ProjectInfo pi2 = new ProjectInfo("Test", "Test", null, date);
		Assert.assertNotEquals(pi1, pi2);
		Assert.assertNotEquals(pi1.hashCode(), pi2.hashCode());
	}

	@Test
	public void testEqualsWhenNulllOtherOrganization() {
		ZonedDateTime date = ZonedDateTime.now();
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", "Test", date);
		ProjectInfo pi2 = new ProjectInfo("Test", "Test", null, date);
		Assert.assertNotEquals(pi2, pi1);
		Assert.assertNotEquals(pi1.hashCode(), pi2.hashCode());
	}

	@Test
	public void testEqualsWhenNotEqualTime() {
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", "Test", ZonedDateTime.now());
		ProjectInfo pi2 = new ProjectInfo("Test", "Test", "Test", ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of("GMT+5")));
		Assert.assertNotEquals(pi1, pi2);
		Assert.assertNotEquals(pi1.hashCode(), pi2.hashCode());
	}

	@Test
	public void testEqualsWhenFirstNull() {
		ProjectInfo pi2 = new ProjectInfo("Test", "Test", "Test", ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of("GMT+5")));
		Assert.assertNotEquals(null, pi2);
	}

	@Test
	public void testEqualsWhenSecondNull() {
		ProjectInfo pi1 = new ProjectInfo("Test", "Test", "Test", ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of("GMT+5")));
		Assert.assertNotEquals(pi1, null);
	}

}