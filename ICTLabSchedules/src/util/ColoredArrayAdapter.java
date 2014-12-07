package util;


import java.util.List;

import com.algonquincollege.yuan0037.ictlabschedules.R;

import domain.Lab;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class ColoredArrayAdapter extends ArrayAdapter<Lab> {
	Context context;
	public ColoredArrayAdapter(Context context, int resource,
			List<Lab> objects) {
		super(context, resource, objects);
		this.context=context;
	}
	public ColoredArrayAdapter(Context context, int resource){
		super(context, resource);
		this.context=context;
	}
	
	/*private view holder class*/
	private class ViewHolder {
		TextView labTitle;
		TextView labScheduleQty;
	}
@Override
public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	ViewHolder holder=null;
	Lab lab=getItem(position);
	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	if (convertView==null)
	{
		convertView=inflater.inflate(R.layout.lab_view, parent, false);
		holder = new ViewHolder();
		holder.labTitle=(TextView) convertView.findViewById(R.id.labNameTextView);
		holder.labScheduleQty=(TextView) convertView.findViewById(R.id.labScheduleQtyTextView);
		holder.labTitle.setText(lab.getRoom());
		holder.labScheduleQty.setText("");
		
	}
	 else 
			holder = (ViewHolder) convertView.getTag();
	if ((position%2)==0)
		convertView.setBackgroundColor(0x30FF0000);
	else
		convertView.setBackgroundColor(0x300000FF);
	return convertView;
}
}
