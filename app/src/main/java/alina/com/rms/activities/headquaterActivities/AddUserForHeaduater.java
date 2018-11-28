package alina.com.rms.activities.headquaterActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.adaptor.CustomAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.Grouplist;
import alina.com.rms.model.HeadQuaterGroupResponse;
import alina.com.rms.model.Response;
import alina.com.rms.model.Result;
import alina.com.rms.model.Userlist;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class AddUserForHeaduater extends HeaduaterBaseActivity implements AsyncCompleteListner,AdapterView.OnItemSelectedListener {
    List<String> headQuaterNames=new ArrayList<String>();
    CustomAdapter customAdapter;
    String selected_groupId;
    private List<Grouplist> headquaterlist=new ArrayList<Grouplist>();
    Bundle bundle;
    private EditText edit_name,edit_email,edit_password,edit_phn;
    private String name,email,password,phn;
    Userlist headquateruserlist;
    private Response response;
    private List<Response> response1;
    private int user_type=4; //user type 4 for data entry and 5 for report view
    private RadioButton radioButtonDataEntry,radioButtonReportView;
    private LinearLayout linear_select_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_user_for_super_admin);
        Gson gson=new Gson();
        Type type = new TypeToken<List<Response>>() {}.getType();
        response1=gson.fromJson(LoginDB.getLoginResponseAsJSON(AddUserForHeaduater.this),type);
        response=response1.get(0);
        bundle=getIntent().getExtras();

        if(bundle!=null)
        {
            headingText.setText("Update User");
            headquateruserlist=(Userlist)bundle.getSerializable("data");
        }
        else {
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
        Button submit=(Button)findViewById(R.id.submitBtn);
        edit_name=(EditText)findViewById(R.id.edit_name);
        edit_email=(EditText)findViewById(R.id.edit_email);
        edit_password=(EditText)findViewById(R.id.edit_password);
        edit_phn=(EditText)findViewById(R.id.edit_phone);
        linear_select_user=(LinearLayout)findViewById(R.id.linear_select_user);
        radioButtonDataEntry=(RadioButton)findViewById(R.id.radioBtnDataEntry);
        radioButtonReportView=(RadioButton)findViewById(R.id.radioBtnReportView);
        linear_select_user.setVisibility(View.VISIBLE);


        if(headquateruserlist!=null)
        {
            edit_name.setText(headquateruserlist.getName());
            edit_email.setText(headquateruserlist.getEmail());
            edit_password.setText(headquateruserlist.getPassword());
            edit_phn.setText(headquateruserlist.getMobile());
            if(headquateruserlist.getUsertype()==4)
            {
                radioButtonDataEntry.setChecked(true);
            }
            else if(headquateruserlist.getUsertype()==5)
            {
                radioButtonReportView.setChecked(true);
            }
        }

        customAdapter=new CustomAdapter(AddUserForHeaduater.this,headQuaterNames);
        spin.setAdapter(customAdapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButtonDataEntry.isChecked())
                {
                    user_type=4;
                }
                else {
                    user_type=5;
                }
                if(bundle!=null)
                {
                    /*Util.showLongToast(AddUserForHeaduater.this,"Email is not valid55");*/
                    updateUser();
                }
                else {
                    //Util.showLongToast(AddUserForHeaduater.this,"Email is not valid");
                    addUser();
                }
            }
        });


        getHeadquaterListFromServer();
        //
    }

    private void getHeadquaterListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(AddUserForHeaduater.this,this,CallType.GET_HQ_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getGroupList(response.getHeadquater(),response.getUserId());
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        //Toast.makeText(getApplicationContext(), countryNames.get(position), Toast.LENGTH_LONG).show();
        selected_groupId=headquaterlist.get(position).getId();
        //loginResultPojo1.setOhetype(keyValues.get(position));


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateUser()
    {
    if(checkConditin())
    {
        name=edit_name.getText().toString();
        email=edit_email.getText().toString();
        phn=edit_phn.getText().toString();
        password=edit_password.getText().toString();
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(this,this,CallType.ADD_SUPER_ADMIN_USER,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.updateUserForGroup1(response.getHeadquater(),name,email,phn,password,selected_groupId,headquateruserlist.getId(),""+user_type);
    }
    }

    private void addUser()
    {
        if(checkConditin())
        {
            name=edit_name.getText().toString();
            email=edit_email.getText().toString();
            phn=edit_phn.getText().toString();
            password=edit_password.getText().toString();
            setProgressDialoug(true);
            AsyncController asyncController=new AsyncController(this,this,CallType.ADD_SUPER_ADMIN_USER,"",true);
            asyncController.setProgressDialoug(true);
            asyncController.addUserForGroup1(response.getHeadquater(),name,email,phn,
                    password,selected_groupId,response.getUserId(),""+user_type);
        }

    }

    private boolean checkConditin()
    {
        if(!Util.isValidEmail(edit_email.getText()))
        {
            Util.showLongToast(this,"Email is not valid");
            return false;
        }

        else if(edit_password.getText()!=null) {
            if (edit_password.getText().toString().length() < 2) {
                Util.showLongToast(this, "Password must be greater then 3");
                return false;
            }
        }
        else if(!Util.chechTextValue(edit_name))
        {
            Util.showShortToast(this,"Enter name");
            return false;
        }
        else if(!Util.chechEditTextValue(edit_phn))
        {
            Util.showShortToast(this,"Enter phone number");
            return false;
        }
        else if(selected_groupId==null)
        {
            Util.showLongToast(this,"No Headquater is selected or available");
            return false;
        }
        return true;
    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if (response != null) {
            if (callType == CallType.GET_HQ_LIST) {
                Gson gson=new Gson();
                HeadQuaterGroupResponse response1=gson.fromJson(response,HeadQuaterGroupResponse.class);

                if (response1 != null) {
                    if (response1.getGrouplist() != null) {
                        Log.e("String", "value");
                        headquaterlist.clear();
                        headquaterlist.addAll(response1.getGrouplist());
                        headQuaterNames.clear();
                        for (int i = 0; i < headquaterlist.size(); i++) {
                            if(headquateruserlist!=null)
                            {
                                if(headquaterlist.get(i).getId().equalsIgnoreCase(headquateruserlist.getGroupId()))
                                {
                                    headQuaterNames.add(0,headquaterlist.get(i).getName());
                                }
                                else {
                                    headQuaterNames.add(headquaterlist.get(i).getName());
                                }
                            }
                              else {
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


            }
            else {
                Gson gson = new Gson();
                Result response1 = gson.fromJson(response, Result.class);
                Util.showLongToast(this,response1.getMessage());
                finish();

            }
        }
    }
}
