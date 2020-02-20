/**
 * 
 */
package io.lightningbug.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author M. Krolak
 * @since 0.0.5
 * 
 *        BlameInfo is a POJO used to store information gathered from the Blame
 *        command against a source code management system
 */
public class BlameInfo {

	private int line;
	private ContributorInfo developer;
	private long timestamp;
	private String commitId;

	/**
	 * @param developer non null information about the developer that wrote this line of code
	 * @param commitId  non null, non empty, unique identifier of the commit in the SCM system
	 * @param line      the positive number associated with the line of code referred to by this blame
	 * @param timestamp the positive number of seconds from epoch to the commit
	 */
	@JsonCreator
	public BlameInfo(ContributorInfo developer, String commitId, int line, long timestamp) {
		if (developer != null && commitId != null && !commitId.isEmpty() && line > 0 && timestamp > 0 ) {
			this.developer = developer;
			this.line = line;
			this.timestamp = timestamp;
			this.commitId = commitId;
		} else {
			throw new IllegalArgumentException("Params are all required, cannot be null, empty or less than 0");
		}
	}

	/**
	 * @return the positive number associated with the line of code referred to by this blame
	 */
	public int getLine() {
		return line;
	}

	/**
	 * @return the developer
	 */
	public ContributorInfo getDeveloper() {
		return developer;
	}

	/**
	 * @return the positive number of seconds from epoch to the commit
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @return non null, non empty, unique identifier of the commit in the SCM system
	 */
	public String getCommitId() {
		return commitId;
	}
}
