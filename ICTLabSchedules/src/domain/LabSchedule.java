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
//		Integer tempResult=Integer.valueOf(0);
//		switch (labName) {
//		case "MAD9111":
//			tempResult= Integer.valueOf(Color.parseColor("#FFA330"));
//			break;
//		case "MAD9014":
//			tempResult= Integer.valueOf(Color.parseColor("#870DFF"));
//			break;
//		case "MAD8010":
//			tempResult= Integer.valueOf(Color.parseColor("7F7218"));
//			break;
//		default:
//			tempResult= 0;
//		}
//		return tempResult;
		if (labName.equals("MAD9111"))
		return Integer.valueOf(Color.parseColor("#FFA330"));
		else if (labName.equals("MAD9014"))
			return Integer.valueOf(Color.parseColor("#870DFF"));
		else if (labName.equals("MAD8010"))
			return Integer.valueOf(Color.parseColor("#7F7218"));
		else if (labName.equals("MAD9132"))
			return Integer.valueOf(Color.parseColor("#21E888"));
		else if (labName.equals("OPEN"))
			return Integer.valueOf(Color.parseColor("#24FFEB"));
		else if (labName.equals("MAD9010"))
			return Integer.valueOf(Color.parseColor("#907CFF"));	
		else if (labName.equals("MAD9013"))
			return Integer.valueOf(Color.parseColor("#27CC77"));
		else if (labName.equals("MAD9133"))
			return Integer.valueOf(Color.parseColor("#31CDFF"));
		else if (labName.equals("MAD9034"))
			return Integer.valueOf(Color.parseColor("#18677F"));
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
