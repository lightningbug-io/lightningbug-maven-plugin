package io.lightningbug.staticanalysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.logging.Log;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.Position;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import io.lightningbug.domain.CodeInfo;
import io.lightningbug.domain.DependencyInfo;
import io.lightningbug.domain.FunctionCallInfo;
import io.lightningbug.domain.FunctionInfo;
import io.lightningbug.domain.ProjectInfo;
import io.lightningbug.exceptions.LambdaExceptionWrappers;

/**
 * @author Mike Krolak
 * @since 1.0
 */

public class JavaParserVisitor extends VoidVisitorAdapter<Void> implements ASTJavaProjectParsable {
	private ProjectInfo projectInfo;
	private JavaParserFacade javaParserFacade;
	private final Log LOGGER;
	List<FunctionInfo> methods = new ArrayList<FunctionInfo>();
	List<FunctionCallInfo> functionCalls = new ArrayList<>();
	List<String> externalDependencies = new ArrayList<>();

	public JavaParserVisitor(ProjectInfo projectInfo, Log log) {
		if (projectInfo != null && log != null) {
			this.projectInfo = projectInfo;
			this.LOGGER = log;
		} else {
			throw new IllegalArgumentException("params cannot be null");
		}
	}

	public ProjectInfo processDirectory(File dir, List<DependencyInfo> externalJars) {
		if (dir!= null && externalJars!=null) {
			if(dir.isDirectory()) {
				Collection<File> files = FileUtils.listFiles(dir, new String[] { "java" }, true);
				this.javaParserFacade = getJavaParserFacade(dir, externalJars);
				files.forEach(LambdaExceptionWrappers.handlingConsumerWrapper(file -> this.processJavaFile(file),
						IOException.class));
			} else {
				throw new IllegalArgumentException(dir.getAbsolutePath() + " is not a directory");
			}
		}
		else {
			throw new IllegalArgumentException("params cannot be null");
		}
		return this.projectInfo;
	}

	public void processJavaFile(File file) throws IOException {
		if (file != null) {
			if (!file.isDirectory() && FilenameUtils.isExtension(file.getName(), "java")) {
				CompilationUnit compilationUnit = StaticJavaParser.parse(file);
				compilationUnit.accept(this, null);
			} else {
				throw new IllegalArgumentException(
						file.getAbsolutePath() + " should not be a directory and must have an extension of java");
			}
		} else {
			throw new IllegalArgumentException("file cannot be null");
		}
	}

	protected JavaParserFacade getJavaParserFacade(File directory, List<DependencyInfo> externalJars) {
		CombinedTypeSolver localCts = new CombinedTypeSolver();
		localCts.add(new ReflectionTypeSolver());
		localCts.add(new JavaParserTypeSolver(new File(directory.getAbsolutePath())));

		externalJars.forEach(jar -> {
			try {
				localCts.add(new JarTypeSolver(jar.getCanonicalLocation()));
			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
		});

		return JavaParserFacade.get(localCts);

	}

	protected JavaParser configureJavaParser() {
		TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver());
		ParserConfiguration configuration = new ParserConfiguration()
				.setSymbolResolver(new JavaSymbolSolver(typeSolver));
		return new JavaParser(configuration);
	}

	protected FunctionCallInfo resolveMethodStatements(MethodCallExpr methodCall) {
		ResolvedMethodDeclaration methodDeclaration = this.javaParserFacade.solve(methodCall)
				.getCorrespondingDeclaration();
		return new FunctionCallInfo(methodDeclaration.getPackageName(), methodDeclaration.getClassName(),
				methodDeclaration.getName(), methodCall.getName().getBegin().orElseGet(() -> {
					return new Position(0, 0);
				}).line);
	}

	@Override
	public void visit(MethodCallExpr node, Void arg) {
		super.visit(node, arg);
	}

	@Override
	public void visit(MethodDeclaration node, Void arg) {
		super.visit(node, arg);
		int startLine = node.getName().getBegin().orElseGet(() -> {
			return new Position(0, 0);
		}).line;
		int endLine = node.getEnd().orElseGet(() -> {
			return new Position(0, 0);
		}).line;
		int loc = 1 + endLine - startLine;
		List<String> paramsAsStrings = new ArrayList<String>();
		node.getParameters().stream().forEach(param -> paramsAsStrings.add(param.getTypeAsString()));
		List<MethodCallExpr> calls = node.findAll(MethodCallExpr.class);
		calls.forEach(m -> {
			try {
				functionCalls.add(resolveMethodStatements(m));
			} catch (UnsolvedSymbolException ex) {
				LOGGER.error("Couldn't find " + m.getNameAsString());
			} catch (RuntimeException ex) {
				LOGGER.error("Hit a runtime exception: " + ex.getMessage());
			}
		});
		FunctionInfo functionInfo = new FunctionInfo(node.getNameAsString(), loc, startLine, endLine, paramsAsStrings,
				node.getTypeAsString(), cyclotomicComplexityCounter);
		functionCalls.forEach(functionCall -> functionInfo.addFunctionCall(functionCall));
		methods.add(functionInfo);
		functionCalls.clear();
		cyclotomicComplexityCounter.set(1);
		tryStatementCounter.set(0);
		catchClauseCounter.set(0);
		synchronizedStatementCounter.set(0);
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration node, Void arg) {
		super.visit(node, arg);
		if (!node.isInterface() && !node.isAbstract()) {
			CodeInfo codeInfo = new CodeInfo(node.getFullyQualifiedName().get());
			methods.forEach((method) -> {
				codeInfo.addLoc(method.getLoc());
				codeInfo.addFunction(method);
			});
			externalDependencies.forEach(externalDependency -> codeInfo.addExternalDependencies(externalDependency));
			projectInfo.addClass(codeInfo);
			methods.clear();
			externalDependencies.clear();
		}
	}

	@Override
	public void visit(ForStmt node, Void arg) {
		cyclotomicComplexityCounter.incrementAndGet();
		super.visit(node, arg);
	}

	@Override
	public void visit(ForEachStmt node, Void arg) {
		cyclotomicComplexityCounter.incrementAndGet();
		super.visit(node, arg);
	}

	@Override
	public void visit(IfStmt node, Void arg) {
		cyclotomicComplexityCounter.incrementAndGet();
		super.visit(node, arg);
	}

	@Override
	public void visit(DoStmt node, Void arg) {
		cyclotomicComplexityCounter.incrementAndGet();
		super.visit(node, arg);
	}

	@Override
	public void visit(WhileStmt node, Void arg) {
		cyclotomicComplexityCounter.incrementAndGet();
		super.visit(node, arg);
	}

	@Override
	public void visit(SynchronizedStmt node, Void arg) {
		synchronizedStatementCounter.incrementAndGet();
		super.visit(node, arg);
	}

	@Override
	public void visit(TryStmt node, Void arg) {
		tryStatementCounter.incrementAndGet();
		super.visit(node, arg);
	}

	@Override
	public void visit(CatchClause node, Void arg) {
		catchClauseCounter.incrementAndGet();
		super.visit(node, arg);
	}

}
