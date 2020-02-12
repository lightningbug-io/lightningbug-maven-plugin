package io.lightningbug.staticanalysis;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.lightningbug.domain.DependencyInfo;
import io.lightningbug.domain.FunctionCallInfo;
import io.lightningbug.domain.FunctionInfo;
import io.lightningbug.domain.ProjectInfo;

/**
 * @author Mike Krolak
 * @since 1.0
 */

public interface ASTJavaProjectParsable {
	AtomicInteger cyclotomicComplexityCounter = new AtomicInteger(1);
	AtomicInteger synchronizedStatementCounter = new AtomicInteger(0);
	AtomicInteger tryStatementCounter = new AtomicInteger(0);
	AtomicInteger catchClauseCounter = new AtomicInteger(0);
	List<FunctionInfo> methods = new ArrayList<FunctionInfo>();
	List<FunctionCallInfo> functionCalls = new ArrayList<FunctionCallInfo>();
	List<String> externalDependencies = new ArrayList<String>();

	public ProjectInfo processDirectory(File dir, List<DependencyInfo> externalJars);

	public void processJavaFile(File file) throws IOException;

}
