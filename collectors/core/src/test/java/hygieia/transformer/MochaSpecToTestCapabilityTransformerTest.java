package hygieia.transformer;

import com.apexon.compass.dashboard.model.TestCapability;
import com.apexon.compass.dashboard.model.TestSuiteType;
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
import static org.mockito.Mockito.when;

/**
 * Created by stevegal on 2019-03-25.
 */
class MochaSpecToTestCapabilityTransformerTest {

	ObjectMapper mapper;

	MochaSpecToTestCapabilityTransformer sut;

	private BuildDataCreateRequest mockBuildDataRequest;

	@BeforeEach
	public void setup() {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		mockBuildDataRequest = mock(BuildDataCreateRequest.class);

		sut = new MochaSpecToTestCapabilityTransformer(mockBuildDataRequest, "testDescription");
	}

	@Test
    void producesCucumberTestResult() throws Exception {

        when(mockBuildDataRequest.getNumber()).thenReturn("aBuildNumber");

        MochaJsSpecReport testReport = this.mapper.readValue(this.getClass().getResource("/mochjsspec.json"), MochaJsSpecReport.class);

        TestCapability capability = sut.convert(testReport);

        // 2 test suites
        assertThat(capability.getExecutionId(), is(equalTo("aBuildNumber")));
        assertThat(capability.getDescription(), is(equalTo("testDescription")));
        assertThat(capability.getType(), is(TestSuiteType.Functional));
        assertThat(capability.getSuccessTestSuiteCount(), is(equalTo(1)));
        assertThat(capability.getFailedTestSuiteCount(), is(equalTo(2)));
        assertThat(capability.getDuration(), is(equalTo(12L)));
        assertThat(capability.getTestSuites().size(), is(equalTo(3)));

    }

}