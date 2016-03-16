package com.razorthink.jira.report.sprintReport.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.razorthink.jira.report.domain.AggregateUserReport;
import com.razorthink.jira.report.domain.UserReport;
import com.razorthink.jira.report.sprintReport.service.SprintReportService;
import com.razorthink.jira.report.userReport.service.UserReportService;
import com.razorthink.jira.report.utils.ConvertToCSV;

@Service
public class SprintReportServiceImpl implements SprintReportService {

	@Autowired
	private Environment env;

	@Autowired
	UserReportService userReportService;

	private static final Logger logger = LoggerFactory.getLogger(SprintReportServiceImpl.class);

	/* (non-Javadoc)
	 * @see com.razorthink.jira.report.sprintReport.service.impl.SprintReport#getSprintReport(java.util.Map, com.atlassian.jira.rest.client.api.JiraRestClient)
	 */
	@Override
	public HashMap<String, AggregateUserReport> getSprintReport( Map<String, String> params, JiraRestClient restClient )
	{
		logger.debug("getSprintReport");
		String sprint = params.get("sprint");
		String project = params.get("project");
		String assignee = null;
		AggregateUserReport aggregateUserReport = new AggregateUserReport();
		List<UserReport> issueList = new ArrayList<>();
		HashMap<String, AggregateUserReport> sprintReport = new HashMap<>();
		Iterable<Issue> retrievedIssue = restClient.getSearchClient()
				.searchJql(" sprint = '" + sprint + "' AND project = '" + project + "'").claim().getIssues();
		for( Issue issue : retrievedIssue )
		{
			if( issue.getAssignee() != null )
			{
				assignee = issue.getAssignee().getName();
				if( sprintReport.get(assignee) == null )
				{
					HashMap<String, String> userParams = new HashMap<>();
					userParams.put("sprint", sprint);
					userParams.put("project", project);
					userParams.put("user", assignee);
					userParams.put("export", "false");
					aggregateUserReport = userReportService.getUserReport(userParams, restClient);
					sprintReport.put(assignee, aggregateUserReport);
					issueList.addAll(aggregateUserReport.getIssues());
				}
			}
		}
		ConvertToCSV exportToCSV = new ConvertToCSV();
		exportToCSV.exportToCSV(env.getProperty("csv.filename"+project+"_"+sprint+".csv"), issueList);
		/*for( Issue issue : retrievedIssue )
		{
			if( issue.getAssignee() != null )
			{
				assignee = issue.getAssignee().getName();
			}
			if( sprintReport.get(assignee) == null )
			{
				aggregateUserReport.setTotalTasks(totalTasks);
				sprintReport.put(assignee, aggregateUserReport);
			}
			else
			{
				if( sprintReport.get(assignee).getIssues() != null )
				{
					issueList = sprintReport.get(assignee).getIssues();
				}
				UserReport userReport = new UserReport();
				userReport.setKey(issue.getKey());
				userReport.setStatus(issue.getStatus().getName());
				userReport.setIssueType(issue.getIssueType().getName());
				userReport.setProject(issue.getProject().getName());
				userReport.setSummary(issue.getSummary());
				userReport.setReporter(issue.getReporter().getName());
				userReport.setReporterDiplayName(issue.getReporter().getDisplayName());
				if( issue.getAssignee() != null )
				{
					userReport.setAssignee(assignee);
					userReport.setAssigneeDiplayName(issue.getAssignee().getDisplayName());
				}
				else
				{
					userReport.setAssignee("Unassigned");
				}
				TemporalAccessor temporal = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
						.parse(issue.getCreationDate().toString());
				String createDate = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss").format(temporal);
				userReport.setCreationDate(createDate);
				if( issue.getUpdateDate() != null )
				{
					temporal = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
							.parse(issue.getUpdateDate().toString());
					String updateDate = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss").format(temporal);
					userReport.setUpdateDate(updateDate);
				}
				else
				{
					userReport.setUpdateDate("null");
				}
				if( issue.getPriority() != null )
				{
					userReport.setPriority(issue.getPriority().getName());
				}
				else
				{
					userReport.setPriority("null");
				}
				if( issue.getTimeTracking() != null )
				{
					userReport.setOriginalEstimateMinutes(issue.getTimeTracking().getOriginalEstimateMinutes());
					userReport.setTimeSpentMinutes(issue.getTimeTracking().getTimeSpentMinutes());
					userReport.setRemainingEstimateMinutes(issue.getTimeTracking().getRemainingEstimateMinutes());
		
					estimatedHours = sprintReport.get(assignee).getEstimatedHours();
					estimatedHours += (issue.getTimeTracking().getOriginalEstimateMinutes() / 60);
					sprintReport.get(assignee).setEstimatedHours(estimatedHours);
		
					if( issue.getTimeTracking().getTimeSpentMinutes() != null )
					{
						actualHours = sprintReport.get(assignee).getActualHours();
						actualHours += (issue.getTimeTracking().getTimeSpentMinutes() / 60);
						sprintReport.get(assignee).setActualHours(actualHours);
					}
				}
				if( !NullEmptyUtils.isNullorEmpty((List<?>) issue.getFields()) )
				{
					if( issue.getFieldByName("Epic Link") != null
							&& issue.getFieldByName("Epic Link").getValue() != null )
					{
						userReport.setEpicLink(issue.getFieldByName("Epic Link").getValue().toString());
					}
					else
					{
						userReport.setEpicLink("null");
					}
					if( issue.getFieldByName("Sprint") != null && issue.getFieldByName("Sprint").getValue() != null )
					{
						sprint = issue.getFieldByName("Sprint").getValue().toString();
						Pattern pattern = Pattern.compile("\\[\".*\\[.*,name=(.*),startDate=(.*),.*\\]");
						Matcher matcher = pattern.matcher(issue.getFieldByName("Sprint").getValue().toString());
						if( matcher.find() )
						{
							userReport.setSprint(matcher.group(1));
						}
					}
					else
					{
						userReport.setSprint("null");
					}
				}
				totalTasks = sprintReport.get(assignee).getTotalTasks() + 1;
				sprintReport.get(assignee).setTotalTasks(totalTasks);
				issueList.add(userReport);
				Pattern patter = Pattern.compile(
						"\\[\".*\\[.*,state=(.*),name=(.*),startDate=(.*),endDate=(.*),completeDate=(.*),.*\\]");
				Matcher matcher = patter.matcher(sprint);
				if( matcher.find() )
				{
					sprintReport.get(assignee).setSprintName(matcher.group(2));
					temporal = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(matcher.group(3));
					String startdate = DateTimeFormatter.ofPattern("dd/MMM/yy,HH:mm").format(temporal);
					sprintReport.get(assignee).setSprintStartDate(startdate);
					temporal = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(matcher.group(4));
					String endDate = DateTimeFormatter.ofPattern("dd/MMM/yy,HH:mm").format(temporal);
					sprintReport.get(assignee).setSprintEndDate(endDate);
					if( matcher.group(5).equals("<null>") )
					{
						sprintReport.get(assignee).setActualsprintEndDate("Open Sprint");
					}
					else
					{
						temporal = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(matcher.group(5));
						String actualEndDate = DateTimeFormatter.ofPattern("dd/MMM/yy,HH:mm").format(temporal);
						sprintReport.get(assignee).setActualsprintEndDate(actualEndDate);
					}
				}
			}
		}*/
		return sprintReport;
	}
}
