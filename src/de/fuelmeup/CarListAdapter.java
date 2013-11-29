package de.fuelmeup;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.fuelmeup.rest.Car;

/**
 * ListAdapter to display car data in listview.
 * @author jonas
 *
 */
public class CarListAdapter extends BaseAdapter {
	private final Activity mContext;
	private ArrayList<Car> mCars;

	static class ViewHolder {
		public TextView address;
		public TextView fuel;
		public TextView name;
	}

	public CarListAdapter(Activity context, ArrayList<Car> cars) {
		super();
		this.mContext = context;
		this.mCars = cars;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*
		 * Viewholder pattern is used for better performance
		 */
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			rowView = inflater.inflate(R.layout.car_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.address = (TextView) rowView.findViewById(R.id.address);
			viewHolder.name = (TextView) rowView.findViewById(R.id.name);
			viewHolder.fuel = (TextView) rowView.findViewById(R.id.fuel);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		Car car = mCars.get(position);
		holder.address.setText(mContext.getString(R.string.address_label)+car.getmAddress());
		holder.name.setText(mContext.getString(R.string.name_label)+car.getmName());
		holder.fuel.setText(mContext.getString(R.string.fuel_label)+car.getmFuel());
		
		return rowView;
	}

	@Override
	public int getCount() {
		return mCars.size();
	}

	@Override
	public Object getItem(int pos) {
		return mCars.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return mCars.get(pos).getmName().hashCode();
	}
	
	public void clear(){
		mCars.clear();
		this.notifyDataSetChanged();
	}

	public void addItem(Car car) {
		mCars.add(car);
		this.notifyDataSetChanged();
	}
}