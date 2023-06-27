package com.apexon.compass.dashboard.model;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LibraryPolicyResultTest {

	@Test
	void addThreat() {
		LibraryPolicyResult result = new LibraryPolicyResult();
		result.addThreat(LibraryPolicyType.License, LibraryPolicyThreatLevel.Critical,
				LibraryPolicyThreatDisposition.Open, "Open:Review Requested", "component1", "0", "7");

		assertEquals(1, result.getThreats().size());
		assertEquals(1, result.getThreats().keySet().size());
		assertEquals(1, result.getThreats().values().size());
		assertEquals(LibraryPolicyType.License, result.getThreats().keySet().iterator().next());
		Set<LibraryPolicyResult.Threat> threats = result.getThreats().get(LibraryPolicyType.License);
		LibraryPolicyResult.Threat threat = threats.iterator().next();
		assertEquals(1, threat.getCount());
		assertEquals(LibraryPolicyThreatLevel.Critical, threat.getLevel());
		assertEquals("component1##Open##0##Open:Review Requested##7", threat.getComponents().iterator().next());
		assertEquals(1, threat.getDispositionCounts().size());
		assertEquals(1, threat.getDispositionCounts().get(LibraryPolicyThreatDisposition.Open).intValue());

		result.addThreat(LibraryPolicyType.License, LibraryPolicyThreatLevel.Critical,
				LibraryPolicyThreatDisposition.Open, "Open", "component2", "0", "7");
		assertEquals(1, result.getThreats().size());
		assertEquals(1, result.getThreats().keySet().size());
		assertEquals(1, result.getThreats().values().size());
		assertEquals(LibraryPolicyType.License, result.getThreats().keySet().iterator().next());
		threats = result.getThreats().get(LibraryPolicyType.License);
		assertEquals(1, threats.size());
		threat = threats.iterator().next();
		assertEquals(2, threat.getCount());
		assertEquals(LibraryPolicyThreatLevel.Critical, threat.getLevel());
		assertTrue(threat.getComponents().contains("component1##Open##0##Open:Review Requested##7"));
		assertTrue(threat.getComponents().contains("component2##Open##0##Open##7"));
		assertEquals(1, threat.getDispositionCounts().size());
		assertEquals(2, threat.getDispositionCounts().get(LibraryPolicyThreatDisposition.Open).intValue());

		result.addThreat(LibraryPolicyType.License, LibraryPolicyThreatLevel.Critical,
				LibraryPolicyThreatDisposition.Closed, "Closed:False Positive", "component3", "0", "7");
		assertEquals(1, result.getThreats().size());
		assertEquals(1, result.getThreats().keySet().size());
		assertEquals(1, result.getThreats().values().size());
		assertEquals(LibraryPolicyType.License, result.getThreats().keySet().iterator().next());
		threats = result.getThreats().get(LibraryPolicyType.License);
		assertEquals(1, threats.size());
		threat = threats.iterator().next();
		assertEquals(3, threat.getCount());
		assertEquals(LibraryPolicyThreatLevel.Critical, threat.getLevel());
		assertTrue(threat.getComponents().contains("component1##Open##0##Open:Review Requested##7"));
		assertTrue(threat.getComponents().contains("component2##Open##0##Open##7"));
		assertTrue(threat.getComponents().contains("component3##Closed##0##Closed:False Positive##7"));
		assertEquals(2, threat.getDispositionCounts().size());
		assertEquals(2, threat.getDispositionCounts().get(LibraryPolicyThreatDisposition.Open).intValue());
		assertEquals(1, threat.getDispositionCounts().get(LibraryPolicyThreatDisposition.Closed).intValue());

		result.addThreat(LibraryPolicyType.License, LibraryPolicyThreatLevel.High, LibraryPolicyThreatDisposition.Open,
				"Open:Legal Review Requested", "component4", "0", "7");
		assertEquals(1, result.getThreats().size());
		assertEquals(1, result.getThreats().keySet().size());
		assertEquals(1, result.getThreats().values().size());
		assertEquals(LibraryPolicyType.License, result.getThreats().keySet().iterator().next());
		threats = result.getThreats().get(LibraryPolicyType.License);
		assertEquals(2, threats.size());
		assertEquals(1,
				threats.stream().filter(t -> Objects.equals(t.getLevel(), LibraryPolicyThreatLevel.Critical)).count());
		assertEquals(1,
				threats.stream().filter(t -> Objects.equals(t.getLevel(), LibraryPolicyThreatLevel.High)).count());
		threat = threats.stream()
			.filter(t -> Objects.equals(t.getLevel(), LibraryPolicyThreatLevel.Critical))
			.findFirst()
			.orElse(null);
		assertNotNull(threat);
		assertEquals(3, threat.getCount());
		assertEquals(LibraryPolicyThreatLevel.Critical, threat.getLevel());
		assertTrue(threat.getComponents().contains("component1##Open##0##Open:Review Requested##7"));
		assertTrue(threat.getComponents().contains("component2##Open##0##Open##7"));
		assertTrue(threat.getComponents().contains("component3##Closed##0##Closed:False Positive##7"));
		assertEquals(2, threat.getDispositionCounts().size());
		assertEquals(2, threat.getDispositionCounts().get(LibraryPolicyThreatDisposition.Open).intValue());
		assertEquals(1, threat.getDispositionCounts().get(LibraryPolicyThreatDisposition.Closed).intValue());
		threat = threats.stream()
			.filter(t -> Objects.equals(t.getLevel(), LibraryPolicyThreatLevel.High))
			.findFirst()
			.orElse(null);
		assertNotNull(threat);
		assertEquals(1, threat.getCount());
		assertEquals(LibraryPolicyThreatLevel.High, threat.getLevel());
		assertTrue(threat.getComponents().contains("component4##Open##0##Open:Legal Review Requested##7"));
		assertEquals(1, threat.getDispositionCounts().size());
		assertEquals(1, threat.getDispositionCounts().get(LibraryPolicyThreatDisposition.Open).intValue());

		result.addThreat(LibraryPolicyType.Security, LibraryPolicyThreatLevel.High, LibraryPolicyThreatDisposition.Open,
				"Open:Unconfirmed", "component5", "0", "7");
		assertEquals(2, result.getThreats().size());
		assertEquals(2, result.getThreats().keySet().size());
		assertEquals(2, result.getThreats().values().size());
		threats = result.getThreats().get(LibraryPolicyType.License);
		assertEquals(2, threats.size());
		assertEquals(1,
				threats.stream().filter(t -> Objects.equals(t.getLevel(), LibraryPolicyThreatLevel.Critical)).count());
		assertEquals(1,
				threats.stream().filter(t -> Objects.equals(t.getLevel(), LibraryPolicyThreatLevel.High)).count());
		threat = threats.stream()
			.filter(t -> Objects.equals(t.getLevel(), LibraryPolicyThreatLevel.Critical))
			.findFirst()
			.orElse(null);
		assertNotNull(threat);
		assertEquals(3, threat.getCount());
		assertEquals(LibraryPolicyThreatLevel.Critical, threat.getLevel());
		assertTrue(threat.getComponents().contains("component1##Open##0##Open:Review Requested##7"));
		assertTrue(threat.getComponents().contains("component2##Open##0##Open##7"));
		assertTrue(threat.getComponents().contains("component3##Closed##0##Closed:False Positive##7"));
		assertEquals(2, threat.getDispositionCounts().size());
		assertEquals(2, threat.getDispositionCounts().get(LibraryPolicyThreatDisposition.Open).intValue());
		assertEquals(1, threat.getDispositionCounts().get(LibraryPolicyThreatDisposition.Closed).intValue());
		threat = threats.stream()
			.filter(t -> Objects.equals(t.getLevel(), LibraryPolicyThreatLevel.High))
			.findFirst()
			.orElse(null);
		assertNotNull(threat);
		assertEquals(1, threat.getCount());
		assertEquals(LibraryPolicyThreatLevel.High, threat.getLevel());
		assertTrue(threat.getComponents().contains("component4##Open##0##Open:Legal Review Requested##7"));
		assertEquals(1, threat.getDispositionCounts().size());
		assertEquals(1, threat.getDispositionCounts().get(LibraryPolicyThreatDisposition.Open).intValue());

		threats = result.getThreats().get(LibraryPolicyType.Security);
		assertEquals(1, threats.size());
		threat = threats.stream()
			.filter(t -> Objects.equals(t.getLevel(), LibraryPolicyThreatLevel.High))
			.findFirst()
			.orElse(null);
		assertNotNull(threat);
		assertEquals(1, threat.getCount());
		assertEquals(LibraryPolicyThreatLevel.High, threat.getLevel());
		assertTrue(threat.getComponents().contains("component5##Open##0##Open:Unconfirmed##7"));
		assertEquals(1, threat.getDispositionCounts().size());
	}

}