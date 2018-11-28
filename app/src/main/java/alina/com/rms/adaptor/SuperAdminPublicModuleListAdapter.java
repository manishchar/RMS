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
import alina.com.rms.activities.superAdminActivities.SuperAdminPublicModuleList;
import alina.com.rms.custom_interface.ServiceClickCallBack;
import alina.com.rms.model.RkmList;


/**
 * Created by palash on 22-06-2016.
 */
public class SuperAdminPublicModuleListAdapter extends RecyclerView.Adapter<SuperAdminPublicModuleListAdapter.ViewHolder> {
    private ServiceClickCallBack callBackListner;
    private SuperAdminPublicModuleList  mContext;
    private List<RkmList> headquaterlist;
    public SuperAdminPublicModuleListAdapter(Context context, ServiceClickCallBack callBackListner, List<RkmList> headquaterlist) {
        this.mContext = (SuperAdminPublicModuleList) context;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_module_list_adaptor, parent, false);
        return new ViewHolder(view);
    }

    // 3
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.edit_proj_name.setText(headquaterlist.get(position).getProjName());
        holder.edit_rkm.setText(headquaterlist.get(position).getRkm());

        holder.edit_group_name.setText(headquaterlist.get(position).getCrs());
        holder.edit_division.setText(headquaterlist.get(position).getDivision());

        holder.edit_zone.setText(headquaterlist.get(position).getZone());
        holder.edit_state.setText(headquaterlist.get(position).getState());
        holder.edit_section.setText(headquaterlist.get(position).getSection_name());
        holder.edit_month.setText(headquaterlist.get(position).getMonth());
        holder.org_name.setText("Org Name : "+headquaterlist.get(position).getOrg_name());
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.deleteModule(headquaterlist.get(position).getCrs_id());
            }
        });

        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.updateHeadQuater(headquaterlist.get(position));
            }
        });
    }

    // 3
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        public LinearLayout placeHolder;
//        public LinearLayout placeNameHolder;
        public ImageView imageViewEdit;
        public ImageView imageViewDelete;
        public EditText edit_proj_name,edit_rkm,edit_group_name,edit_division,edit_zone,edit_state,edit_section,edit_month;
        public TextView org_name;
        public ViewHolder(View itemView) {
            super(itemView);
            edit_proj_name=(EditText)itemView.findViewById(R.id.edit_proj_name);
            edit_rkm=(EditText)itemView.findViewById(R.id.edit_rkm);
            edit_group_name=(EditText)itemView.findViewById(R.id.edit_group_name);
            edit_division=(EditText)itemView.findViewById(R.id.edit_division);
            edit_zone=(EditText)itemView.findViewById(R.id.edit_zone);
            edit_state=(EditText)itemView.findViewById(R.id.edit_state);
            edit_section=(EditText)itemView.findViewById(R.id.edit_section);
            edit_month=(EditText)itemView.findViewById(R.id.edit_month);
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
