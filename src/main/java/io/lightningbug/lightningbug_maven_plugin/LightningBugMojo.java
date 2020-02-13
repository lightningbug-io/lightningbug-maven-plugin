package io.lightningbug.lightningbug_maven_plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.List;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;

import io.lightningbug.domain.BuildInfo;
import io.lightningbug.domain.DependencyInfo;
import io.lightningbug.domain.InfrastructureInfo;
import io.lightningbug.domain.ProjectInfo;
import io.lightningbug.staticanalysis.JavaParserVisitor;

/**
 * @author Mike Krolak
 * @since 1.0
 */

@Mojo(name = "lightningbug", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE, threadSafe = true)

public class LightningBugMojo extends AbstractMojo {
	@Component
	private RepositorySystem repoSystem;
	/**
	 * The location of the source code
	 * 
	 * @parameter expression="${project.build.sourceDirectory}"
	 * @required
	 * @readonly
	 */
	@Parameter(property = "generateCodeReport", required = false)
	private boolean generateBuildReport;

	/**
	 * The location of the source code
	 * 
	 * @parameter expression="${project.build.sourceDirectory}"
	 * @required
	 * @readonly
	 */
	@Parameter(defaultValue = "${project.build.sourceDirectory}", property = "sourceDirectory", required = true)
	private File sourceDirectory;
	/**
	 * The location of the test code
	 * 
	 * @parameter expression="${project.build.sourceDirectory}"
	 * @required
	 * @readonly
	 */
	@Parameter(defaultValue = "${project.build.testSourceDirectory}", property = "testSourceDirectory", required = true)
	private File testSourceDirectory;

	/**
	 * The Maven project.
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	MavenProject project;

	@Parameter(defaultValue = "${repositorySystemSession}", readonly = true, required = true)
	private RepositorySystemSession artifactRepoSession;

	/**
	 * Executes the plug-in.
	 * 
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException {
		ZonedDateTime startTime = ZonedDateTime.now();
		getLog().info("Starting Static Code Analysis");
		project.getCompileSourceRoots().forEach(System.out::println);
		project.getTestCompileSourceRoots().forEach(System.out::println);
		POMParser pomParser = new POMParser(this.repoSystem, artifactRepoSession, getLog());
		List<DependencyInfo> transitiveDependencies = pomParser.resolveAllTransitiveDependencies(this.project);
		ProjectInfo projectInfo = new ProjectInfo(project, ZonedDateTime.now());
		File projectDirectory = new File(this.sourceDirectory.getAbsolutePath());
		JavaParserVisitor jpVisitor = new JavaParserVisitor(projectInfo);
		jpVisitor.processDirectory(projectDirectory, transitiveDependencies);
		jpVisitor.processDirectory(new File(this.testSourceDirectory.getAbsolutePath()), transitiveDependencies);
		BuildInfo buildInfo = new BuildInfo(new InfrastructureInfo(), projectInfo, startTime, ZonedDateTime.now());
		getLog().debug("About to start generating the file");
		generateBuildReport(buildInfo);
		getLog().info(buildInfo.toString());
	}

	private void generateBuildReport(BuildInfo buildInfo) {
		if (generateBuildReport) {
			File dir = new File(project.getBuild().getDirectory());
			if (dir != null && dir.exists() && dir.isDirectory()) {
				File file = new File(dir.getAbsolutePath() + "\\lightningbug" + System.currentTimeMillis() + ".json");
				try {
					if (file.createNewFile()) {
						try (PrintWriter writer = new PrintWriter(file)) {
							writer.println(buildInfo.toString());
						} catch (FileNotFoundException e) {
							getLog().debug(e.getMessage());
						}
					}
				} catch (IOException e) {
					getLog().debug(e.getMessage());
				}
			}
		}
	}

}
