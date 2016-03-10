package com.razorthink.jira.report.userReport.service;

import java.util.Map;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.razorthink.jira.report.domain.AggregateUserReport;

public interface UserReportService {

	AggregateUserReport getUserReport( Map<String, String> params, JiraRestClient restClient );

}