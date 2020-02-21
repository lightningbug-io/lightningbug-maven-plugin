package io.lightningbug.staticanalysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import com.github.javaparser.JavaParser;
import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;

import io.lightningbug.domain.CodeInfo;
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

	@Test
	public void testProcessDirectoryProperly() throws IOException {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		Assert.assertNotNull(jpv.processDirectory(folder.newFolder(), new ArrayList<>()));
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
	public void testConcreteClassDeclaration() {
		ProjectInfo pi = Mockito.mock(ProjectInfo.class);
		JavaParserVisitor jpv = new JavaParserVisitor(pi, Mockito.mock(Log.class));
		ClassOrInterfaceDeclaration cd = Mockito.mock(ClassOrInterfaceDeclaration.class);
		Mockito.when(cd.getFullyQualifiedName()).thenReturn(Optional.of("org.test.Tester"));
		Mockito.when(cd.isAbstract()).thenReturn(false);
		Mockito.when(cd.isInterface()).thenReturn(false);
		jpv.visit(cd, null);
		Mockito.verify(pi, Mockito.times(1)).addClass(Mockito.any(CodeInfo.class));
	}

	@Test
	public void testAbstractClassDeclaration() {
		ProjectInfo pi = Mockito.mock(ProjectInfo.class);
		JavaParserVisitor jpv = new JavaParserVisitor(pi, Mockito.mock(Log.class));
		ClassOrInterfaceDeclaration cd = new ClassOrInterfaceDeclaration();
		cd.setInterface(false);
		cd.setAbstract(true);
		cd.setName("TestClass");
		jpv.visit(cd, null);
		Assert.assertEquals(0, pi.getClasses().size());
	}

	@Test
	public void testInterfaceDeclaration() {
		ProjectInfo pi = Mockito.mock(ProjectInfo.class);
		JavaParserVisitor jpv = new JavaParserVisitor(pi, Mockito.mock(Log.class));
		ClassOrInterfaceDeclaration cd = new ClassOrInterfaceDeclaration();
		cd.setInterface(true);
		cd.setName("TestClass");
		jpv.visit(cd, null);
		Assert.assertEquals(0, pi.getClasses().size());
	}

	@Test
	public void testMethodDeclaration() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		MethodDeclaration md = new MethodDeclaration();
		md.setName("main");
		md.getName().setRange(new Range(new Position(1, 1), new Position(2, 3)));
		md.setRange(new Range(new Position(1, 1), new Position(2, 3)));
		md.setPublic(true);
		md.addParameter(new Parameter(PrimitiveType.booleanType(), "p"));
		BlockStmt bs = new BlockStmt();
		bs.addAndGetStatement("String s = \"test\"");
		bs.addAndGetStatement("System.out.println(s)");
		md.setType("String");
		Optional<MethodCallExpr> call = Navigator.findMethodCall(md, "add");
		md.setBody(bs);
		System.out.println(md.getBegin() + " " + md.getEnd() + md.toString());
		jpv.visit(md, null);
		Assert.assertEquals(1, jpv.cyclotomicComplexityCounter.get());
	}

	@Test
	public void testForStatement() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.visit(new ForStmt(), null);
		Assert.assertEquals(2, jpv.cyclotomicComplexityCounter.get());
	}

	@Test
	public void testCatchClause() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.visit(new CatchClause(), null);
		Assert.assertEquals(1, jpv.catchClauseCounter.get());
	}

	@Test
	public void testTryStmt() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.visit(new TryStmt(), null);
		Assert.assertEquals(1, jpv.tryStatementCounter.get());
	}

	@Test
	public void testIfStmt() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.cyclotomicComplexityCounter.set(1);
		jpv.visit(new IfStmt(), null);
		Assert.assertEquals(2, jpv.cyclotomicComplexityCounter.get());
	}

	@Test
	public void testForEachStmt() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.cyclotomicComplexityCounter.set(1);
		jpv.visit(new ForEachStmt(), null);
		Assert.assertEquals(2, jpv.cyclotomicComplexityCounter.get());
	}

	@Test
	public void testDoStmt() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.cyclotomicComplexityCounter.set(1);
		jpv.visit(new DoStmt(), null);
		Assert.assertEquals(2, jpv.cyclotomicComplexityCounter.get());
	}

	@Test
	public void testWhileStmt() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.cyclotomicComplexityCounter.set(1);
		jpv.visit(new WhileStmt(), null);
		Assert.assertEquals(2, jpv.cyclotomicComplexityCounter.get());
	}

	@Test
	public void testSynchronizedStmt() {
		JavaParserVisitor jpv = new JavaParserVisitor(Mockito.mock(ProjectInfo.class), Mockito.mock(Log.class));
		jpv.visit(new SynchronizedStmt(), null);
		Assert.assertEquals(1, jpv.synchronizedStatementCounter.get());
	}

}