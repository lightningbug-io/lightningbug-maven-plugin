package io.lightningbug.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author Mike Krolak
 * @since 1.0
 */

public class ContributorInfo {
	private String name = "";
	private String userId = "";

	/**
	 * Constructs a BuildInfo instance in the system.
	 * 
	 * @param name   non null String representing the name of the contributor
	 * @param userId non null String representing the userId of the contributor
	 */
	@JsonCreator
	public ContributorInfo(final String name, final String userId) {
		if (name != null && !name.isEmpty()) {
			this.name = name;
		} else {
			throw new IllegalArgumentException("name must not be blank");
		}
		if (userId != null && !userId.isEmpty()) {
			this.userId = userId;
		} else {
			throw new IllegalArgumentException("userId must not be blank");
		}
	}

	public String getName() {
		return this.name;
	}

	public String getUserId() {
		return this.userId;
	}

}
