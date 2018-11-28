package alina.com.rms.activities.headquaterActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.adaptor.CustomAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.Grouplist;
import alina.com.rms.model.HeadQuaterGroupResponse;
import alina.com.rms.model.LoginResultPojo;
import alina.com.rms.model.Result;
import alina.com.rms.model.Sectionlist;
import alina.com.rms.model.Userlist;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class AddMasterActivity extends HeaduaterBaseActivity implements AdapterView.OnItemSelectedListener,AsyncCompleteListner{

    CustomAdapter customAdapter,customAdapter1;
    private List<LoginResultPojo> loginResultPojo;
    private LoginResultPojo loginResultPojo1;
    private String login_response;
    private EditText nametEditText;
    private List<Grouplist> headquaterlist=new ArrayList<Grouplist>();
    private Button submitBtn;
    Userlist headquateruserlist;
    private List<Sectionlist> sectionlist1=new ArrayList<Sectionlist>();
    List<String> sectionNames=new ArrayList<String>();
    List<String> sectionID=new ArrayList<String>();
    public String selected_groupId,selecte_agency;
    List<String> headQuaterNames=new ArrayList<String>();
    List<String> headQuaterId=new ArrayList<String>();
    String type;
    String name;
    RadioButton r1,r2,r3;
    Sectionlist sectionlist;
    String selected_section="";
    Bundle bundle;
    int screenposition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ohe);

         bundle=getIntent().getExtras();
        if(bundle.containsKey("screenPosition"))
        {
            screenposition=bundle.getInt("screenPosition");
        }


        if(screenposition==0)
        {
            type="SP";
        }
        else if(screenposition==1)
        {
            type="SSP";
        }
        else if(screenposition==2)
        {
            type="TSS";
        }


        login_response= LoginDB.getLoginResponseAsJSON(AddMasterActivity.this);
       // Log.e("value",login_response);
        Gson gson = new Gson();
        Type type = new TypeToken<List<LoginResultPojo>>() {}.getType();
     /*   String json = gson.toJson(login_response, type);
        System.out.println(json);*/
      bundle=getIntent().getExtras();
     if(bundle!=null)
     {
         sectionlist=(Sectionlist)bundle.getSerializable("data");


     }

     if(sectionlist==null)
     {
         headingText.setText("Add "+this.type);
     }
     else {
         headingText.setText("Update "+this.type);
     }

        loginResultPojo = gson.fromJson(login_response, type);
        loginResultPojo1=loginResultPojo.get(0);
        //loginResultPojo =
        addView();
        initUI();
        getGroupListFromServer();
    }

    @Override
    public void onResume()
    {
        super.onResume();


    }

    private void getGroupListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(AddMasterActivity.this,this,CallType.GET_HQ_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getGroupList(loginResultPojo1.getHeadquater(),loginResultPojo1.getUserId());
    }


    private void getSectionListFromServer(String selected_group1)
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(this,this, CallType.GET_SECTION_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getHeadquaterSectionList(loginResultPojo1.getHeadquater(),selected_group1);

        // GET_USER_LIST
    }

    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_add_master, null);
        setView(myView);
    }

    private void initUI() {


        Spinner spin1 = (Spinner) findViewById(R.id.spinner1);
        spin1.setOnItemSelectedListener(this);
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        //selected_date_text=(TextView)findViewById(R.id.selected_date_text);
        nametEditText=(EditText)findViewById(R.id.nameEditText);
        submitBtn=(Button)findViewById(R.id.submitBtn);
        r1=(RadioButton)findViewById(R.id.radioBtnSP);
        r2=(RadioButton)findViewById(R.id.radioBtnSSp);
        r3=(RadioButton)findViewById(R.id.radioBtnTSS);
        if (bundle!=null && sectionlist!=null)
        {

            if(sectionlist.getTss_name()!=null)
            {
                nametEditText.setText(sectionlist.getTss_name());
            }
            else if(sectionlist.getSsp_name()!=null)
            {
                nametEditText.setText(sectionlist.getSsp_name());
            }
            else if(sectionlist.getSp_name()!=null)
            {
                nametEditText.setText(sectionlist.getSp_name());
            }


        }



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selected_groupId.trim().length()<1)
                {
                    Util.showShortToast(AddMasterActivity.this,"PLease select group");
                    return;
                }
                else if(selected_section.trim().length()<1)
                {
                    Util.showShortToast(AddMasterActivity.this,"PLease select section");
                    return;
                }

                else if(!Util.chechEditTextValue(nametEditText))
                {
                    Util.showShortToast(AddMasterActivity.this,"Enter Name");
                    return;
                }

                name=nametEditText.getText().toString();

                if (bundle!=null && sectionlist!=null)
                {
                    updateValue();

                }
                else {
                    submitValue();
                }
            }
        });


        customAdapter=new CustomAdapter(getApplicationContext(),headQuaterNames);
        spin.setAdapter(customAdapter);
        customAdapter1=new CustomAdapter(getApplicationContext(),sectionNames);
        spin1.setAdapter(customAdapter1);

    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        //Toast.makeText(getApplicationContext(), countryNames.get(position), Toast.LENGTH_LONG).show();
       // Log.e("Key Values",keyValues.get(position));
        //loginResultPojo1.setOhetype(keyValues.get(position));
        if(arg0.getId()==R.id.spinner)
        {
            //Log.e("selected",headquaterlist.get(position).getId());
            selected_groupId=headQuaterId.get(position);
            getSectionListFromServer(selected_groupId);
        }
        else {
            selected_section=sectionID.get(position);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void getOHETarget()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(AddMasterActivity.this,this, CallType.GET_OHE,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }


    public void submitValue()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(AddMasterActivity.this,this, CallType.SUBMITTED_VALUE,loginResultPojo1,true);
        asyncController.setProgressDialoug(true);
        asyncController.addMasterForHeadquater(loginResultPojo1.getHeadquater(),selected_groupId,name,selected_section,
        type);
    }
    public void updateValue()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(AddMasterActivity.this,this, CallType.SUBMITTED_VALUE,loginResultPojo1,true);
        asyncController.setProgressDialoug(true);
        asyncController.updateMasterForHeadquater(loginResultPojo1.getHeadquater(),selected_groupId,name,selected_section,
                type,sectionlist.getId());
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
                        headQuaterId.clear();

                        for (int i = 0; i < headquaterlist.size(); i++) {
                            if(sectionlist!=null)
                            {
                                if(headquaterlist.get(i).getId().equalsIgnoreCase(sectionlist.getGroupId()))
                                {
                                    headQuaterNames.add(0,headquaterlist.get(i).getName());
                                    headQuaterId.add(0,headquaterlist.get(i).getId());
                                }
                                else {
                                    headQuaterNames.add(headquaterlist.get(i).getName());
                                    headQuaterId.add(headquaterlist.get(i).getId());
                                }
                            }
                            else {
                                headQuaterNames.add(headquaterlist.get(i).getName());
                                headQuaterId.add(headquaterlist.get(i).getId());
                            }
                        }
                        if(headQuaterNames.size()>0)
                        {
                            selected_groupId=headQuaterId.get(0);
                        }
                        // Log.e("value",""+headquaterlist.size());
                        customAdapter.notifyDataSetChanged();

                    }
                    else {
                        selected_groupId="";
                        headquaterlist.clear();
                        headQuaterNames.clear();
                        headQuaterId.clear();
                        customAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                    }
                } else {
                    selected_groupId="";
                    headquaterlist.clear();
                    headQuaterNames.clear();
                    headQuaterId.clear();
                    customAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                }


            }
            else if(callType==CallType.GET_SECTION_LIST)
            {
                Gson gson=new Gson();
                Result response1=gson.fromJson(response,Result.class);

                if(response1!=null)
                {
                    if(response1.getSectionlist()!=null)
                    {
                        Log.e("String","value");
                        sectionNames.clear();
                        sectionID.clear();
                        sectionlist1.clear();
                        sectionlist1.addAll(response1.getSectionlist());
                        Log.e("value",""+sectionlist1.size());
                        for (int i=0;i<sectionlist1.size();i++)
                        {
                           // sectionNames.add(sectionlist1.get(i).getSection());

                            if(sectionlist!=null)
                            {
                                if(sectionlist1.get(i).getId().equalsIgnoreCase(sectionlist.getSection_id()))
                                {
                                    sectionNames.add(0,sectionlist1.get(i).getSection());
                                    sectionID.add(0,sectionlist1.get(i).getId());
                                }
                                else {
                                    sectionNames.add(sectionlist1.get(i).getSection());
                                    sectionID.add(sectionlist1.get(i).getId());
                                }
                            }
                            else {
                                sectionNames.add(sectionlist1.get(i).getSection());
                                sectionID.add(sectionlist1.get(i).getId());
                            }
                        }
                        customAdapter1.notifyDataSetChanged();
                        if(sectionlist1.size()>0)
                        {
                            selected_section=sectionlist1.get(0).getId();
                        }



                    }
                    else {
                        selected_section="";
                        sectionNames.clear();
                        sectionlist1.clear();
                        customAdapter1.notifyDataSetChanged();
                        Toast.makeText(this,response1.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    selected_section="";
                    sectionNames.clear();
                    sectionlist1.clear();
                    customAdapter1.notifyDataSetChanged();
                    Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                }


            }


            else if (callType == CallType.SUBMITTED_VALUE) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showShortToast(AddMasterActivity.this, "Data not saved successfully");
                        }
                        else {
                            Util.showShortToast(AddMasterActivity.this, "Data saved successfully");
                            finish();
                            //selected_date_text.setText("");

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Util.showShortToast(AddMasterActivity.this, "Error in connection");
            }
        }
    }


}
