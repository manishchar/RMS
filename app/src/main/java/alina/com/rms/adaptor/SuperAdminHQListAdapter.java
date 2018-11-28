package alina.com.rms.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import alina.com.rms.R;
import alina.com.rms.activities.superAdminActivities.SuperAdminHQList;
import alina.com.rms.custom_interface.ServiceClickCallBack;
import alina.com.rms.model.Headquaterlist;


/**
 * Created by palash on 22-06-2016.
 */
public class SuperAdminHQListAdapter extends RecyclerView.Adapter<SuperAdminHQListAdapter.ViewHolder> {
    private ServiceClickCallBack callBackListner;
    private SuperAdminHQList mContext;
    private List<Headquaterlist> headquaterlist;
    public SuperAdminHQListAdapter(Context context, ServiceClickCallBack callBackListner, List<Headquaterlist> headquaterlist) {
        this.mContext = (SuperAdminHQList) context;
        this.callBackListner = callBackListner;
        this.headquaterlist = headquaterlist;
    }

    // 1
    @Override
    public int getItemCount() {
        return headquaterlist.size();
    }

    // 2
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hq_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    // 3
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.group_name_txt.setText(headquaterlist.get(position).getName());
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.deleteHQ(headquaterlist.get(position).getId());
            }
        });

        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.updateHeadQuater(headquaterlist.get(position).getId(),headquaterlist.get(position).getName());
            }
        });
    }

    // 3
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        public LinearLayout placeHolder;
//        public LinearLayout placeNameHolder;
        public TextView group_name_txt,group_quantity_txt;
        public ImageView imageViewEdit;
        public ImageView imageViewDelete;

        public ViewHolder(View itemView) {
            super(itemView);
//            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            group_name_txt = (TextView) itemView.findViewById(R.id.group_name_txt);
            group_quantity_txt = (TextView) itemView.findViewById(R.id.group_quantity_txt);
//            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            imageViewDelete = (ImageView) itemView.findViewById(R.id.imageViewDelete);
            imageViewEdit = (ImageView) itemView.findViewById(R.id.imageViewEdit);
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
