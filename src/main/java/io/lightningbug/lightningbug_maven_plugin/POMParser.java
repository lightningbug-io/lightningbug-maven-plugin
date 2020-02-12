package io.lightningbug.lightningbug_maven_plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;

import io.lightningbug.domain.DependencyInfo;

/**
 * @author Mike Krolak
 * @since 1.0
 */

public class POMParser {
	private Log log;
	private RepositorySystemSession artifactRepoSession;
	private RepositorySystem repoSystem;

	public POMParser(RepositorySystem repoSystem, RepositorySystemSession artifactRepoSession, Log log) {
		this.repoSystem = repoSystem;
		this.log = log;
		this.artifactRepoSession = artifactRepoSession;
	}

	/**
	 * Constructs a List of transitive dependencies of a specified Maven project
	 * 
	 * @param project non null MavenProject representing the project identified in
	 *                the pom.xml
	 * @return a collection of all the transitive dependencies of the specified
	 *         Maven project
	 * @throws MojoExecutionException If an error happen while resolving the
	 *                                artifact.
	 */
	public List<DependencyInfo> resolveAllTransitiveDependencies(MavenProject project) throws MojoExecutionException {
		if (project != null) {
			List<DependencyInfo> canonicalPaths = new ArrayList<DependencyInfo>();
			for (Artifact unresolvedArtifact : project.getDependencyArtifacts()) {
				String artifactId = unresolvedArtifact.getArtifactId();
				org.eclipse.aether.artifact.Artifact aetherArtifact = new DefaultArtifact(
						unresolvedArtifact.getGroupId(), unresolvedArtifact.getArtifactId(),
						unresolvedArtifact.getClassifier(), unresolvedArtifact.getType(),
						unresolvedArtifact.getVersion());
				ArtifactRequest req = new ArtifactRequest().setRepositories(project.getRemoteProjectRepositories())
						.setArtifact(aetherArtifact);
				ArtifactResult resolutionResult = resolveArtifact(artifactId, req);

				// The file should exists, but we never know.
				File file = resolutionResult.getArtifact().getFile();
				if (file == null || !file.exists()) {
					log.warn("Artifact " + artifactId
							+ " has no attached file. Its content will not be added into the list of transitive dependencies");
					continue;
				}
				DependencyInfo dependency = new DependencyInfo(unresolvedArtifact.getGroupId(),
						unresolvedArtifact.getArtifactId(), unresolvedArtifact.getType(),
						unresolvedArtifact.getVersion(), file);
				canonicalPaths.add(dependency);
			}
			return canonicalPaths;
		} else {
			throw new IllegalArgumentException("Maven project must not be null");
		}
	}

	protected ArtifactResult resolveArtifact(String artifactId, ArtifactRequest req) throws MojoExecutionException {
		ArtifactResult resolutionResult;
		try {
			resolutionResult = this.repoSystem.resolveArtifact(this.artifactRepoSession, req);
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException("Artifact " + artifactId + " could not be resolved.", e);
		}
		return resolutionResult;
	}
}
