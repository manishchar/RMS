package alina.com.rms.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import alina.com.rms.R;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int flags[];
    List<String> countryNames;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, /*int[] flags,*/ List<String> countryNames) {
        this.context = applicationContext;
      //  this.flags = flags;
        this.countryNames = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryNames.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        //ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        //icon.setImageResource(flags[i]);
        names.setText(countryNames.get(i));
        return view;
    }
}