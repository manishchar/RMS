package alina.com.rms.navDrawer;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import alina.com.rms.R;


/**
 * Created by HP on 01-12-2017.
 */

public class NavigationRecyleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AdapterGetterSetter> adapterGetterSettersList;

    public class NavHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView personNameTextView, emailTextView;
        public ImageView headerImageView;
        public NavHeaderViewHolder(View view) {
            super(view);
            headerImageView=(ImageView)view.findViewById(R.id.itemImageView);
           personNameTextView = (TextView) view.findViewById(R.id.textView2);
           // emailTextView = (TextView) view.findViewById(R.id.emailTextView);*/
        }
    }

    public class NavItemViewHolder extends RecyclerView.ViewHolder {
        public TextView title,itemText;
        public ImageView itemImageView;
        public NavItemViewHolder(View view) {
            super(view);
            itemImageView = (ImageView) view.findViewById(R.id.itemImageView);
            itemText = (TextView) view.findViewById(R.id.textView2);
        }
    }


    public NavigationRecyleAdapter(List<AdapterGetterSetter> adapterGetterSettersList) {
        this.adapterGetterSettersList = adapterGetterSettersList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

       if (viewType==adapterGetterSettersList.size()-1)
        {
          itemView=  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.nav_item_list_view1, parent, false);
            return new NavigationRecyleAdapter.NavHeaderViewHolder(itemView);
        }
        else {
            itemView=  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.nav_item_list_view, parent, false);
            return new NavigationRecyleAdapter.NavItemViewHolder(itemView);
        }
/*        itemView=  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nav_item_list_view, parent, false);
        return new NavigationRecyleAdapter.NavItemViewHolder(itemView);*/

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        int tempPosition=position;

/*        AdapterGetterSetter movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());*/
       /* Log.e("Position",""+position);
        Log.e("TempOsition",""+tempPosition);*/
        AdapterGetterSetter movie = adapterGetterSettersList.get(tempPosition);
        if(position==adapterGetterSettersList.size()-1)
        {
            NavHeaderViewHolder navHeaderViewHolder=(NavHeaderViewHolder)holder;
            navHeaderViewHolder.personNameTextView.setText(movie.getItem_name());
            navHeaderViewHolder.headerImageView.setImageResource(movie.getItem_image());
        }
        else {
            NavItemViewHolder navItemViewHolderholder=(NavItemViewHolder)holder;

           try {
               navItemViewHolderholder.itemImageView.setImageResource(movie.getItem_image());
           }catch (Exception e)
           {
               Log.e("Exception",""+e);
           }
            navItemViewHolderholder.itemText.setText(movie.getItem_name());
          //  Log.e("TempOsition",""+movie.getItem_name());
        /*holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());*/
        }

    }

    @Override
    public int getItemCount() {
        return adapterGetterSettersList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        //return position % 2 * 2;
        return position;
    }
}
