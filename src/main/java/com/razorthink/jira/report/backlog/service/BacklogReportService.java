package com.razorthink.jira.report.backlog.service;

import java.util.List;
import java.util.Map;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.razorthink.jira.report.domain.UserReport;

public interface BacklogReportService {

	List<UserReport> getBacklogReport( Map<String, String> params, JiraRestClient restClient );

}