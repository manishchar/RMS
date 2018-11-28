package alina.com.rms.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import alina.com.rms.R;
import alina.com.rms.activities.superAdminActivities.SuperAdminNewEntryMastErictionList;
import alina.com.rms.custom_interface.ServiceClickCallBack;
import alina.com.rms.model.GetMastDatum;


/**
 * Created by palash on 22-06-2016.
 */
public class SuperAdminMastErictionListAdapter extends RecyclerView.Adapter<SuperAdminMastErictionListAdapter.ViewHolder> {
    private ServiceClickCallBack callBackListner;
    private SuperAdminNewEntryMastErictionList mContext;
    private List<GetMastDatum> getFoundationData;
    public SuperAdminMastErictionListAdapter(Context context, ServiceClickCallBack callBackListner, List<GetMastDatum> getFoundationData) {
        this.mContext = (SuperAdminNewEntryMastErictionList) context;
        this.callBackListner = callBackListner;
        this.getFoundationData = getFoundationData;
    }

    // 1
    @Override
    public int getItemCount() {
        return getFoundationData.size();
    }

    // 2
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mast_eriction_list_adaptor, parent, false);
        return new ViewHolder(view);
    }

    // 3
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.edit_station_name.setText(getFoundationData.get(position).getStationName());
        holder.edit_station_km.setText(getFoundationData.get(position).getStationKm());
        holder.edit_no_foundation.setText(getFoundationData.get(position).getMast());

        //holder.edit_progress_entry.setText(getFoundationData.get(position).getStationName());
        holder.edit_progress_entry.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getFoundationData.get(position).setMast_prog_no(s.toString());
            }
        });
       /* holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.deleteModule(getFoundationData.get(position).getCrs_id());
            }
        });*/

/*        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.updateHeadQuater(getFoundationData.get(position));
            }
        });*/
    }

    // 3
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        public LinearLayout placeHolder;
//        public LinearLayout placeNameHolder;
        public ImageView imageViewEdit;
        public ImageView imageViewDelete;
        public EditText edit_station_name,edit_station_km,edit_no_foundation,edit_progress_entry;
        public TextView org_name;
        public ViewHolder(View itemView) {
            super(itemView);

            edit_station_name=(EditText)itemView.findViewById(R.id.edit_station_name);
            edit_station_km=(EditText)itemView.findViewById(R.id.edit_station_km);
            edit_no_foundation=(EditText)itemView.findViewById(R.id.edit_no_foundation);
            edit_progress_entry=(EditText)itemView.findViewById(R.id.edit_progress_entry);

            imageViewDelete = (ImageView) itemView.findViewById(R.id.imageViewDelete);
            imageViewEdit = (ImageView) itemView.findViewById(R.id.imageViewEdit);
            org_name=(TextView)itemView.findViewById(R.id.org_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //  Log.d("dd", "onClick " + getPosition());
            /*if((Build.VERSION.SDK_INT)>20)
            {
                ((RippleDrawable)view.getBackground()).setHotspot(1,1);
            }*/
            callBackListner.callBack(getLayoutPosition());

        }
    }


}
