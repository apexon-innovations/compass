
package hygieia.transformer;

import com.apexon.compass.dashboard.model.TestCapability;
import com.apexon.compass.dashboard.model.TestSuite;
import com.apexon.compass.dashboard.model.quality.JunitXmlReport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class JunitXmlToTestCapabilityTransformerTest {

	JunitXmlToTestCapabilityTransformer sut = new JunitXmlToTestCapabilityTransformer();

	private static JAXBContext jaxbContext;

	static {
		try {
			jaxbContext = JAXBContext.newInstance(JunitXmlReport.class);
		}
		catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Test
	void producesJunitTestResult() throws Exception {

		URL url = getClass().getResource("/junit.xml");
		File file = new File(url.getPath());
		FileReader fileReader = new FileReader(file);
		JunitXmlReport responseReport = null;

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		responseReport = (JunitXmlReport) unmarshaller.unmarshal(fileReader);

		TestCapability capability = sut.convert(responseReport);

		assertThat(capability.getSuccessTestSuiteCount()).isEqualTo(1);
		assertThat(capability.getFailedTestSuiteCount()).isZero();
		assertThat(capability.getTestSuites()).hasSize(1);
		TestSuite suite = capability.getTestSuites().iterator().next();
		assertThat(suite.getTestCases()).hasSize(4);
	}

}
