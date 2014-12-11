package domain;

import java.util.Date;
import android.graphics.Color;

/**
 * Object for each Schedule.
 *
 * @author yuan0037@algonquinlive.com
 * @Version 1.0
 */

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

		else if (labName.equals("CST8182"))
			return Integer.valueOf(Color.parseColor("#1485CC"));
		else if (labName.equals("NET2000"))
			return Integer.valueOf(Color.parseColor("#0BD9BA"));
		else if (labName.equals("NET3900"))
			return Integer.valueOf(Color.parseColor("#0BC8E3"));
		else if (labName.equals("CST8304"))
			return Integer.valueOf(Color.parseColor("#0B1DD9"));
		else if (labName.equals("NET3008"))
			return Integer.valueOf(Color.parseColor("#CC7F14"));
		else if (labName.equals("CST8278"))
			return Integer.valueOf(Color.parseColor("#7DCC14"));
		else if (labName.equals("NET3009"))
			return Integer.valueOf(Color.parseColor("#18677F"));
		else if (labName.equals("MAD9034"))
			return Integer.valueOf(Color.parseColor("#18677F"));
		else if (labName.equals("CST8340"))
			return Integer.valueOf(Color.parseColor("#18677F"));
		else if (labName.equals("CST8270"))
			return Integer.valueOf(Color.parseColor("#18677F"));
		else if (labName.equals("CST8271"))
			return Integer.valueOf(Color.parseColor("#AD14CC"));
		else if (labName.equals("NET1002"))
			return Integer.valueOf(Color.parseColor("#14CC78"));
		else if (labName.equals("NET1020"))
			return Integer.valueOf(Color.parseColor("#CC5F4E"));
		else if (labName.equals("CST8217"))
			return Integer.valueOf(Color.parseColor("#7F0648"));
		else if (labName.equals("NET3007"))
			return Integer.valueOf(Color.parseColor("#CCC489"));
		else if (labName.equals("NEW LAB"))
			return Integer.valueOf(Color.parseColor("#3848E3"));
		else if (labName.equals("OFFICE HOUR"))
			return Integer.valueOf(Color.parseColor("#35D967"));
		else if (labName.equals("ICT Coords"))
			return Integer.valueOf(Color.parseColor("#D95E35"));
		else if (labName.equals("MAD&D Coords"))
			return Integer.valueOf(Color.parseColor("#35D96E"));
		else if (labName.equals("WEEKEND!"))
			return Integer.valueOf(Color.parseColor("#5AD92C"));										
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
