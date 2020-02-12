package io.lightningbug.domain;

public class FunctionCallInfo {
	private int lineNumber = 0;
	private String packageName;
	private String codeName;
	private String functionName;

	public FunctionCallInfo(final String packageName, final String codeName, final String functionName,
			final int lineNumber) {
		super();
		if ((packageName != null && !packageName.isEmpty()) && (codeName != null && !codeName.isEmpty())
				&& (functionName != null && !functionName.isEmpty()) && lineNumber > 0) {
			this.packageName = packageName;
			this.codeName = codeName;
			this.functionName = functionName;
			this.lineNumber = lineNumber;
		} else {
			throw new IllegalArgumentException("parameters must be non empty, non null and greater than 0");
		}
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getCodeName() {
		return codeName;
	}

	public String getFunctionName() {
		return functionName;
	}

}
