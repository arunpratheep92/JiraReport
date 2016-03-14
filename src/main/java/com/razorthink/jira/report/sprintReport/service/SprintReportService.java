package com.razorthink.jira.report.sprintReport.service;

import java.util.HashMap;
import java.util.Map;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.razorthink.jira.report.domain.AggregateUserReport;

public interface SprintReportService {

	HashMap<String, AggregateUserReport> getSprintReport( Map<String, String> params, JiraRestClient restClient );

}