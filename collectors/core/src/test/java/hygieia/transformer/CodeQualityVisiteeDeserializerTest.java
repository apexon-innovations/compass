package hygieia.transformer;

import com.apexon.compass.dashboard.model.quality.CucumberJsonReport;
import com.apexon.compass.dashboard.model.quality.MochaJsSpecReport;
import com.apexon.compass.dashboard.model.quality.QualityVisitee;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Created by stevegal on 2019-03-25.
 */
class CodeQualityVisiteeDeserializerTest {

	private ObjectMapper mapper;

	@BeforeEach
	public void setup() {

		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		SimpleModule module = new SimpleModule();
		module.addDeserializer(QualityVisitee.class, new QualityVisiteeDeserializer());
		mapper.registerModule(module);
	}

	@Test
	void deserializesCucumber() throws Exception {
		QualityVisitee cucumberTestReport = mapper.readValue(this.getClass().getResource("/cucumber.json"),
				QualityVisitee.class);

		assertThat(cucumberTestReport, is(instanceOf(CucumberJsonReport.class)));
	}

	@Test
	void deserializesMocha() throws Exception {
		QualityVisitee cucumberTestReport = mapper.readValue(this.getClass().getResource("/mochjsspec.json"),
				QualityVisitee.class);

		assertThat(cucumberTestReport, is(instanceOf(MochaJsSpecReport.class)));
	}

}