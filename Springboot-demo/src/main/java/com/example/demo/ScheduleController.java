package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScheduleController {
	
	@Autowired
	private ScheduleService service;
	
	@GetMapping("/schedule")
	public ResponseEntity<List<ScheduleInfo>> getAllSchedule()
	{
		List<ScheduleInfo> scheduleList = service.getAllSchedule();
		return new ResponseEntity<List<ScheduleInfo>>(scheduleList, new HttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping("addSchedule")
	public ResponseEntity<String> insert(@RequestBody(required = true) ScheduleInfo schedule)
	{
		String message = service.insertSchedule(schedule);
		return  new ResponseEntity<String>(message, new HttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping("updateSchedule")
	public ResponseEntity<String> update(@RequestBody(required = true) ScheduleInfo schedule)
	{
		String message = service.updateSchedule(schedule);
		return new ResponseEntity<String>(message, new HttpHeaders(),HttpStatus.OK );
	}
	
	@PostMapping("updateListOfSchedules")
	public ResponseEntity<String> updateListofSchedule(@RequestBody(required=true) List<ScheduleInfo> infoList)
	{
		String  msg = service.updateListofSchedule(infoList);
		return new ResponseEntity<String>(msg,new HttpHeaders(),HttpStatus.OK);
	}
	
	@RequestMapping(value = "employeeschedule/{empId}", method = RequestMethod.GET)
	public ResponseEntity<List<ScheduleInfo>> findByempId(@PathVariable Integer empId)
	{
		List<ScheduleInfo> info = service.findByempId(empId);
		return new ResponseEntity<List<ScheduleInfo>>(info ,new HttpHeaders(), HttpStatus.OK);
	}
	@GetMapping("cancelSchedule/{empId}")
	public ResponseEntity<String> deleteByempId(@PathVariable int empId)
	{
		String msg = service.cancelSchedule(empId);
		return new ResponseEntity<String>(msg ,new HttpHeaders(), HttpStatus.OK);
	}
	
	@PostMapping("cancelSchedule/{empId}")
	public ResponseEntity<String> cancelSceduleByDate(@Param(value = "startDate") String startDate, @Param(value="endDate") String endDate, @PathVariable int empId)
	{
		String msg = service.deleteByDate(startDate, endDate, empId);
		return new ResponseEntity<String>(msg, new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("SchedulebyDate")
	public ResponseEntity<List<ScheduleInfo>> selectSchedulesByDate(@Param(value = "date") String date)
	{
		List<ScheduleInfo> infoList = new ArrayList<>();
		if(date != null)
		{
		infoList = service.getScheduleByDate(date);
		}
		return new ResponseEntity<List<ScheduleInfo>>(infoList, new HttpHeaders(), HttpStatus.OK );
	}

}
