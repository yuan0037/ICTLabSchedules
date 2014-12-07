package util;


import java.util.List;

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
		convertView=inflater.inflate(R.layout.lab_view, null);
		holder = new ViewHolder();
		holder.labTitle=convertView.findViewById(R.id.labNameTextView);
		holder.labScheduleQty=convertView.findViewById(R.id.labScheduleQtyTextView);
		
	}
	convertView.setBackgroundColor(0x30FF0000);
	return super.getView(position, convertView, parent);
}
}
