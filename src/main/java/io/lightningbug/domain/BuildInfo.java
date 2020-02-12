package io.lightningbug.domain;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class BuildInfo {
	private final ZonedDateTime startTime;
	private final ZonedDateTime endTime;
	private final InfrastructureInfo infrastructureInfo;
	private final ProjectInfo projectInfo;

	/**
	 * Constructs a BuildInfo instance in the system.
	 * 
	 * @param infrastructureInfo InfrastructureInfo object
	 * @param projectInfo        ProjectInfo object
	 * @param startTime          The zoned date time of when the build starts
	 * @param endTime            The zoned date time of when the build stops
	 */
	@JsonCreator
	public BuildInfo(@JsonProperty("infrastructureInfo") final InfrastructureInfo infrastructureInfo,
			@JsonProperty("projectInfo") final ProjectInfo projectInfo,
			@JsonProperty("startTime") final ZonedDateTime startTime,
			@JsonProperty("endTime") final ZonedDateTime endTime) {
		super();
		if ((infrastructureInfo != null) && (projectInfo != null) && (startTime != null) && (endTime != null)) {
			this.infrastructureInfo = infrastructureInfo;
			this.projectInfo = projectInfo;
			this.startTime = startTime;
			this.endTime = endTime;
		} else {
			throw new IllegalArgumentException("Null parameters are not acceptable");
		}
	}

	@JsonGetter("startTime")
	public String getStartTime() {
		return startTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
	}

	@JsonGetter("endTime")
	public String getEndTime() {
		return endTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
	}

	public InfrastructureInfo getInfrastructureInfo() {
		return infrastructureInfo;
	}

	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}

	public String toString() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "";
		}
	}
}
