package alina.com.rms.adaptor;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import alina.com.rms.R;
import alina.com.rms.custom_interface.ServiceClickCallBack;


/**
 * Created by palash on 22-06-2016.
 */
public class ReportingProjectListAdapter extends RecyclerView.Adapter<ReportingProjectListAdapter.ViewHolder> {
    private ServiceClickCallBack callBackListner;
    private Context mContext;
    private List<String>projName;
   // String prevColor;
    int prevColorCount;
    private String[] homeScreenItemBackground = {"#3B5999","#B18904"};//, "#7EE84A", "#0BCE54", "#4BA4E8", "#2A47E0", "#AF4BE9", "#7D04C1", "#E94BE6", "#C105B2"};
    /*  private String[] homeScreenItemBackground = {"#3B5999","#0077B5", "#21759B", "#3F729B", "#0077B5", "#3B5999","#3AAF85", "#00B489", "#B92b27", "#EB4924",
              "#FF0084", "#F94877"};*/
    private int[] homeScreenDrawable = {R.drawable.manager};

    public ReportingProjectListAdapter(Context context, ServiceClickCallBack callBackListner, List<String> projName) {
        this.mContext = context;
        this.callBackListner = callBackListner;
        this.projName = projName;
    }

    // 1
    @Override
    public int getItemCount() {
        return projName.size();
    }

    // 2
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proj_item_list, parent, false);
        return new ViewHolder(view);
    }

    // 3
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

            if(position==0)
            {
                holder.cardView.setCardBackgroundColor(Color.parseColor(homeScreenItemBackground[0].trim()));
            }
            else if(position==1)
            {
                holder.cardView.setCardBackgroundColor(Color.parseColor(homeScreenItemBackground[1].trim()));
                prevColorCount = 1;
            }

           else if(prevColorCount<2) {
                holder.cardView.setCardBackgroundColor(Color.parseColor(homeScreenItemBackground[1].trim()));
                prevColorCount++;
            }
            else {
                holder.cardView.setCardBackgroundColor(Color.parseColor(homeScreenItemBackground[0].trim()));
                prevColorCount += 1;
                if(prevColorCount>3)
                {
                    prevColorCount = 0;
                }
            }


        holder.placeName.setText(projName.get(position));
//        Picasso.with(mContext).load(place.getImageResourceId(mContext)).into(holder.placeImage);

            holder.placeImage.setBackgroundDrawable(mContext.getResources().getDrawable(homeScreenDrawable[0]));

   }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView placeName;
        public ImageView placeImage;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
//            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
//            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            placeImage = (ImageView) itemView.findViewById(R.id.placeImage);
            cardView = (CardView) itemView.findViewById(R.id.placeCard);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            callBackListner.callBack(getLayoutPosition());

        }
    }


}
