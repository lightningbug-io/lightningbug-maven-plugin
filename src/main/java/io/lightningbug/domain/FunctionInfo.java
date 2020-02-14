package io.lightningbug.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Mike Krolak
 * @since 1.0
 */
public class FunctionInfo {

	private final String name;
	private int loc = 0;
	private int startLine = 0;
	private int endLine = 0;
	private AtomicInteger cyclotomicComplexity = new AtomicInteger(0);
	private final String returnType;
	private List<ContributorInfo> developers = new ArrayList<>();
	private List<String> parameters = new ArrayList<>();
	private List<FunctionCallInfo> functionCalls = new ArrayList<>();

	public FunctionInfo(final String name, final int loc, final int startLine, final int endLine,
			final List<String> params, final String returnType, final AtomicInteger cyclotomicComplexity) {
		if ((name != null && !name.isEmpty()) && (loc > 0) && (startLine > 0) && (endLine > 0) && (params != null)
				&& (returnType != null && !returnType.isEmpty()) && (cyclotomicComplexity != null)) {
			this.name = name;
			this.startLine = startLine;
			this.endLine = endLine;
			this.loc = loc;
			this.parameters = params.stream().collect(Collectors.toList());
			this.returnType = returnType;
			this.cyclotomicComplexity = cyclotomicComplexity;
		} else {
			throw new IllegalArgumentException("parameters must be non empty, non null and greater than 0");
		}
	}

	public List<FunctionCallInfo> getFunctionCalls() {
		return this.functionCalls;
	}

	public void addFunctionCall(FunctionCallInfo functionCall) {
		this.functionCalls.add(functionCall);
	}

	public List<ContributorInfo> getDevelopers() {
		return developers;
	}

	public void addDeveloper(ContributorInfo developer) {
		this.developers.add(developer);
	}

	public int getStartLine() {
		return startLine;
	}

	public int getEndLine() {
		return endLine;
	}

	public List<String> getParameters() {
		return this.parameters.stream().collect(Collectors.toList());
	}

	public String getReturnType() {
		return "" + returnType;
	}

	public String getName() {
		return this.name;
	}

	public int getLoc() {
		return this.loc;
	}

	public AtomicInteger getCyclotomicComplexity() {
		return this.cyclotomicComplexity;
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
