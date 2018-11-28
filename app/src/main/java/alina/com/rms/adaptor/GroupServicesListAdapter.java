package alina.com.rms.adaptor;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import alina.com.rms.R;
import alina.com.rms.custom_interface.ServiceClickCallBack;


/**
 * Created by palash on 22-06-2016.
 */
public class GroupServicesListAdapter extends RecyclerView.Adapter<GroupServicesListAdapter.ViewHolder> {
    private ServiceClickCallBack callBackListner;
    private Context mContext;
    private String[] homeScreenItems = {"OHE", "PSI", "TR Line", "Other", "User LIST"};
    private String[] homeScreenItemBackground = {"#c16a2a", "#d4bc3a", "#62aa13", "#3a93d4","#3f3c92"};//, "#7EE84A", "#0BCE54", "#4BA4E8", "#2A47E0", "#AF4BE9", "#7D04C1", "#E94BE6", "#C105B2"};
  /*  private String[] homeScreenItemBackground = {"#3B5999","#0077B5", "#21759B", "#3F729B", "#0077B5", "#3B5999","#3AAF85", "#00B489", "#B92b27", "#EB4924",
            "#FF0084", "#F94877"};*/
    private int[] homeScreenDrawable = {R.drawable.mechanic, R.drawable.manager, R.drawable.layer10, R.drawable.engineer,R.drawable.group2};

    public GroupServicesListAdapter(Context context, ServiceClickCallBack callBackListner) {
        this.mContext = context;
        this.callBackListner = callBackListner;
    }

    // 1
    @Override
    public int getItemCount() {
        return homeScreenItems.length;
    }

    // 2
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listview_home_screen_items, parent, false);
        return new ViewHolder(view);
    }

    // 3
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        final Place place = new PlaceData().placeList().get(position);
     //   Log.e("color",""+position);
        holder.cardView.setCardBackgroundColor(Color.parseColor(homeScreenItemBackground[position].trim()));
        holder.placeName.setText(homeScreenItems[position]);
//        Picasso.with(mContext).load(place.getImageResourceId(mContext)).into(holder.placeImage);
        holder.placeImage.setBackgroundDrawable(mContext.getResources().getDrawable(homeScreenDrawable[position]));
    }

    // 3
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        public LinearLayout placeHolder;
//        public LinearLayout placeNameHolder;
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
          //  Log.d("dd", "onClick " + getPosition());
            /*if((Build.VERSION.SDK_INT)>20)
            {
                ((RippleDrawable)view.getBackground()).setHotspot(1,1);
            }*/
            callBackListner.callBack(getLayoutPosition());

        }
    }


}
