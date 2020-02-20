package io.lightningbug.domain;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestSuiteInfo {
	private final ZonedDateTime startTime;
	private ZonedDateTime endTime;
	private final String name;
	private Map<String, TestExecutionInfo> testExecutions = new HashMap<>();

	@JsonCreator
	public TestSuiteInfo(final String name, ZonedDateTime startTime) {
		if ((name != null) && (startTime != null)) {
			this.name = name;
			this.startTime = startTime;
		} else {
			throw new IllegalArgumentException("Name and/or startTime can not be null");
		}
	}

	@JsonGetter("startTime")
	public String getStartTime() {
		return startTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
	}

	@JsonGetter("endTime")
	public String getEndTime() {
		if (this.endTime != null) {
			return endTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
		} else {
			return "";
		}
	}

	public void setEndTime(final ZonedDateTime endTime) {
		if (endTime != null) {
			if (this.endTime == null) {
				this.endTime = endTime;
			} else {
				throw new IllegalArgumentException("end time for " + this.name + " is already set");
			}
		} else {
			throw new IllegalArgumentException("end time can not be null");
		}
	}

	public String getName() {
		return name;
	}

	public Map<String, TestExecutionInfo> getTestExecutions() {
		return testExecutions;
	}

	public void addTestExecution(final TestExecutionInfo testInfo) {
		if (testInfo != null) {
			if (!testExecutions.containsKey(testInfo.getName())) {
				testExecutions.put(testInfo.getName(), testInfo);
			} else {
				throw new IllegalArgumentException(testInfo.getName() + "  already exists in the testExecutions");
			}
		} else {
			throw new IllegalArgumentException("testInfo is null");
		}
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
