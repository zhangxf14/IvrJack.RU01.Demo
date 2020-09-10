package com.ivrjack.ru01.demo;

import java.util.ArrayList;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 超限列表适配器
 * @author Sai
 *
 */
public class ExceededAdpt extends BaseAdapter{
	private ArrayList<TagTemperature> mDatas;
	public ExceededAdpt(ArrayList<TagTemperature> datas){
		this.mDatas=datas;
	}
	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TagTemperature data=mDatas.get(position);
		Holder holder=null; 
		View view =convertView;
		if(view==null){
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view=inflater.inflate(R.layout.adpt_item_exceeded, null);
			holder=creatHolder(view);
			view.setTag(holder);
		}
		else{
			holder=(Holder) view.getTag();
		}
		holder.UpdateUI(data,position);
		return view;
	}
	public Holder creatHolder(View view){
		Holder holder=new Holder();
		holder.tvId=(TextView) view.findViewById(R.id.tvNo);
		holder.tvDate=(TextView) view.findViewById(R.id.tvDate);
		holder.tvTemperature=(TextView) view.findViewById(R.id.tvTemperature);
		return holder;
	}
	class Holder {
		private TextView tvId;
		private TextView tvDate;
		private TextView tvTemperature;
		public void UpdateUI(TagTemperature data,int position){
			tvId.setText(String.valueOf(getCount()-position));
			tvDate.setText(TimeUtil.getTimeWithoutYear(data.getDate()));
			String temperature=DataUtil.formatTemperature(data.getTemperature());
			tvTemperature.setText(String.valueOf(temperature));
			if(data.getExceededStatus()!=Constants.STATUS_NORMAL){
				tvId.setTextColor(Color.RED);
				tvDate.setTextColor(Color.RED);
				tvTemperature.setTextColor(Color.RED);
			}
			else{
				tvId.setTextColor(Color.BLACK);
				tvDate.setTextColor(Color.BLACK);
				tvTemperature.setTextColor(Color.BLACK);
			}
		}
	}
}