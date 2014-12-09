package domain;

import java.util.ArrayList;
import java.util.Date;

import android.graphics.Color;



public class LabSchedule {

	private String labName;
	private String room;
	private Date scheduleStartTime;
	private Integer scheduleStartHour;
	private Integer scheduleEndHour;
	private Date scheduleEndTime;
	private Integer scheduleColor;
	private Integer scheduleDayOfWeek;

	
	public Integer getLabScheduleColorFromName()
	{
		
		if (labName.equals("MAD9111"))
		return Integer.valueOf(Color.rgb(255, 0, 0));
		else if (labName.equals("MAD9014"))
			return Integer.valueOf(Color.rgb(255, 255, 0));
		else if (labName.equals("MAD8010"))
			return Integer.valueOf(Color.rgb(0, 255, 255));
		else if (labName.equals("MAD9132"))
			return Integer.valueOf(Color.rgb(0, 255, 0));
		else if (labName.equals("OPEN"))
			return Integer.valueOf(Color.rgb(0, 0, 255));
		else if (labName.equals("MAD9010"))
			return Integer.valueOf(Color.rgb(0, 122, 122));	
		else if (labName.equals("MAD9013"))
			return Integer.valueOf(Color.rgb(111, 111, 111));
		else if (labName.equals("MAD9133"))
			return Integer.valueOf(Color.rgb(155, 155, 155));
		else if (labName.equals("MAD9034"))
			return Integer.valueOf(Color.parseColor("#CCCCCC"));
		else return 0;
		
	}
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
		this.scheduleColor=getLabScheduleColorFromName();
	}
	public Date getScheduleStartTime() {
		return scheduleStartTime;
	}
	public void setScheduleStartTime(Date scheduleStartTime) {
		this.scheduleStartTime = scheduleStartTime;
	}
	public Date getScheduleEndTime() {
		return scheduleEndTime;
	}
	public void setScheduleEndTime(Date scheduleEndTime) {
		this.scheduleEndTime = scheduleEndTime;
	}
	public Integer getScheduleColor() {
		return scheduleColor;
	}
	public void setScheduleColor(Integer scheduleColor) {
		this.scheduleColor = scheduleColor;
	}
	public Integer getScheduleDayOfWeek() {
		return scheduleDayOfWeek;
	}
	public void setScheduleDayOfWeek(Integer scheduleDayOfWeek) {
		this.scheduleDayOfWeek = scheduleDayOfWeek;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public Integer getScheduleStartHour() {
		return scheduleStartHour;
	}
	public void setScheduleStartHour(Integer scheduleStartHour) {
		this.scheduleStartHour = scheduleStartHour;
	}
	public Integer getScheduleEndHour() {
		return scheduleEndHour;
	}
	public void setScheduleEndHour(Integer scheduleEndHour) {
		this.scheduleEndHour = scheduleEndHour;
	}
}
