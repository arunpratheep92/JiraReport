package com.razorthink.jira.report.rest;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.razorthink.jira.report.domain.AggregateUserReport;
import com.razorthink.jira.report.exception.DataException;
import com.razorthink.jira.report.login.service.LoginService;
import com.razorthink.jira.report.userReport.service.UserReportService;
import com.razorthink.jira.report.utils.Response;

@RestController
@RequestMapping( "/userReport" )
public class UserReportRestService {

	@Autowired
	UserReportService userReporService;

	@Autowired

	LoginService loginService;

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	@RequestMapping( value = "/getUserReport", method = RequestMethod.POST )
	public ResponseEntity<Response> getUserReport( @RequestBody Map<String, String> params )
	{
		Response response = new Response();
		try
		{
			JiraRestClient restClient = loginService.getRestClient();
			AggregateUserReport report = userReporService.getUserReport(params, restClient);
			response.setErrorCode(null);
			response.setErrorMessage(null);
			response.setObject(report);
			return new ResponseEntity(response, HttpStatus.OK);
		}
		catch( DataException e )
		{
			response.setErrorCode(HttpStatus.UNAUTHORIZED.name());
			response.setErrorMessage(e.getMessage());
			response.setObject(null);
			return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
		}
	}

}
