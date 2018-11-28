package alina.com.rms.activities.headquater_special_user.tss_progress;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.activities.headquater_special_user.HeadQuaterSpecialUserBaseActivity;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.EditWiringProgress;
import alina.com.rms.model.GetActivityDatum;
import alina.com.rms.model.LoginResultPojo;
import alina.com.rms.model.Result;
import alina.com.rms.util.CallType;
import alina.com.rms.util.CameraAndGalleryUtil;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class HeadQuaterSpecialUserProgressEditTSSActivity extends HeadQuaterSpecialUserBaseActivity implements AsyncCompleteListner {

    private List<EditWiringProgress> editProgresses = new ArrayList<EditWiringProgress>();

    public EditText edit_station_name, edit_station_km, edit_no_foundation, edit_progress_entry;

    private Button button_update;
    private String foundation_prog1__id;
    private TextView dateTextView;
    private RelativeLayout rel_date;
    private ImageView img_view;
    private CameraAndGalleryUtil cameraAndGalleryUtil;
    private File output1;
    ProgressBar progressBar;
    private String type = "";
    private String station_id = "";

    private List<LoginResultPojo> loginResultPojo;
    private LoginResultPojo loginResultPojo1;
    private String login_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_super_admin_foundation);

        login_response = LoginDB.getLoginResponseAsJSON(this);
        Gson gson = new Gson();
        Type type1 = new TypeToken<List<LoginResultPojo>>() {
        }.getType();
        loginResultPojo = gson.fromJson(login_response, type1);
        loginResultPojo1 = loginResultPojo.get(0);
        //setContentView(R.layout.activity_super_admin_foundation);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            foundation_prog1__id = bundle.getString("foundation_prog1__id");
            type = bundle.getString("type");
            station_id = bundle.getString("station_id");

        }
        headingText.setText("Edit TSS Progress");
        addView();
        getEditData();
    }


    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_progress_edit, null);
        setView(myView);
        img_view = (ImageView) findViewById(R.id.img_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        edit_station_name = (EditText) findViewById(R.id.edit_station_name);
        edit_station_km = (EditText) findViewById(R.id.edit_station_km);
        edit_no_foundation = (EditText) findViewById(R.id.edit_no_foundation);
        edit_progress_entry = (EditText) findViewById(R.id.edit_progress_entry);
        dateTextView = (TextView) findViewById(R.id.txt_date);
        button_update = (Button) findViewById(R.id.button_update);
        rel_date = (RelativeLayout) findViewById(R.id.rel_date);
        button_update.setVisibility(View.VISIBLE);

        rel_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(dateTextView, HeadQuaterSpecialUserProgressEditTSSActivity.this);
            }
        });
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //nextStepToFoundation();
                if (!Util.checkVAlue(edit_no_foundation, edit_progress_entry)) {
                    Util.showLongToast(getApplicationContext(), "Entry progress can not be greater then given progress");
                    return;
                }

                if (!edit_progress_entry.getText().toString().trim().isEmpty()) {
                    submitToserver();
                } else {
                    Util.showLongToast(getApplicationContext(), "please fill progress entry");
                }
            }
        });


        img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img = (ImageView) v;
                Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                selectImage(bitmap);
            }
        });


    }

    private void submitToserver() {

        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(HeadQuaterSpecialUserProgressEditTSSActivity.this, this,
                CallType.SAVE_FOUNDATION_ITEM, "", true);
        // asyncController.setProgressDialoug(true);
        String date = dateTextView.getText().toString();
        List<GetActivityDatum> getFoundationData = new ArrayList<GetActivityDatum>();
        GetActivityDatum getFoundationDatum = new GetActivityDatum();
        Bitmap bitmap = ((BitmapDrawable) img_view.getDrawable()).getBitmap();
        getFoundationDatum.set_prog_no(edit_progress_entry.getText().toString());
        getFoundationDatum.setActivity(editProgresses.get(0).getWiring());
        getFoundationDatum.setStationName(edit_station_name.getText().toString());
        getFoundationDatum.setStationKm(edit_station_km.getText().toString());
        getFoundationDatum.setStationId(editProgresses.get(0).getStationId());
        getFoundationDatum.setId("");
        getFoundationDatum.setLng("");
        getFoundationDatum.setLat("");
        getFoundationDatum.setUser_id(loginResultPojo1.getUserId());
        getFoundationDatum.setBitmap(bitmap);
        getFoundationData.add(getFoundationDatum);
        asyncController.saveProfileAccount(getFoundationData, date, foundation_prog1__id, type,
                "addtssActivityProgress", "tss_name");

    }


    private void getEditData() {

        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(HeadQuaterSpecialUserProgressEditTSSActivity.this, this, CallType.EDIT_FOUNDATION_DATA, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getEditTssProgressView(foundation_prog1__id, type, station_id);
    }

    public void openDatePickerDialog(final TextView editText, HeadQuaterSpecialUserProgressEditTSSActivity context) {
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
                        if ((monthOfYear + 1) < 10) {
                            month = "0" + (monthOfYear + 1);
                        } else {
                            month = "" + (monthOfYear + 1);
                            ;

                        }

                        if ((dayOfMonth) < 10) {
                            date = "0" + dayOfMonth;
                        } else {
                            date = "" + dayOfMonth;
                        }

                        editText.setText(year + "-" + month + "-"
                                + date);
                        String sdate = editText.getText().toString();
                        boolean b = true;
                        // context.addDate_tolist(position,sdate,b);

                    }
                }, mYear, mMonth, mDay);
        dpd.setTitle("Select Date");
        // dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraAndGalleryUtil.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            //cameraAndGalleryUtil.setPic(profile_image1);
            String photoUri = cameraAndGalleryUtil.getPhotoURI();
            //deleteFile();
            output1 = new File(photoUri);
            Uri uri = Uri.fromFile(output1);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                output1 = new File(cameraAndGalleryUtil.saveGalaryImageOnLitkat(bitmap, "rms.jpg"));
                Bitmap bitmap2 = BitmapFactory.decodeFile(output1.getPath());
                img_view.setImageBitmap(bitmap2);

                cameraAndGalleryUtil.deleteFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void selectImage(final Bitmap bm) {
        final CharSequence[] items = {"Take Photo from camera", "View Image", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    if (cameraAndGalleryUtil == null) {
                        cameraAndGalleryUtil = new CameraAndGalleryUtil(HeadQuaterSpecialUserProgressEditTSSActivity.this);
                    }
                    cameraAndGalleryUtil.dispatchTakePictureIntent("rms.jpg");

                } else if (item == 1) {

                    showImageDialog(bm);
                } else if (item == 2) {
                    dialog.dismiss();

                }
            }
        });
        AlertDialog dialog = builder.create();
        //dialog.getWindow().setLayout(800,600);
        dialog.show();


    }

    public void showImageDialog(Bitmap showDialog) {
        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.image_dialog, null, false);
        dialog.setContentView(view);
        ImageView dialogImageView = (ImageView) dialog.findViewById(R.id.dialogImageView);
        dialogImageView.setImageBitmap(showDialog);
        ImageView close_btn = (ImageView) dialog.findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int[] width_height = Util.getDisplayHeightAndWidth(this);
        lp.width = width_height[0] - 50; // Width
        lp.height = width_height[1] - 50; // Heigh
        dialogWindow.setAttributes(lp);
        dialog.show();


    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {

        setProgressDialoug(false);

        if (response != null) {
            if (callType == CallType.EDIT_FOUNDATION_DATA) {
                Gson gson = new Gson();
                Result response1 = gson.fromJson(response, Result.class);

                if (response1 != null) {

                    if (response1.getEditWiringProgress() != null) {
                        Log.e("String", "value");

                        if (editProgresses.size() > 0) {
                            editProgresses.clear();
                        }
                        editProgresses.addAll(response1.getEditWiringProgress());

                        if (editProgresses.size() < 1) {
                            Util.showLongToast(getApplicationContext(), "SOmething is wrong try again");
                            finish();
                        } else {
                            String img_type = editProgresses.get(0).getImg();
                            Util.setImage(getApplicationContext(), img_view, progressBar, img_type);
                            edit_station_name.setText(editProgresses.get(0).getStationName());
                            edit_station_km.setText(editProgresses.get(0).getStationKm());
                            edit_no_foundation.setText(editProgresses.get(0).getWiring());
                            edit_progress_entry.setText(editProgresses.get(0).getWiringProgNo());
                            dateTextView.setText(editProgresses.get(0).getWiringDate());
                        }

                    } else {
                        Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
            } else if (callType == CallType.SAVE_FOUNDATION_ITEM) {
                Gson gson = new Gson();
                Result response1 = gson.fromJson(response, Result.class);
                if (response1 != null) {
                    if (response1.getResponseCode() == 1) {
                        Util.showLongToast(getApplicationContext(), response1.getMessage());
                        finish();
                    } else {
                        Util.showLongToast(getApplicationContext(), response1.getMessage());
                        // finish();
                    }
                }
            }
        } else {
            Toast.makeText(this, "Error in network", Toast.LENGTH_LONG).show();
            finish();
        }

    }

}

