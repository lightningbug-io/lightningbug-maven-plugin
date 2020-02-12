package io.lightningbug.domain;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Mike Krolak
 * @since 1.0
 */

public class CodeInfo {

	@JsonProperty
	private final String name;
	@JsonProperty
	private AtomicInteger loc = new AtomicInteger(0);
	@JsonProperty
	private AtomicInteger cyclotomicComplexity = new AtomicInteger(0);
	@JsonProperty
	private Set<ContributorInfo> currentContributors = new LinkedHashSet<ContributorInfo>();
	private List<FunctionInfo> functions = new ArrayList<FunctionInfo>();
	private Set<String> externalDependencies = new LinkedHashSet<String>();

	/**
	 * Constructs a BuildInfo instance in the system.
	 * 
	 * @param name String with the non empty name of the code file
	 */
	@JsonCreator
	public CodeInfo(final String name) {
		if ((name != null) && (!name.isEmpty())) {
			this.name = name;
		} else {
			throw new IllegalArgumentException("name must not be blank");
		}
	}

	/**
	 * Adds a contributor to the list of people who have contributed to this code
	 * 
	 * @param contributor ContributorInfo object containing the information
	 *                    necessary to identify the contributor
	 * @return true unless attempting to add an existing ContributorInfo
	 */
	public boolean addContributor(final ContributorInfo contributor) {
		if (contributor != null) {
			return this.currentContributors.add(contributor);
		} else {
			throw new IllegalArgumentException("contributor must not be null");
		}
	}

	/**
	 * Adds an external dependency to the list of dependencies required for this
	 * code to compile
	 * 
	 * @param externalDependency String providing the location of the dependency
	 * @return true unless attempting to add an existing externalDependency
	 */
	public boolean addExternalDependencies(final String externalDependency) {
		if (externalDependency != null && !externalDependency.isEmpty()) {
			return this.externalDependencies.add(externalDependency);
		} else {
			throw new IllegalArgumentException("externalDependency must not be null or empty");
		}
	}

	/**
	 * Allows you to increment the number of lines of code in this code file
	 * 
	 * @param loc integer representing the number of lines of code to add to this
	 *            code
	 */
	public void addLoc(final int loc) {
		this.loc.addAndGet(loc);
	}

	/**
	 * Allows you to increment the code complexity of this code
	 * 
	 * @param complexity integer representing the complexity to add to this code
	 */
	public void addCyclotomicComplexiy(final int complexity) {
		this.cyclotomicComplexity.addAndGet(complexity);
	}

	/**
	 * Allows you to add a function to this code file
	 * 
	 * @param functionInfo FunctionInfo object to describe another function in this
	 *                     code
	 */
	public void addFunction(FunctionInfo functionInfo) {
		if (functionInfo != null) {
			this.functions.add(functionInfo);
		} else {
			throw new IllegalArgumentException("functionInfo must not be null");
		}
	}

	/**
	 * Simple getter of class variable name
	 * 
	 * @return String containing the name of the code
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Simple getter of class variable loc
	 * 
	 * @return AtomicInteger containing the total loc of the cde
	 */
	public AtomicInteger getLoc() {
		return this.loc;
	}

	/**
	 * Simple getter of class variable cyclotomoic complexity
	 * 
	 * @return AtomicInteger containing the cyclotomic complexity of the code
	 */
	public AtomicInteger getCyclotomicComplexity() {
		return this.cyclotomicComplexity;
	}

	/**
	 * Simple getter of class variable currentContributors
	 * 
	 * @return Keep in mind that this stored as a LinkedHashSet so there should be
	 *         no duplicates
	 */
	public List<ContributorInfo> getCurrentContributors() {
		return this.currentContributors.stream().collect(Collectors.toList());
	}

	/**
	 * Simple getter of class variable functions
	 * 
	 * @return list of functions
	 */
	public List<FunctionInfo> getFunctions() {
		return this.functions.stream().collect(Collectors.toList());
	}

	/**
	 * Simple getter of class variable externalDependencies
	 * 
	 * @return Keep in mind that this stored as a LinkedHashSet so there should be
	 *         no duplicates
	 */
	public List<String> getExternalDependencies() {
		return this.externalDependencies.stream().collect(Collectors.toList());
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
		result = prime * result + (currentContributors.hashCode());
		result = prime * result + (externalDependencies.hashCode());
		result = prime * result + (functions.hashCode());
		result = prime * result + (name.hashCode());
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
		CodeInfo other = (CodeInfo) obj;
		if (!currentContributors.equals(other.currentContributors))
			return false;
		if (!externalDependencies.equals(other.externalDependencies))
			return false;
		if (!functions.equals(other.functions))
			return false;
		if (!name.equals(other.name))
			return false;
		return true;
	}

}
