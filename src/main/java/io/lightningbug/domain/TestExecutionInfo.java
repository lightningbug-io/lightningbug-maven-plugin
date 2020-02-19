package io.lightningbug.domain;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestExecutionInfo {
	private ZonedDateTime startTime;
	private ZonedDateTime endTime;
	private boolean signal = false;
	private String name;
	private int mutationScore = 0;

	@JsonCreator
	public TestExecutionInfo(String name, ZonedDateTime startTime) {
		if (name != null && startTime != null && !name.isEmpty()) {
			this.name = name;
			this.startTime = startTime;
		} else {
			throw new IllegalArgumentException("Params can not be null");
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

	public void setEndTime(ZonedDateTime endTime) {
		if(endTime !=null) {
		if (this.endTime == null) {
			this.endTime = endTime;
		} else {
			throw new IllegalArgumentException("end time for " + this.name + " is already set");
		}
		}
		else {
			throw new IllegalArgumentException("endTime cannot be null");
		}
	}

	public boolean isSignal() {
		return signal;
	}

	public void setSignal(boolean signal) {
		this.signal = signal;
	}

	public String getName() {
		return name;
	}

	public int getMutationScore() {
		return mutationScore;
	}

	public void setMutationScore(int mutationScore) {
		this.mutationScore = mutationScore;
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
