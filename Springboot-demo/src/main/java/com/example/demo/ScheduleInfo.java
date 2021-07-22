package src.main.java.com.example.demo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="schedule_info")
public class ScheduleInfo implements Serializable {
	
	ScheduleInfo()
	{
		
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name ="scheduleId")
	private Long scheduleId;
	@Column(name = "EmpId")
	private int empId;
	@Column(name = "startDate")
	private String startDate;
	@Column(name = "endDate")
	private String endDate;
	@Column(name = "startTime")
	private String startTime;
	@Column(name = "Duration")
	private String duration;
	@Column(name = "frequency")
	private int frequency;
	@Transient
	private boolean isSelect = true;
	@Transient
	private String freqTypeName;
	@Transient
	private boolean repeat;
	
	public ScheduleInfo(ScheduleInfo schedule) {
		 setEmpId(schedule.getEmpId());
		 setScheduleId(schedule.getScheduleId());
		 this.startDate = schedule.getStartDate();
		 this.endDate = schedule.getEndDate();
		 this.frequency = schedule.getFrequency();
		 this.startTime =schedule.getStartTime();
		 this.duration = schedule.getDuration();
	}
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
		
	public String getFreqTypeName() {
		return freqTypeName;
	}
	public void setFreqTypeName(String freqTypeName) {
		this.freqTypeName = freqTypeName;
		this.frequency = Frequency.getFrequencyType(freqTypeName);
		if(this.frequency != 0)
		{
			this.repeat = true;
		}
	}
	public boolean isRepeat() {
		return repeat;
	}
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	
	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		if(startDate!=null)
		{
		this.startDate = convertDateFormat(startDate);
		}
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		if(endDate != null)
		this.endDate = convertDateFormat(endDate);

	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		if(duration != null)
		{
		this.duration = duration;
		}
		
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
		this.freqTypeName = Frequency.getFreqenceTypeName(frequency);
		if(frequency != 0)
		{
			this.repeat = true;
		}
	}
	public String convertDateFormat(String dateStr)
	{
		String s = "";
		if(!isSelect)
		{
		Date date = new Date(dateStr);
		int year = 1900+date.getYear();
		int month = date.getMonth()+1;
		int day = date.getDate();
		String monthFormat = month>=10 ? String.valueOf(month) :"0"+month;
		String dayFormat = day>=10 ? String.valueOf(day) : "0"+day;
		s = year + "-" + monthFormat + "-" + dayFormat;
		}
		else
		{
			if(dateStr.contains("-"))
			{
				
			try {
				Date d = new SimpleDateFormat("YYYY-MM-DD").parse(dateStr);
				s = new SimpleDateFormat("dd-MMM-yyyy").format(d);
				s = s.replace("-", " ");
				}
			catch (ParseException e) 
			{
				e.printStackTrace();
				isSelect = false;
				s = convertDateFormat(dateStr);
			}	
			}
			else
			{
				isSelect = false;
				s = convertDateFormat(dateStr);
			}
				
		}		
		return s;
	}
	
	public void setStartDateDirect(String startDate)
	{
		this.startDate = startDate;
	}

	public void setEndDateDirect(String endDate)
	{
		this.endDate = endDate;
	}
}
