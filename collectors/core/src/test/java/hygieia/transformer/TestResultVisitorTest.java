package hygieia.transformer;

import com.apexon.compass.dashboard.model.TestResult;
import com.apexon.compass.dashboard.model.quality.CucumberJsonReport;
import com.apexon.compass.dashboard.model.quality.MochaJsSpecReport;
import com.apexon.compass.dashboard.request.BuildDataCreateRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Created by stevegal on 2019-03-25.
 */
class TestResultVisitorTest {

	private BuildDataCreateRequest mockBuidDataCreateRequest;

	private TestResultVisitor sut;

	private CucumberJsonReport cucumberTestReport;

	private MochaJsSpecReport mochaTestReport;

	@BeforeEach
	public void setup() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		cucumberTestReport = mapper.readValue(this.getClass().getResource("/cucumber.json"), CucumberJsonReport.class);
		mochaTestReport = mapper.readValue(this.getClass().getResource("/mochjsspec.json"), MochaJsSpecReport.class);
		mockBuidDataCreateRequest = mock(BuildDataCreateRequest.class);
		sut = new TestResultVisitor("functional", mockBuidDataCreateRequest);
	}

	@Test
	void convertsCucumber() {
		sut.visit(cucumberTestReport);

		TestResult report = sut.produceResult();

		assertThat(report.getTotalCount(), is(equalTo(1)));
	}

	@Test
	void convertsMocha() {
		sut.visit(mochaTestReport);

		TestResult report = sut.produceResult();

		assertThat(report.getTotalCount(), is(equalTo(1)));
	}

	@Test
	void combinesResultsFromMultiples() {

		sut.visit(cucumberTestReport);
		sut.visit(mochaTestReport);

		TestResult report = sut.produceResult();

		assertThat(report.getTotalCount(), is(equalTo(2)));

	}

}