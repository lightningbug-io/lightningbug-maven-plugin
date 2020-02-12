package io.lightningbug.domain;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.maven.project.MavenProject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Mike Krolak
 * @since 1.0
 */

public class ProjectInfo {

	private final String name;
	private final String version;
	private final String organization;
	private final ZonedDateTime releaseDate;
	private final List<ContributorInfo> developers = new ArrayList<ContributorInfo>();
	private final List<ContributorInfo> testers = new ArrayList<ContributorInfo>();
	private AtomicInteger loc = new AtomicInteger(0);
	private List<CodeInfo> classes = new ArrayList<CodeInfo>();
	private List<CodeInfo> testClasses = new ArrayList<CodeInfo>();
	private List<DependencyInfo> transitiveDependencies = new ArrayList<DependencyInfo>();

	public ProjectInfo(final MavenProject project, final ZonedDateTime releaseDate) {
		if (project != null && releaseDate != null) {
			this.name = project.getName();
			this.version = project.getVersion();
			if (project.getOrganization() != null) {
				this.organization = project.getOrganization().getName();
			} else {
				this.organization = "";
			}
			this.releaseDate = releaseDate;
		} else {
			throw new IllegalArgumentException("parameters can't be null");
		}
	}

	@JsonCreator
	public ProjectInfo(final String name, final String version, final String organization,
			final ZonedDateTime releaseDate) {
		if (name != null && !name.isEmpty()) {
			this.name = name;
		} else {
			throw new IllegalArgumentException("name must not be blank");
		}
		if (version != null && !version.isEmpty()) {
			this.version = version;
		} else {
			throw new IllegalArgumentException("version must not be blank");
		}
		this.organization = organization == null ? "" : organization;
		if (releaseDate != null) {
			this.releaseDate = releaseDate;
		} else {
			throw new IllegalArgumentException("releaseDate must not be blank");
		}
	}

	public void addClass(final CodeInfo ci) {
		if (ci != null) {
			this.classes.add(ci);
			// this.loc.addAndGet(ci.getLoc().get());
		} else {
			throw new IllegalArgumentException("code info info can't be null");
		}
	}

	public void addTestClass(final CodeInfo ci) {
		if (ci != null) {
			this.testClasses.add(ci);
		} else {
			throw new IllegalArgumentException("code info can't be null");
		}
	}

	public void addTransitiveDependency(final DependencyInfo di) {
		if (di != null) {
			this.transitiveDependencies.add(di);
		} else {
			throw new IllegalArgumentException("dependency info can't be null");
		}
	}

	public void addDeveloper(final ContributorInfo developer) {
		if (developer != null) {
			this.developers.add(developer);
		} else {
			throw new IllegalArgumentException("developer can't be null");
		}
	}

	public void addTester(final ContributorInfo tester) {
		if (tester != null) {
			this.testers.add(tester);
		} else {
			throw new IllegalArgumentException("tester can't be null");
		}
	}

	public String getName() {
		return this.name;
	}

	public List<ContributorInfo> getDevelopers() {
		return this.developers.stream().collect(Collectors.toList());
	}

	public List<ContributorInfo> getTesters() {
		return this.testers.stream().collect(Collectors.toList());
	}

	public List<DependencyInfo> getTransitiveDependencies() {
		return this.transitiveDependencies.stream().collect(Collectors.toList());
	}

	public String getVersion() {
		return this.version;
	}

	public String getOrganization() {
		return this.organization;
	}

	public AtomicInteger getLoc() {
		return this.loc;
	}

	public List<CodeInfo> getClasses() {
		return this.classes.stream().collect(Collectors.toList());
	}

	public List<CodeInfo> getTestClasses() {
		return this.testClasses.stream().collect(Collectors.toList());
	}

	public String toString() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (classes.hashCode());
		result = prime * result + (developers.hashCode());
		result = prime * result + (loc.hashCode());
		result = prime * result + (name.hashCode());
		result = prime * result + (organization.hashCode());
		result = prime * result + (releaseDate.hashCode());
		result = prime * result + (testClasses.hashCode());
		result = prime * result + (testers.hashCode());
		result = prime * result + (transitiveDependencies.hashCode());
		result = prime * result + (version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectInfo other = (ProjectInfo) obj;
		if (!classes.equals(other.classes))
			return false;
		if (!developers.equals(other.developers))
			return false;
		if (!(loc.get() == other.loc.get()))
			return false;
		if (!name.equals(other.name))
			return false;
		if (!organization.equals(other.organization))
			return false;
		if (!releaseDate.equals(other.releaseDate))
			return false;
		if (!testClasses.equals(other.testClasses))
			return false;
		if (!testers.equals(other.testers))
			return false;
		if (!transitiveDependencies.equals(other.transitiveDependencies))
			return false;
		if (!version.equals(other.version))
			return false;
		return true;
	}

}
