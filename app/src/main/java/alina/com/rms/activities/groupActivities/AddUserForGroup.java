package alina.com.rms.activities.groupActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.adaptor.CustomAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.GroupResponse;
import alina.com.rms.model.GroupResponseOfResponse;
import alina.com.rms.model.Result;
import alina.com.rms.model.Userlist;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class AddUserForGroup extends GroupBaseActivity implements AsyncCompleteListner, AdapterView.OnItemSelectedListener {
    List<String> headQuaterNames = new ArrayList<String>();
    CustomAdapter customAdapter;
    String selected_headQuater = "";
    private List<Userlist> headquaterlist = new ArrayList<Userlist>();
    Bundle bundle;
    private EditText edit_name, edit_email, edit_password, edit_phn;
    private String name, email, password, phn;
    Userlist headquateruserlist;
    private RelativeLayout rel_spinner;

    GroupResponse groupResponse;
    GroupResponseOfResponse groupResponseOfResponse;
    List<GroupResponseOfResponse> responseOfResponses = new ArrayList<GroupResponseOfResponse>();

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_user_for_super_admin);

        Gson gson = new Gson();
        groupResponse = gson.fromJson(LoginDB.getUSerResponseAsJSON(AddUserForGroup.this), GroupResponse.class);
        responseOfResponses.clear();
        responseOfResponses.addAll(groupResponse.getResponse());
        groupResponseOfResponse = responseOfResponses.get(0);
        selected_headQuater = groupResponseOfResponse.getHeadquater();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            headingText.setText("Update User");
            headquateruserlist = (Userlist) bundle.getSerializable("data");

        } else {
            headingText.setText("Add User");
        }
        addView();
        initUI();
    }

    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_add_user_for_super_admin, null);
        setView(myView);
    }

    private void initUI() {
        //  headingText.setText("User List");
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        rel_spinner = (RelativeLayout) findViewById(R.id.rel_spinner);
        rel_spinner.setVisibility(View.GONE);
        Button submit = (Button) findViewById(R.id.submitBtn);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_phn = (EditText) findViewById(R.id.edit_phone);
        if (headquateruserlist != null) {
            edit_name.setText(headquateruserlist.getName());
            edit_email.setText(headquateruserlist.getEmail());
            edit_password.setText(headquateruserlist.getPassword());
            edit_phn.setText(headquateruserlist.getMobile());
        }
        /*customAdapter=new CustomAdapter(AddUserForGroup.this,headQuaterNames);
        spin.setAdapter(customAdapter);*/
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle != null) {
                    /*Util.showLongToast(AddUserForHeaduater.this,"Email is not valid55");*/
                    updateUser();
                } else {
                    //Util.showLongToast(AddUserForHeaduater.this,"Email is not valid");
                    addUser();
                }
            }
        });


        //  getHeadquaterListFromServer();
        //
    }

    private void getHeadquaterListFromServer() {
        AsyncController asyncController = new AsyncController(AddUserForGroup.this, this, CallType.GET_HQ_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getHQListForGroup(groupResponseOfResponse.getHeadquater());
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //Toast.makeText(getApplicationContext(), countryNames.get(position), Toast.LENGTH_LONG).show();
        selected_headQuater = headquaterlist.get(position).getId();
        //loginResultPojo1.setOhetype(keyValues.get(position));


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateUser() {
        if (checkConditin()) {
            name = edit_name.getText().toString();
            email = edit_email.getText().toString();
            phn = edit_phn.getText().toString();
            password = edit_password.getText().toString();

            AsyncController asyncController = new AsyncController(this, this, CallType.ADD_SUPER_ADMIN_USER, "", true);
            asyncController.setProgressDialoug(true);
            asyncController.updateUserForGroup(selected_headQuater, name, email, phn, password, headquateruserlist.getId()
                    , groupResponseOfResponse.getGroup());
        }
    }

    private void addUser() {
        if (checkConditin()) {
            name = edit_name.getText().toString();
            email = edit_email.getText().toString();
            phn = edit_phn.getText().toString();
            password = edit_password.getText().toString();
            setProgressDialoug(true);
            AsyncController asyncController = new AsyncController(this, this, CallType.ADD_SUPER_ADMIN_USER, "", true);
            asyncController.setProgressDialoug(true);
            asyncController.addUserForGroup(selected_headQuater, name, email, phn, password, groupResponseOfResponse.getUserId()
                    , groupResponseOfResponse.getGroup());
        }

    }

    private boolean checkConditin() {
        if (!Util.isValidEmail(edit_email.getText())) {
            Util.showLongToast(this, "Email is not valid");
            return false;
        } else if (edit_password.getText() != null) {
            if (edit_password.getText().toString().length() < 2) {
                Util.showLongToast(this, "Password must be greater then 3");
                return false;
            }
        } else if (!Util.chechTextValue(edit_name)) {
            Util.showShortToast(this, "Enter name");
            return false;
        } else if (!Util.chechEditTextValue(edit_phn)) {
            Util.showShortToast(this, "Enter phone number");
            return false;
        } else if (selected_headQuater == null) {
            Util.showLongToast(this, "No Headquater is selected or available");
            return false;
        }
        return true;
    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if (response != null) {
            if (callType == CallType.GET_HQ_LIST) {
                Gson gson = new Gson();
                Result response1 = gson.fromJson(response, Result.class);

                if (response1 != null) {
                    if (response1.getHeadquaterlist() != null) {
                        Log.e("String", "value");
                        headquaterlist.clear();
                        headquaterlist.addAll(response1.getUserlist());
                        headQuaterNames.clear();
                        for (int i = 0; i < headquaterlist.size(); i++) {
                            if (headquateruserlist != null) {
                                if (headquaterlist.get(i).getId().equalsIgnoreCase(headquateruserlist.getHeadquaterId())) {
                                    headQuaterNames.add(0, headquaterlist.get(i).getName());
                                } else {
                                    headQuaterNames.add(headquaterlist.get(i).getName());
                                }
                            } else {
                                headQuaterNames.add(headquaterlist.get(i).getName());
                            }
                        }
                        // Log.e("value",""+headquaterlist.size());
                        customAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                }


            } else {
                Gson gson = new Gson();
                Result response1 = gson.fromJson(response, Result.class);
                Util.showLongToast(this, response1.getMessage());
                finish();

            }
        }
    }
}
