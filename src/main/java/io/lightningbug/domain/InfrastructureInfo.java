package io.lightningbug.domain;

import java.io.File;
import java.text.NumberFormat;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

public class InfrastructureInfo {
	private final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

	private Runtime runtime = Runtime.getRuntime();
	
	@JsonProperty
	private HardwareAbstractionLayer sysInfo = new SystemInfo().getHardware();
	
	@JsonGetter("osName")
	public String getOperatingSystemName() {
		return System.getProperty("os.name");
	}
	
	@JsonGetter("javaRuntimeVersion")
	public String getJavaVersion() {
		return System.getProperty("java.version");
	}

	@JsonGetter("javaRuntimeVendor")
	public String getJavaVendor() {
		return System.getProperty("java.vendor");
	}

	@JsonGetter("javaVMSpecificationVersion")
	public String getJavaVMSpecificationVersion() {
		return System.getProperty("java.vm.specification.version");
	}

	@JsonGetter("javaVMSpecificationVendor")
	public String getJavaVMSpecificationVendor() {
		return System.getProperty("java.vm.specification.vendor");
	}
	
	@JsonGetter("javaVMSpecificationName")
	public String getJavaVMSpecificationName() {
		return System.getProperty("java.vm.specification.name");
	}
	
	@JsonGetter("javaVMVersion")
	public String getJavaVMVersion() {
		return System.getProperty("java.vm.version");
	}
	
	@JsonGetter("javaVMVendor")
	public String getJavaVMVendor() {
		return System.getProperty("java.vm.vendor");
	}
	
	@JsonGetter("javaRuntimeSpecificationVersion")
	public String getJavaRuntimeSpecificationVersion() {
		return System.getProperty("java.specification.version");
	}

	@JsonGetter("javaRuntimeSpecificationVendor")
	public String getJavaRuntimeSpecificationVendor() {
		return System.getProperty("java.specification.vendor");
	}
	
	@JsonGetter("javaRuntimeSpecificationName")
	public String getJavaRuntimeSpecificationName() {
		return System.getProperty("java.specification.name");
	}
	@JsonGetter("osVersion")
	public String getOperatingSystemVersion() {
		return System.getProperty("os.version");
	}

	@JsonGetter("osArchitecture")
	public String getOperatingSystemArchitecture() {
		return System.getProperty("os.arch");
	}

	@JsonGetter("totalMemory")
	public long getTotalMemory() {
		return Runtime.getRuntime().totalMemory();
	}

	@JsonGetter("usedMemory")
	public long getUsedMemory() {
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	@JsonGetter("maxMemory")
	public String getMaxMemory() {
		return NUMBER_FORMAT.format(runtime.maxMemory() / 1024);
	}

	@JsonGetter("freeMemory")
	public String getFreeMemory() {
		return NUMBER_FORMAT.format(runtime.freeMemory() / 1024);
	}

	@JsonGetter("allocatedMemory")
	public String getallocatedMemory() {
		return NUMBER_FORMAT.format(runtime.totalMemory() / 1024);
	}

	@JsonGetter("numberOfCores")
	public long getNumberOfCores() {
		return runtime.availableProcessors();
	}

	@JsonGetter("totalFreeMemory")
	public String getTotalFreeMemory() {
		return NUMBER_FORMAT.format((runtime.freeMemory() + (runtime.maxMemory() - runtime.totalMemory())) / 1024);
	}

	public String DiskInfo() {
		File[] roots = File.listRoots();
		StringBuilder sb = new StringBuilder();
		for (File root : roots) {
			sb.append("File system root: ");
			sb.append(root.getAbsolutePath());
			sb.append("<br/>");
			sb.append("Total space (bytes): ");
			sb.append(root.getTotalSpace());
			sb.append("<br/>");
			sb.append("Free space (bytes): ");
			sb.append(root.getFreeSpace());
			sb.append("<br/>");
			sb.append("Usable space (bytes): ");
			sb.append(root.getUsableSpace());
			sb.append("<br/>");
		}
		return sb.toString();
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