package alina.com.rms.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import alina.com.rms.R;
import alina.com.rms.activities.headquaterActivities.HeadquaterSectionListActivity;
import alina.com.rms.custom_interface.ServiceClickCallBack;
import alina.com.rms.model.Sectionlist;


/**
 * Created by palash on 22-06-2016.
 */
public class HeadquaterSectionListAdapter extends RecyclerView.Adapter<HeadquaterSectionListAdapter.ViewHolder> {
    private ServiceClickCallBack callBackListner;
    private HeadquaterSectionListActivity mContext;
    private List<Sectionlist> headquaterlist;
    public HeadquaterSectionListAdapter(Context context, ServiceClickCallBack callBackListner, List<Sectionlist> headquaterlist) {
        this.mContext = (HeadquaterSectionListActivity) context;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_section_list_adaptor, parent, false);
        return new ViewHolder(view);
    }

    // 3
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.edit_name.setText(headquaterlist.get(position).getSection());
        holder.edit_email.setText(headquaterlist.get(position).getAgency());
        holder.edit_password.setText(headquaterlist.get(position).getCrs());
        holder.edit_phone.setText(headquaterlist.get(position).getTargete());
        if(headquaterlist.get(position).getRkm()!=null)
        {
            holder.edit_rkm.setText(headquaterlist.get(position).getRkm());

        }
        if(headquaterlist.get(position).getTkm()!=null)
        {
            holder.edit_tkm.setText(headquaterlist.get(position).getTkm());
        }

        if(headquaterlist.get(position).getGroup()!=null)
        {
            holder.headQuater_Heading.setText("Group : "+headquaterlist.get(position).getGroup());
        }

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.deleteHQ(headquaterlist.get(position).getId());
            }
        });

        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.updateHeadQuater(position);
            }
        });
    }

    // 3
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        public LinearLayout placeHolder;
//        public LinearLayout placeNameHolder;
        public EditText edit_name,edit_email,edit_password,edit_phone,edit_rkm,edit_tkm;
        public ImageView imageViewEdit;
        public ImageView imageViewDelete;
        public TextView headQuater_Heading;

        public ViewHolder(View itemView) {
            super(itemView);
//            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            edit_name = (EditText) itemView.findViewById(R.id.edit_name);
            edit_email = (EditText) itemView.findViewById(R.id.edit_email);
            edit_password = (EditText) itemView.findViewById(R.id.edit_password);
            edit_phone = (EditText) itemView.findViewById(R.id.edit_phone);
            edit_rkm = (EditText) itemView.findViewById(R.id.edit_rkm);
            edit_tkm = (EditText) itemView.findViewById(R.id.edit_tkm);
            headQuater_Heading=(TextView)itemView.findViewById(R.id.headQuater_Heading);
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
