package com.apexon.compass.dashboard.bitbucketapi;

import com.apexon.compass.dashboard.collector.GitSettings;
import com.apexon.compass.dashboard.model.ScmConfiguration;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class BitbucketApiUrlBuilderTest {

	public static final String HTTPS_HTTPS_SOMERANDOMURL_COM_XYZ_RSA = "https://somerandomurl.com/XYZ/rsa";

	@Mock
	private GitSettings settings;

	@InjectMocks
	private BitbucketApiUrlBuilder testObject;

	@Mock
	ScmConfiguration scmConfiguration;

	@Test
	public void buildReposApiUrl() throws URISyntaxException {
		given(settings.getApi()).willReturn("/rest/api/1.0/");
		given(settings.getProduct()).willReturn("server");
		URI actual = testObject.buildReposApiUrl(HTTPS_HTTPS_SOMERANDOMURL_COM_XYZ_RSA);
		assertNotNull(actual);
		// URI expected = new
		// URIBuilder("https://somerandomurl.com/rest/api/1.0/projects/XYZ/repos/rsa").build();
		URI expected = new URIBuilder("https://somerandomurl.com/projects/XYZ/repos/rsa").build();
		assertEquals(expected, actual);
	}

	@Test
	public void buildPullRequestApiUrl() throws URISyntaxException {
		given(settings.getApi()).willReturn("/rest/api/1.0/");
		given(settings.getProduct()).willReturn("server");
		URI actual = testObject.buildPullRequestApiUrl(HTTPS_HTTPS_SOMERANDOMURL_COM_XYZ_RSA);
		assertNotNull(actual);
		// URI expected = new
		// URIBuilder("https://somerandomurl.com/rest/api/1.0/projects/XYZ/repos/rsa/pull-requests").build();
		URI expected = new URIBuilder("https://somerandomurl.com/projects/XYZ/repos/rsa/pull-requests").build();
		assertEquals(expected, actual);
	}

	@Test
	public void buildPullRequestActivitiesApiUrl() throws URISyntaxException {
		given(settings.getApi()).willReturn("/rest/api/1.0/");
		given(settings.getProduct()).willReturn("server");
		URI actual = testObject.buildPullRequestActivitiesApiUrl(HTTPS_HTTPS_SOMERANDOMURL_COM_XYZ_RSA, "1111");
		assertNotNull(actual);
		URI expected = new URIBuilder("https://somerandomurl.com/projects/XYZ/repos/rsa/pull-requests/1111/activities")
			.build();
		// URI expected = new
		// URIBuilder("https://somerandomurl.com/rest/api/1.0/projects/XYZ/repos/rsa/pull-requests/1111/activities").build();
		System.out.println(actual);
		assertEquals(expected, actual);
	}

	@Test
	public void buildReposApiUrlforCloud() throws URISyntaxException {
		given(settings.getApi()).willReturn("/api/2.0/repositories/");
		given(settings.getProduct()).willReturn("cloud");
		URI actual = testObject.buildReposApiUrl(HTTPS_HTTPS_SOMERANDOMURL_COM_XYZ_RSA);
		assertNotNull(actual);
		// URI expected = new
		// URIBuilder("https://somerandomurl.com/api/2.0/repositories/XYZ/rsa").build();
		URI expected = new URIBuilder("https://somerandomurl.com/projects/XYZ/repos/rsa").build();
		assertEquals(expected, actual);
	}

	@Test
	public void buildPullRequestApiUrlforCloud() throws URISyntaxException {
		given(settings.getApi()).willReturn("/api/2.0/repositories/");
		given(settings.getProduct()).willReturn("cloud");
		URI actual = testObject.buildPullRequestApiUrl(HTTPS_HTTPS_SOMERANDOMURL_COM_XYZ_RSA);
		assertNotNull(actual);
		URI expected = new URIBuilder("https://somerandomurl.com/projects/XYZ/repos/rsa/pull-requests").build();
		// URI expected = new
		// URIBuilder("https://somerandomurl.com/api/2.0/repositories/XYZ/rsa/pullrequests").build();
		assertEquals(expected, actual);
	}

	@Test
	public void buildPullRequestActivitiesApiUrlforCloud() throws URISyntaxException {
		given(settings.getApi()).willReturn("/api/2.0/repositories/");
		given(settings.getProduct()).willReturn("cloud");
		URI actual = testObject.buildPullRequestActivitiesApiUrl(HTTPS_HTTPS_SOMERANDOMURL_COM_XYZ_RSA, "1111");
		assertNotNull(actual);
		URI expected = new URIBuilder("https://somerandomurl.com/projects/XYZ/repos/rsa/pull-requests/1111/activities")
			.build();
		// URI expected = new
		// URIBuilder("https://somerandomurl.com/api/2.0/repositories/XYZ/rsa/pullrequests/1111/activity").build();
		assertEquals(expected, actual);
	}

}
