package alina.com.rms.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import alina.com.rms.R;

/**
 * Created by HP on 02-01-2018.
 */

public class Util {

    public static String htmlString ="<html><body><div style=\"text-align:center; background:#fff\"><img src=\"out.gif\" style=\"width:auto;top:0;height:100%;\"><br><br></div></body></html>";

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void showLongToast(Context mContext,String msg)
    {
        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(Context mContext,String msg)
    {
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }


    public static void openDatePickerDialog(final TextView editText, Context context) {
        int mYear;
        int mMonth;
        int mDay;
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 0);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DATE);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        // Display Selected date in textbox
                        String date;
                        String month;
                        if((monthOfYear + 1)<10)
                        {
                            month="0"+(monthOfYear + 1);
                        }
                        else {
                            month=""+(monthOfYear + 1);;

                        }

                        if((dayOfMonth)<10)
                        {
                            date="0"+dayOfMonth;
                        }
                        else {
                            date=""+dayOfMonth;
                        }

                        editText.setText(year + "-" + month+ "-"
                                + date);
                        String sdate=editText.getText().toString();
                        boolean b=true;
                        // context.addDate_tolist(position,sdate,b);

                    }
                }, mYear, mMonth, mDay);
        dpd.setTitle("Select Date");
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }

    public static boolean chechTextValue(TextView textView)
    {
        if(textView.getText().toString()!=null)
        {
            if(textView.getText().toString().trim().length()>0)
            {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public static boolean chechEditTextValue(EditText textView)
    {
        if(textView.getText().toString()!=null)
        {
            if(textView.getText().toString().trim().length()>0)
            {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void showDialog(Context context,String msg)
    {
        new AlertDialog.Builder(context)
                .setTitle(msg)
                .setIcon(R.drawable.logo)
                .setPositiveButton("OK", null)
                .show();


    }

    public static void   nonEditableEditTextBox(EditText textView)
    {
        textView.setFocusable(false);
        textView.setFocusableInTouchMode(false);
        textView.setCursorVisible(false);
        textView.setClickable(false);
    }
    public static int[] getDisplayHeightAndWidth(AppCompatActivity appCompatActivity)
    {
        Display display = appCompatActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        return (new int[]{width,height});
    }


    public static boolean checkVAlue(EditText editText ,EditText editText1)
    {
        String e1 = editText.getText().toString();
        String e2 = editText1.getText().toString();
        if(e1.trim().isEmpty() ||e2.trim().isEmpty())
        {
         return false;
        }
        float a1= Float.parseFloat(e1);
        float a2= Float.parseFloat(e2);

        if(a2>a1)
        {
            return false;
        }
        /*if(e1.trim().isEmpty() ||e2.trim().isEmpty())
        {
            return false;
        }*/
        return true;
    }

    public static void setImage(Context mContext, ImageView imageView,final ProgressBar progressBar,String img_url)
    {
        if ( img_url!= null) {
            if (img_url.trim().length() > 0 && (img_url.endsWith(".jpg") || img_url.endsWith(".jpeg") ||
                    img_url.endsWith("png"))) {
                progressBar.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(img_url)
                        .placeholder(R.drawable.demo_img)
                        .error(R.drawable.demo_img)
                        .resize(300, 300)
                        .centerCrop()
                        .into(imageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError() {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        }
    }
}
