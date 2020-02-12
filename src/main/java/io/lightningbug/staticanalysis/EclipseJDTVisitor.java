package io.lightningbug.staticanalysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import com.github.javaparser.ast.stmt.CatchClause;

import io.lightningbug.domain.CodeInfo;
import io.lightningbug.domain.FunctionCallInfo;
import io.lightningbug.domain.FunctionInfo;
import io.lightningbug.domain.ProjectInfo;
import io.lightningbug.exceptions.LambdaExceptionWrappers;

public class EclipseJDTVisitor extends ASTVisitor {
	private AtomicInteger cyclotomicComplexityCounter = new AtomicInteger(1);
	private AtomicInteger tryStatementCounter = new AtomicInteger(0);
	private AtomicInteger catchStatementCounter = new AtomicInteger(0);
	private AtomicInteger synchronizedStatementCounter = new AtomicInteger(0);
	private List<FunctionInfo> methods = new ArrayList<FunctionInfo>();
	private List<FunctionCallInfo> functionCalls = new ArrayList<FunctionCallInfo>();
	private List<String> externalDependencies = new ArrayList<String>();
	private ProjectInfo projectInfo;
	private ASTParser parser = ASTParser.newParser(AST.JLS8);
	private CompilationUnit unit;

	public EclipseJDTVisitor(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
	}

	public void processDirectory(File dir) {
		if (dir.isDirectory()) {
			Collection<File> files = FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
			this.parser.setKind(ASTParser.K_COMPILATION_UNIT);
			files.forEach(LambdaExceptionWrappers.handlingConsumerWrapper(file -> this.processJavaFile(file, dir),
					IOException.class));
		} else {
			throw new IllegalArgumentException(dir.getAbsolutePath() + " is not a directory");
		}
	}

	public void processJavaFile(File file, File sourceDir) throws IOException {
		String source = FileUtils.readFileToString(file, "UTF-8");
		String unitName = file.getName();
		this.parser.setResolveBindings(true);
		this.parser.setBindingsRecovery(true);
		this.parser.setUnitName(unitName);
		this.parser.setEnvironment(new String[] { sourceDir.getAbsolutePath() }, new String[] { sourceDir.getAbsolutePath() }, new String[] { "UTF-8" },
				true);
		this.parser.setSource(source.toCharArray());
		this.unit = (CompilationUnit) this.parser.createAST(null);
		this.unit.accept(this);
	}

	@Override
	public void endVisit(final MethodDeclaration md) {
		int startLine = unit.getLineNumber(md.getBody().getStartPosition());
		int endLine = unit.getLineNumber(md.getStartPosition() + md.getLength());
		int loc = 1 + endLine - startLine;
		md.getModifiers();
		List<String> paramsAsStrings = new ArrayList<String>();
		List<SingleVariableDeclaration> params = md.parameters();
		for (SingleVariableDeclaration variableDeclaration : params) {
			StringBuilder param = new StringBuilder("");
			param.append(variableDeclaration.getStructuralProperty(SingleVariableDeclaration.TYPE_PROPERTY).toString());
			for (int i = 0; i < variableDeclaration.getExtraDimensions(); i++) {
				param.append("[]");
				break;
			}
			paramsAsStrings.add(param.toString());
		}
		FunctionInfo functionInfo = new FunctionInfo(md.getName().getFullyQualifiedName(), loc, startLine, endLine,
				paramsAsStrings, md.getReturnType2() + "", cyclotomicComplexityCounter);
		functionCalls.forEach(functionCall -> functionInfo.addFunctionCall(functionCall));
		methods.add(functionInfo);
		functionCalls.clear();
		cyclotomicComplexityCounter.set(1);
		this.catchStatementCounter.set(0);
		this.tryStatementCounter.set(0);
	}

	@Override
	public void endVisit(final TypeDeclaration td) {
		if (!td.isInterface()) {
			CodeInfo codeInfo = new CodeInfo(td.getName().getFullyQualifiedName());
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
	public boolean visit(final ReturnStatement node) {
		return true;
	}

	@Override
	public boolean visit(SynchronizedStatement node) {
		this.synchronizedStatementCounter.incrementAndGet();
		return true;
	}

	@Override
	public boolean visit(ExpressionStatement node) {
		return true;
	}

	@Override
	public boolean visit(MethodInvocation node) {
		IMethodBinding binding = node.resolveMethodBinding();
		if (binding != null) {
			IMethodBinding methodBinding = binding.getMethodDeclaration();
			if (methodBinding != null) {
				ITypeBinding declaringClass = methodBinding.getDeclaringClass();
				if (declaringClass != null) {
					String className = declaringClass.getName();
					String packageName = declaringClass.getQualifiedName().substring(0,
							declaringClass.getQualifiedName().length() - className.length());
					FunctionCallInfo functionCall = new FunctionCallInfo(packageName, className,
							node.getName().getFullyQualifiedName(), unit.getLineNumber(node.getStartPosition()));
					functionCalls.add(functionCall);
				}
			}
		}
		return true;
	}

	@Override
	public boolean visit(TypeDeclarationStatement node) {
		return true;
	}

	@Override
	public boolean visit(IfStatement node) {
		cyclotomicComplexityCounter.incrementAndGet();
		return true;
	}

	@Override
	public boolean visit(ForStatement node) {
		cyclotomicComplexityCounter.incrementAndGet();
		return true;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		return true;
	}

	@Override
	public boolean visit(TypeDeclaration typeDeclaration) {
		return !typeDeclaration.isInterface();
	}

	@Override
	public boolean visit(ImportDeclaration node) {
		externalDependencies.add(node.getName().getFullyQualifiedName());
		return true;
	}

	@Override
	public boolean visit(DoStatement node) {
		cyclotomicComplexityCounter.incrementAndGet();
		return true;
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		cyclotomicComplexityCounter.incrementAndGet();
		return true;
	}

	@Override
	public boolean visit(SwitchStatement node) {
		return true;
	}

	@Override
	public boolean visit(BreakStatement node) {
		cyclotomicComplexityCounter.incrementAndGet();
		return true;
	}

	@Override
	public boolean visit(WhileStatement node) {
		cyclotomicComplexityCounter.incrementAndGet();
		return true;
	}

	@Override
	public boolean visit(TryStatement node) {
		this.tryStatementCounter.incrementAndGet();
		return true;
	}
	
	@Override
	public boolean visit(ConstructorInvocation node) {
		return true;
	}

	@Override
	public boolean visit(SuperConstructorInvocation node) {
		return true;
	}

	public boolean visit(SuperMethodInvocation node) {
		return true;
	}
}
