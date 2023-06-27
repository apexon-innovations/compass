package com.apexon.compass.dashboard.model;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardWidgetTest {

	@Test
	void testStandardwidget() {
		StandardWidget sw = new StandardWidget(CollectorType.Build, ObjectId.get());
		assertTrue(sw.getName().equalsIgnoreCase("build"));
		assertEquals("build0", sw.getOptions().get("id"));
		assertTrue(sw.getWidget().getName().equalsIgnoreCase("build"));
		assertEquals("build0", sw.getWidget().getOptions().get("id"));

		sw = new StandardWidget(CollectorType.SCM, ObjectId.get());
		assertTrue(sw.getName().equalsIgnoreCase("repo"));
		assertEquals("repo0", sw.getOptions().get("id"));
		assertTrue(sw.getWidget().getName().equalsIgnoreCase("repo"));
		assertEquals("repo0", sw.getWidget().getOptions().get("id"));

		sw = new StandardWidget(CollectorType.CodeQuality, ObjectId.get());
		assertTrue(sw.getName().equalsIgnoreCase("codeanalysis"));
		assertEquals("codeanalysis0", sw.getOptions().get("id"));
		assertTrue(sw.getWidget().getName().equalsIgnoreCase("codeanalysis"));
		assertEquals("codeanalysis0", sw.getWidget().getOptions().get("id"));

		sw = new StandardWidget(CollectorType.Test, ObjectId.get());
		assertTrue(sw.getName().equalsIgnoreCase("codeanalysis"));
		assertEquals("codeanalysis0", sw.getOptions().get("id"));
		assertTrue(sw.getWidget().getName().equalsIgnoreCase("codeanalysis"));
		assertEquals("codeanalysis0", sw.getWidget().getOptions().get("id"));

		sw = new StandardWidget(CollectorType.StaticSecurityScan, ObjectId.get());
		assertTrue(sw.getName().equalsIgnoreCase("codeanalysis"));
		assertEquals("codeanalysis0", sw.getOptions().get("id"));
		assertTrue(sw.getWidget().getName().equalsIgnoreCase("codeanalysis"));
		assertEquals("codeanalysis0", sw.getWidget().getOptions().get("id"));

		sw = new StandardWidget(CollectorType.LibraryPolicy, ObjectId.get());
		assertTrue(sw.getName().equalsIgnoreCase("codeanalysis"));
		assertEquals("codeanalysis0", sw.getOptions().get("id"));
		assertTrue(sw.getWidget().getName().equalsIgnoreCase("codeanalysis"));
		assertEquals("codeanalysis0", sw.getWidget().getOptions().get("id"));

		sw = new StandardWidget(CollectorType.Deployment, ObjectId.get());
		assertTrue(sw.getName().equalsIgnoreCase("deploy"));
		assertEquals("deploy0", sw.getOptions().get("id"));
		assertTrue(sw.getWidget().getName().equalsIgnoreCase("deploy"));
		assertEquals("deploy0", sw.getWidget().getOptions().get("id"));

		sw = new StandardWidget(CollectorType.AgileTool, ObjectId.get());
		assertTrue(sw.getName().equalsIgnoreCase("feature"));
		assertEquals("feature0", sw.getOptions().get("id"));
		assertTrue(sw.getWidget().getName().equalsIgnoreCase("feature"));
		assertEquals("feature0", sw.getWidget().getOptions().get("id"));
	}

}