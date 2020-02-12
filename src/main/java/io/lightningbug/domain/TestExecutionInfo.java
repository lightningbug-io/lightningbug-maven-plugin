package io.lightningbug.domain;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestExecutionInfo {
	private ZonedDateTime startTime;
	private ZonedDateTime endTime;
	private boolean signal;
	private String name;
	private int loc = 0;
	private int mutationScore = 0;
	private AtomicInteger cyclotomicComplexity = new AtomicInteger(0);

	@JsonCreator
	public TestExecutionInfo(String name, ZonedDateTime startTime) {
		this.name = name;
		this.startTime = startTime;
	}

	@JsonGetter("startTime")
	public String getStartTime() {
		return startTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
	}

	@JsonGetter("endTime")
	public String getEndTime() {
		if(this.endTime != null) {
		return endTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
		}
		else { 
			return "";
		}
	}

	public void setEndTime(ZonedDateTime endTime) {
		if (this.endTime == null) {
			this.endTime = endTime;
		} else {
			throw new IllegalArgumentException("end time for " + this.name + " is already set");
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
