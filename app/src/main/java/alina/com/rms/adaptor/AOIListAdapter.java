package alina.com.rms.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import alina.com.rms.R;


/**
 * Created by cnet on 1/25/2016.
 */
public class AOIListAdapter extends BaseAdapter{

    Context context;
    List<String> keyValues,pairValues;
    Set<String> ids;
    List<Boolean> isChecked1;

    public AOIListAdapter(Context context, List<String> keyValues,List<String> pairValues ,List<Boolean>isChecked) {
        this.context = context;
        this.keyValues = keyValues;
        this.pairValues=pairValues;
        this.isChecked1 = isChecked;
       // this.ids=ids;
    }

    @Override
    public int getCount() {
        return keyValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        {
            // TODO Auto-generated method stub

            ViewHolder viewHolder = null;
   /*         if (convertView == null) {*/
                LayoutInflater layoutInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.multipal_feeder_adp, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_feedername = (TextView) convertView.findViewById(R.id.tv_feeder_name);
                viewHolder.btn_check = (CheckBox) convertView.findViewById(R.id.check_btn);
                convertView.setTag(viewHolder);
         /*   } /*else {*//*
                viewHolder = (ViewHolder) convertView.getTag();
            *//*}*/
            if(isChecked1.get(position))
            {
                viewHolder.btn_check.setChecked(true);
            }
            viewHolder.tv_feedername.setText(pairValues.get(position));
          //  viewHolder.btn_check.setChecked(isChecked);
            viewHolder.btn_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    /*if(isChecked)
                    {
                        isChecked1.set(position,true);
                    }else{
                    isChecked1.set(position,false);
                    notifyDataSetChanged();
                    }*/

                    if(isChecked)
                    {
                        if(position==0)
                        {
                            isChecked1.clear();
                            for (int i=0;i<keyValues.size();i++)
                            {
                                isChecked1.add(true);
                            }

                        }
                        else {
                            isChecked1.set(position,true);
                        }
                    }
                    else {

                        if(position==0)
                        {
                            isChecked1.clear();
                            for (int i=0;i<keyValues.size();i++)
                            {
                                isChecked1.add(false);
                            }
                            notifyDataSetChanged();
                        }
                        else {
                            if(isChecked1.get(0))
                            {
                                isChecked1.set(0,false);
                            }
                            isChecked1.set(position,false);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            /*if(ids.contains(areaOfIntrestResultLists.get(position).getCategory_id())){
                viewHolder.btn_check.setChecked(true);
            }else{
                viewHolder.btn_check.setChecked(false);
            }*/

            return convertView;
        }
    }

    class ViewHolder {

        TextView tv_feedername;
        CheckBox btn_check;

    }
}