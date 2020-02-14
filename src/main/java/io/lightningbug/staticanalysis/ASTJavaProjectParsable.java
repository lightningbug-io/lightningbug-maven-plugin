package io.lightningbug.staticanalysis;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.lightningbug.domain.DependencyInfo;
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


	public ProjectInfo processDirectory(File dir, List<DependencyInfo> externalJars);

	public void processJavaFile(File file) throws IOException;

}
