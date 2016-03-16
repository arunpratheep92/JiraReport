package com.razorthink.jira.report.timesheet.service;

import java.util.List;
import java.util.Map;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.razorthink.jira.report.domain.TimesheetReport;

public interface TimesheetReportService {

	List<TimesheetReport> getTimesheetReport( Map<String, String> params, JiraRestClient restClient );

}