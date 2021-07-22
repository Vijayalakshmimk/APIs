package src.main.java.com.example.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ScheduleService {
	
	@Autowired
	ScheduleRepository schedulerepo;
	
	//Choose Monday as default Value for Weekly days, Since it is not mentioned.
	public int weeklyDay = 1;
	//Choose First Day of Month as default Value for Monthly date, Since it is not mentioned.
	public int monthlydate = 1;
	
	public List<ScheduleInfo> getAllSchedule()
	{
		List<ScheduleInfo> scheduleList = schedulerepo.findAll();
		if(scheduleList.size()>0)
		{
			//scheduleList.forEach(i -> i.setFrequency(i.getFrequency()));
			for(ScheduleInfo i : scheduleList)
			{
				i.setFrequency(i.getFrequency());
				i.setStartDate(i.getStartDate());
				i.setEndDate(i.getEndDate());
			}
			
			return scheduleList;
		}
		else {
		return new ArrayList<ScheduleInfo>();
		}
	}
	
	public String insertSchedule(ScheduleInfo schedule)
	{
		String msg = "Schedule not created";
		try 
		{
		schedulerepo.save(schedule);
		 msg ="Schedule created successfully";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return msg;
	}
	
	public String updateSchedule(ScheduleInfo schedule)
	{
		String message = "Schedule not updated";
		String msg = "";
		Optional<ScheduleInfo> scheduleInfo = schedulerepo.findById(schedule.getScheduleId());
		//Optional<ScheduleInfo> scheduleInfo = schedulerepo.findByempId(schedule.getEmpId());

		ScheduleInfo newSchedule;
		if(scheduleInfo.isPresent())
		{
			newSchedule = scheduleInfo.get();
			newSchedule.setEmpId(schedule.getEmpId());
			newSchedule.setDuration(schedule.getDuration());
			newSchedule.setScheduleId(schedule.getScheduleId());
			newSchedule.setStartDateDirect(schedule.getStartDate());
			newSchedule.setEndDateDirect(schedule.getEndDate());
			newSchedule.setFrequency(schedule.getFrequency());
			newSchedule.setStartTime(schedule.getStartTime());
			msg = "Schedule updated successfully";
		}
		else
		{
			 newSchedule = new ScheduleInfo(schedule);
			 msg = "Since schedule not existed, new schedule has been created";					 
		}
		try
		{
		 newSchedule = schedulerepo.save(newSchedule);
		 message = msg;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return message;
	}
	
	public List<ScheduleInfo> findByempId(int empId)  
	{
		List<ScheduleInfo> info = schedulerepo.findByempId(empId);
		info.forEach(i -> i.setFrequency(i.getFrequency()));
		return info;
	}
	
	public String cancelSchedule(int empId)
	{
		String msg = "Schedule not canceled";
		try {
		schedulerepo.deleteById(empId);	
		msg = "schedule has been canceled";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return msg;
	}
	
	public String updateListofSchedule(List<ScheduleInfo> infoList)
	{
		String message = "Schedules not updated";
		try 
		{
		  schedulerepo.saveAll(infoList);
		  message = "Schedules updated Successfully";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return message;
	}
	
	public String deleteByDate(String start, String end, int empId)
	{
		String message = "Schedule not has been deleted";
		
		if(empId != 0)
		{
			
			if((start != null && !start.isEmpty()) || (end != null && !end.isEmpty()))
			{
				start = convertDateFormat(start);
				end = convertDateFormat(end);
				
				if((start != null && !start.isEmpty()) && (end != null && !end.isEmpty()))
				{
					schedulerepo.deleteBetweenDates(start, end, empId);
					List<ScheduleInfo> infoList = schedulerepo.fetchScheduleBetweenDates(start, end);
					String previousDay = getPreviousDay(start);
					infoList.forEach(i -> i.setEndDateDirect(previousDay));
					schedulerepo.saveAll(infoList);
					
					infoList = schedulerepo.fetchSchedulesUpdateStartDate(start, end);
					String nextDay = getNextDay(end);
					infoList.forEach(i -> i.setStartDateDirect(nextDay));
					schedulerepo.saveAll(infoList);
					
					infoList = schedulerepo.fetchScheduleUpdateStartandEndDates(start, end);
					List<ScheduleInfo> scheduleInfoList = new ArrayList<>();
					for(int i=0;i<infoList.size();i++)
					{
						ScheduleInfo s = infoList.get(i);
						ScheduleInfo s1 = new ScheduleInfo(s);
						s.setEndDateDirect(previousDay);
						s1.setStartDateDirect(nextDay);
						scheduleInfoList.add(s);
						scheduleInfoList.add(s1);
					}
					schedulerepo.saveAll(scheduleInfoList);
					
					message = "Schedules deleted between "+ start + " and " + end + " for the employee.";
				}
				
				else if((start != null && !start.isEmpty()) && (end==null || end.isEmpty()))
				{
					schedulerepo.deleteByStartdate(start, empId);
					List<ScheduleInfo> scheduleInfoList = schedulerepo.getScheduleByDate(start);
					String previousDay = getPreviousDay(start);
					scheduleInfoList.forEach(i -> i.setEndDateDirect(previousDay));
					schedulerepo.saveAll(scheduleInfoList);
					message = "Schedules has been deleted from the Start date given " + start;
				}
				
				else if(end != null && !end.isEmpty() && (start==null || start.isEmpty()))
				{
					schedulerepo.deleteByEndDate(end, empId);
					List<ScheduleInfo> scheduleInfoList = schedulerepo.getScheduleByDate(end);
					String nextDay = getNextDay(end);
					scheduleInfoList.forEach(i -> i.setStartDateDirect(nextDay));
					schedulerepo.saveAll(scheduleInfoList);
					message = "Schedules has been deleted till the End date given" + end;
				}
								
			}
			else
			{
				message = "Start date and End date are not valid";		
			}
		}
		else
		{
			message = "Not a valid employee. No Schedule has been deleted";
		}
		
		return message;
	}
	
	private String getNextDay(String end) {
		Date date = new Date(end.replace("-", "/"));
		Date nextDay = new Date(date.getTime() + (1000*60*60*24));
		return convertDateFormat(nextDay.toLocaleString());
	}

	private String getPreviousDay(String start) 
	{
		Date date = new Date(start.replace("-","/"));
		Date yesterday = new Date(date.getTime() - (1000*60*60*24));
		return convertDateFormat(yesterday.toLocaleString());
	}

	public String convertDateFormat(String datestr)
	{
		if(datestr!=null)
		{
		Date date = new Date(datestr);
		int year = 1900+date.getYear();
		int month = date.getMonth()+1;
		int day = date.getDate();
		String monthFormat = month>=10 ? String.valueOf(month) :"0"+month;
		String s = year + "-" + monthFormat + "-" + date.getDate();
		return s;
		}
		else
		{
			return datestr;
		}
	}
	
	public List<ScheduleInfo> getScheduleByDate(String date)
	{
		List<ScheduleInfo> info = schedulerepo.getScheduleByDate(convertDateFormat(date));
		info = checkFreqMode(info, date);
		info.forEach(i -> i.setFrequency(i.getFrequency()));
		return info;		
	}

	private List<ScheduleInfo> checkFreqMode(List<ScheduleInfo> info, String date) 
	{
		Date givenDate = new Date(date);
		List<ScheduleInfo> infoList = new ArrayList<>();
		int day = givenDate.getDay();
		int dateOfMonth = givenDate.getDate();
		for(int i = 0;i<info.size();i++)
			{
				ScheduleInfo schedule = info.get(i);
				
				switch(schedule.getFrequency())
				{
				case 0:
				case 2:
					infoList.add(schedule);
					break;
					
				case 1:
					if(day != 0 && day != 6)
					{
						infoList.add(schedule);
					}
					break;
				case 3:
					if(day == weeklyDay)
					{
						infoList.add(schedule);
					}
					break;
				case 4:
					if(dateOfMonth == monthlydate)
					{
						infoList.add(schedule);
					}
					break;
				}
				
			}
		return infoList;
	}
}
