package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.model.*;
import com.apexon.compass.dashboard.repository.*;
import com.apexon.compass.dashboard.util.AsyncUtil;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SonarCollectorTaskTest {

	@InjectMocks
	private SonarCollectorTask task;

	@Mock
	private SonarCollectorRepository sonarCollectorRepository;

	@Mock
	private SonarProjectRepository sonarProjectRepository;

	@Mock
	private CodeQualityRepository codeQualityRepository;

	@Mock
	private SonarProfileRepostory sonarProfileRepostory;

	@Mock
	private SonarSettings sonarSettings;

	@Mock
	private ComponentRepository dbComponentRepository;

	@Mock
	private SonarClientSelector sonarClientSelector;

	@Mock
	private DefaultSonarClient defaultSonarClient;

	@Mock
	private DefaultSonar6Client defaultSonar6Client;

	@Mock
	private TaskScheduler taskScheduler;

	@Mock
	private Environment environment;

	@Mock
	private DesSonarConfigurations desSonarConfiguration;

	private static final String SERVER1 = "server1";

	private static final String SERVER2 = "server2";

	private static final String METRICS1 = "nloc";

	private static final String METRICS2 = "nloc,violations";

	private static final Double VERSION43 = 4.3;

	private static final Double VERSION54 = 5.4;

	private static final Double VERSION63 = 6.3;

	private static final String NICENAME1 = "niceName1";

	private static final String NICENAME2 = "niceName2";

	private static final String QUALITYPROFILE = "cs-default-donotmodify-89073";

	private JSONArray qualityProfiles = new JSONArray();

	private JSONArray profileConfigurationChanges = new JSONArray();

	private JSONObject qualityProfile = new JSONObject();

	private JSONObject profileConfigurationChange = new JSONObject();

	ConfigHistOperationType operation = ConfigHistOperationType.CHANGED;

	@Mock
	AsyncUtil asyncUtil;

	@Before
	public void setup() throws ParseException {
		qualityProfile.put("key", QUALITYPROFILE);
		qualityProfile.put("name", "Default-DoNotModify");
		qualityProfiles.add(qualityProfile);

		profileConfigurationChange.put("authorName", "foo");
		profileConfigurationChange.put("authorLogin", "bar");
		profileConfigurationChange.put("date", "2017-10-05T13:57:40+0000");
		profileConfigurationChange.put("action", "DEACTIVATED");
		profileConfigurationChanges.add(profileConfigurationChange);

		Mockito.doReturn(qualityProfiles).when(defaultSonarClient).getQualityProfiles(SERVER1);

		Mockito.doReturn(profileConfigurationChanges)
			.when(defaultSonarClient)
			.getQualityProfileConfigurationChanges(SERVER1, QUALITYPROFILE);

		Mockito.doReturn(qualityProfiles).when(defaultSonar6Client).getQualityProfiles(SERVER1);
		Mockito.doReturn(qualityProfiles).when(defaultSonar6Client).getQualityProfiles(SERVER2);

		Mockito.doReturn(profileConfigurationChanges)
			.when(defaultSonar6Client)
			.getQualityProfileConfigurationChanges(SERVER1, QUALITYPROFILE);
		Mockito.doReturn(profileConfigurationChanges)
			.when(defaultSonar6Client)
			.getQualityProfileConfigurationChanges(SERVER2, QUALITYPROFILE);

		SonarConfiguration sonarConfiguration1 = new SonarConfiguration();
		sonarConfiguration1.setIscProjectId(new ObjectId());
		sonarConfiguration1.setVersion("7.2");
		sonarConfiguration1.setUrl(SERVER1);
		sonarConfiguration1.setUserName("bob");
		sonarConfiguration1.setCredentials("cSfwaiyUVzI=");
		sonarConfiguration1.setEncryptionKey("12345678abcdefgh12345678abcdefgh");

		SonarConfiguration sonarConfiguration2 = new SonarConfiguration();
		sonarConfiguration2.setIscProjectId(new ObjectId());
		sonarConfiguration2.setVersion("7.2");
		sonarConfiguration2.setUrl(SERVER2);
		sonarConfiguration2.setUserName("bob");
		sonarConfiguration2.setCredentials("cSfwaiyUVzI=");
		sonarConfiguration2.setEncryptionKey("12345678abcdefgh12345678abcdefgh");

		this.desSonarConfiguration = new DesSonarConfigurations();
		this.desSonarConfiguration.setSonarConfigurations(Arrays.asList(sonarConfiguration1, sonarConfiguration2));

		this.task = new SonarCollectorTask(taskScheduler, sonarCollectorRepository, sonarProjectRepository,
				codeQualityRepository, sonarProfileRepostory, sonarClientSelector, dbComponentRepository,
				desSonarConfiguration, environment, asyncUtil);
		doNothing().when(asyncUtil).close();
	}

	@Test
  public void collectEmpty() throws Exception {
    //    doReturn(components()).when(dbComponentRepository.findAll());
    when(dbComponentRepository.findAll()).thenReturn(components());
    task.collect(new SonarCollector());
		verifyNoInteractions(sonarClientSelector, codeQualityRepository);
  }

	@Test
  public void collectOneServer43() throws Exception {
    when(dbComponentRepository.findAll()).thenReturn(components());
    when(sonarClientSelector.getSonarVersion(SERVER1)).thenReturn(VERSION43);
    when(sonarSettings.getServers()).thenReturn(Arrays.asList(SERVER1));
    when(sonarSettings.getUsernames()).thenReturn(Arrays.asList("bob"));
    when(sonarSettings.getPasswords()).thenReturn(Arrays.asList("matrix"));
    when(sonarClientSelector.getSonarClient(VERSION43)).thenReturn(defaultSonarClient);
    task.collect(collectorWithOneServer());
    verify(sonarClientSelector).getSonarClient(VERSION43);
  }

	@Test
  public void collectOneServer54() throws Exception {
    when(dbComponentRepository.findAll()).thenReturn(components());
    when(sonarClientSelector.getSonarVersion(SERVER1)).thenReturn(VERSION54);

    when(sonarSettings.getServers()).thenReturn(Arrays.asList(SERVER1));
    when(sonarSettings.getUsernames()).thenReturn(Arrays.asList("robert"));
    when(sonarSettings.getPasswords()).thenReturn(Arrays.asList("k"));

    when(sonarClientSelector.getSonarClient(VERSION54)).thenReturn(defaultSonar6Client);
    task.collect(collectorWithOneServer());

    verify(sonarClientSelector).getSonarClient(VERSION54);
    // verify(defaultSonar6Client).getQualityProfiles(SERVER1);
    // verify(defaultSonar6Client).retrieveProfileAndProjectAssociation(SERVER1, QUALITYPROFILE);
    // verify(defaultSonar6Client).getQualityProfileConfigurationChanges(SERVER1, QUALITYPROFILE);
  }

	@Test
  public void collectOneServer63() throws Exception {
    when(dbComponentRepository.findAll()).thenReturn(components());
    when(sonarClientSelector.getSonarVersion(SERVER1)).thenReturn(VERSION63);

    when(sonarSettings.getServers()).thenReturn(Arrays.asList(SERVER1));
    when(sonarSettings.getUsernames()).thenReturn(Arrays.asList("yes"));
    when(sonarSettings.getPasswords()).thenReturn(Arrays.asList("4kkpt"));

    when(sonarClientSelector.getSonarClient(VERSION63)).thenReturn(defaultSonar6Client);
    task.collect(collectorWithOneServer());

    verify(sonarClientSelector).getSonarClient(VERSION63);
    // verify(defaultSonar6Client).getQualityProfiles(SERVER1);
    // verify(defaultSonar6Client).retrieveProfileAndProjectAssociation(SERVER1, QUALITYPROFILE);
    // verify(defaultSonar6Client).getQualityProfileConfigurationChanges(SERVER1, QUALITYPROFILE);
  }

	@Test
  public void collectTwoServer43And54() throws Exception {

    when(dbComponentRepository.findAll()).thenReturn(components());
    when(sonarClientSelector.getSonarVersion(SERVER1)).thenReturn(VERSION43);
    when(sonarClientSelector.getSonarVersion(SERVER2)).thenReturn(VERSION54);
    when(sonarSettings.getServers()).thenReturn(Arrays.asList(SERVER1, SERVER2));
    when(sonarSettings.getUsernames()).thenReturn(Arrays.asList("bob", "bob"));
    when(sonarSettings.getPasswords()).thenReturn(Arrays.asList("k", "l"));
    when(sonarClientSelector.getSonarClient(VERSION54)).thenReturn(defaultSonar6Client);
    when(sonarClientSelector.getSonarClient(VERSION43)).thenReturn(defaultSonarClient);

    task.collect(collectorWithOnTwoServers());

    verify(sonarClientSelector).getSonarClient(VERSION43);
    verify(sonarClientSelector).getSonarClient(VERSION54);

    // verify(defaultSonar6Client).getQualityProfiles(SERVER2);
    // verify(defaultSonar6Client).retrieveProfileAndProjectAssociation(SERVER2, QUALITYPROFILE);
    // verify(defaultSonar6Client).getQualityProfileConfigurationChanges(SERVER2, QUALITYPROFILE);
  }

	private List<com.apexon.compass.dashboard.model.Component> components() {
		List<com.apexon.compass.dashboard.model.Component> cArray = new ArrayList<>();
		com.apexon.compass.dashboard.model.Component c = new Component();
		c.setId(new ObjectId());
		c.setName("COMPONENT1");
		c.setOwner("JOHN");
		cArray.add(c);
		return cArray;
	}

	private SonarCollector collectorWithOneServer() {
		return SonarCollector.prototype(Collections.singletonList(SERVER1), Collections.singletonList(NICENAME1));
	}

	private SonarCollector collectorWithOnTwoServers() {
		return SonarCollector.prototype(Arrays.asList(SERVER1, SERVER2), Arrays.asList(NICENAME1, NICENAME2));
	}

}
