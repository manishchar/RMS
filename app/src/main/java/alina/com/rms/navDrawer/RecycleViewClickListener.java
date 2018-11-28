package alina.com.rms.navDrawer;

import android.view.View;

/**
 * Created by HP on 01-12-2017.
 */

public interface RecycleViewClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
