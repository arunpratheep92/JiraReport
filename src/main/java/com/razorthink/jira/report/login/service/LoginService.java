package com.razorthink.jira.report.login.service;

import java.util.Map;
import com.atlassian.jira.rest.client.api.JiraRestClient;

public interface LoginService {

	JiraRestClient authorize( Map<String, String> params );

	JiraRestClient getRestClient();

}