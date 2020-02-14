package io.lightningbug.domain;

import java.io.File;

import org.apache.maven.model.Dependency;

public class DependencyInfo {
	private final String groupId;
	private final String artifactId;
	private final String name;
	private final String version;
	private final String type;
	private File canonicalLocation;

	public DependencyInfo(String groupId, String artifactId, String type, String version, File canonicalLocation) {
		super();
		if ((groupId != null && !groupId.isEmpty()) && (artifactId != null && !artifactId.isEmpty())
				&& (type != null && !type.isEmpty()) && (version != null && !version.isEmpty())
				&& (canonicalLocation != null)) {
			this.groupId = groupId;
			this.artifactId = artifactId;
			this.name = groupId + ":" + artifactId + ":" + type;
			this.version = version;
			this.type = type;
			this.canonicalLocation = canonicalLocation;
		} else {
			throw new IllegalArgumentException("Params can not be null or empty");
		}
	}

	public DependencyInfo(Dependency dependency) {
		if (dependency != null) {
			this.version = dependency.getVersion();
			this.groupId = dependency.getGroupId();
			this.artifactId = dependency.getArtifactId();
			this.name = dependency.getManagementKey();
			this.type = dependency.getType();
		} else {
			throw new IllegalArgumentException("Params can not be null or empty");
		}
	}

	public File getCanonicalLocation() {
		return canonicalLocation;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getType() {
		return type;
	}
}
