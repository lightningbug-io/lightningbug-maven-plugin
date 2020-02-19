package io.lightningbug.staticanalysis;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;

import io.lightningbug.domain.ProjectInfo;

public class JavaParserVisitorTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNulls() throws Exception {
		new JavaParserVisitor(null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithProjectNull() throws Exception {
		new JavaParserVisitor(null, Mockito.mock(Log.class));
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithLogNull() throws Exception {
		new JavaParserVisitor(Mockito.mock(ProjectInfo.class), null);
		Assert.fail();
	}

	@Test
	public void testCreateWithNonNulls() {
		JavaParserVisitor jpv;
		try {
			jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
			Assert.assertNotNull(jpv);
		} catch (Exception e) {
			Assert.fail();
		}
	}
	@Test(expected = IllegalArgumentException.class)
	public void testProcessDirectoryNulls() throws IOException {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.processDirectory(null, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testProcessDirectoryNullDependencies() throws IOException {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.processDirectory(folder.newFolder(), null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testProcessDirectoryNullFolder() throws IOException {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.processDirectory(null, new ArrayList<>());
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testProcessDirectoryUsingFile() throws IOException {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.processDirectory(folder.newFile(), new ArrayList<>());
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testProcessJavaFileNullFile() throws IOException {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.processJavaFile(null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testProcessJavaFileWithFolder() throws IOException {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.processJavaFile(folder.newFolder());
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testProcessJavaFileWithNonJavaFile() throws IOException {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.processJavaFile(folder.newFile("test.txt"));
		Assert.fail();
	}

	@Test
	public void testProcessJavaFileWithJavaFile() throws IOException {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.processJavaFile(folder.newFile("test.java"));
	}
	
	@Test
	public void testconfigureJavaParser() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		JavaParser jp = jpv.configureJavaParser();
		Assert.assertNotNull(jp);
	}

	@Test
	public void testForStatement() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.visit(new ForStmt(),null);
		Assert.assertEquals(jpv.cyclotomicComplexityCounter.get(),2);
	}
	
	@Test
	public void testCatchClause() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.visit(new CatchClause(),null);
		Assert.assertEquals(jpv.catchClauseCounter.get(),1);
	}
	
	@Test
	public void testTryStmt() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.visit(new TryStmt(),null);
		Assert.assertEquals(jpv.tryStatementCounter.get(),1);
	}

	@Test
	public void testIfStmt() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.cyclotomicComplexityCounter.set(1);
		jpv.visit(new IfStmt(),null);
		Assert.assertEquals(jpv.cyclotomicComplexityCounter.get(),2);
	}
	@Test
	public void testForEachStmt() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.cyclotomicComplexityCounter.set(1);
		jpv.visit(new ForEachStmt(),null);
		Assert.assertEquals(jpv.cyclotomicComplexityCounter.get(),2);
	}	
	@Test
	public void testDoStmt() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.cyclotomicComplexityCounter.set(1);
		jpv.visit(new DoStmt(),null);
		Assert.assertEquals(jpv.cyclotomicComplexityCounter.get(),2);
	}
	@Test
	public void testWhileStmt() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.cyclotomicComplexityCounter.set(1);
		jpv.visit(new WhileStmt(),null);
		Assert.assertEquals(jpv.cyclotomicComplexityCounter.get(),2);
	}
	
	@Test
	public void testSynchronizedStmt() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.visit(new SynchronizedStmt(),null);
		Assert.assertEquals(jpv.synchronizedStatementCounter.get(),1);
	}

}