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
import alina.com.rms.activities.superAdminActivities.SuperAdminGroupList;
import alina.com.rms.custom_interface.ServiceClickCallBack;
import alina.com.rms.model.Headquateruserlist;


/**
 * Created by palash on 22-06-2016.
 */
public class SuperAdminUserListAdapter extends RecyclerView.Adapter<SuperAdminUserListAdapter.ViewHolder> {
    private ServiceClickCallBack callBackListner;
    private SuperAdminGroupList mContext;
    private List<Headquateruserlist> headquaterlist;
    public SuperAdminUserListAdapter(Context context, ServiceClickCallBack callBackListner, List<Headquateruserlist> headquaterlist) {
        this.mContext = (SuperAdminGroupList) context;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_user_super_admin, parent, false);
        return new ViewHolder(view);
    }

    // 3
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.edit_name.setText(headquaterlist.get(position).getName());
        holder.edit_email.setText(headquaterlist.get(position).getEmail());
        holder.edit_password.setText(headquaterlist.get(position).getPassword());
        holder.edit_phone.setText(headquaterlist.get(position).getMobile());
        if(headquaterlist.get(position).getHeadquarter()!=null)
        {
            holder.headQuater_Heading.setText("Headquaters : "+headquaterlist.get(position).getHeadquarter());
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
        public EditText edit_name,edit_email,edit_password,edit_phone;
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
