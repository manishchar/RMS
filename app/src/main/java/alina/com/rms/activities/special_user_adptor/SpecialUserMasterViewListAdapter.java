package alina.com.rms.activities.special_user_adptor;

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
import alina.com.rms.activities.special_user.SpecialUserViewMastErictionList;
import alina.com.rms.custom_interface.ServiceClickCallBack;
import alina.com.rms.model.MastProgressList;


/**
 * Created by palash on 22-06-2016.
 */
public class SpecialUserMasterViewListAdapter extends RecyclerView.Adapter<SpecialUserMasterViewListAdapter.ViewHolder> {
    private ServiceClickCallBack callBackListner;
    private SpecialUserViewMastErictionList mContext;
    private List<MastProgressList> progressLists;
    public SpecialUserMasterViewListAdapter(Context context, ServiceClickCallBack callBackListner, List<MastProgressList> progressLists) {
        this.mContext = (SpecialUserViewMastErictionList) context;
        this.callBackListner = callBackListner;
        this.progressLists = progressLists;
    }

    // 1
    @Override
    public int getItemCount() {
        return progressLists.size();
    }

    // 2
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.masteriction_view_list_adaptor, parent, false);
        return new ViewHolder(view);
    }

    // 3
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.edit_project_name.setText(progressLists.get(position).getProjectName());
        holder.edit_group_name.setText(progressLists.get(position).getGroupName());
        holder.edit_section_name.setText(progressLists.get(position).getSectionName());
        holder.edit_foundation_prog_no.setText(progressLists.get(position).getMastProgNo());
        holder.edit_foundation_date.setText(progressLists.get(position).getMastDate());
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mContext.deleteModule(progressLists.get(position).getMastProgId());
            }
        });

        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.updateHeadQuater(progressLists.get(position));
            }
        });
    }

    // 3
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        public LinearLayout placeHolder;
//        public LinearLayout placeNameHolder;
        public ImageView imageViewEdit;
        public ImageView imageViewDelete;
        public EditText edit_project_name,edit_group_name,edit_section_name,edit_foundation_prog_no,edit_foundation_date;
        public TextView org_name;
        public ViewHolder(View itemView) {
            super(itemView);
            edit_project_name=(EditText)itemView.findViewById(R.id.edit_project_name);
            edit_group_name=(EditText)itemView.findViewById(R.id.edit_group_name);
            edit_section_name=(EditText)itemView.findViewById(R.id.edit_section_name);
            edit_foundation_prog_no=(EditText)itemView.findViewById(R.id.edit_foundation_prog_no);
            edit_foundation_date=(EditText)itemView.findViewById(R.id.edit_foundation_date);

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
