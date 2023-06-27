package com.apexon.compass.dashboard.model;

/**
 * Enumerates the possible {@link Collector} types.
 */
public enum CollectorType {

	SCM, CMDB, Incident, Build, Artifact, Deployment, AgileTool, @Deprecated
	Feature, TestResult, @Deprecated
	ScopeOwner, @Deprecated
	Scope, CodeQuality, Test, StaticSecurityScan, LibraryPolicy, ChatOps, Cloud, Product, AppPerformance,
	InfraPerformance, Score, TEAM, Audit, Log, AutoDiscover, InfrastructureAsCode, Docker, Api, ApiAudit, Helm,
	InfrastructureScan;

	public static CollectorType fromString(String value) {
		for (CollectorType collectorType : values()) {
			if (collectorType.toString().equalsIgnoreCase(value)) {
				return collectorType;
			}
		}
		throw new IllegalArgumentException(value + " is not a CollectorType");
	}

}
