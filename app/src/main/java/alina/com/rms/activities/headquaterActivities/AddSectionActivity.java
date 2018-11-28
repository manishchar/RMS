package alina.com.rms.activities.headquaterActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
import alina.com.rms.model.Sectionlist;
import alina.com.rms.model.Userlist;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class AddSectionActivity extends HeaduaterBaseActivity implements AdapterView.OnItemSelectedListener,AsyncCompleteListner{
    /*List<String> countryNames=new ArrayList<String>();
    List<String> keyValues=new ArrayList<String>();*/
   //int flags[] = {R.drawable.round1, R.drawable.round3, R.drawable.round4, R.drawable.round1, R.drawable.round3, R.drawable.round4};
    CustomAdapter customAdapter,customAdapter1;
    RelativeLayout date_layout;
    private List<LoginResultPojo> loginResultPojo;
    private LoginResultPojo loginResultPojo1;
    private String login_response;
    private EditText targetEditText,achiveEditText,rkmEditText,tkmEditText;
    private List<Grouplist> headquaterlist=new ArrayList<Grouplist>();
    private Button submitBtn;
    Userlist headquateruserlist;
    String [] agencyArray={"KPTL","EMC","L&T","COBRA","CEC","BCPL","KYTX","OIL","KAYTX","SIPS","Termtd","Zapdor","GIPL","Sikka","Bright",
            "MCPL","Inabensa","CIMECHEL","TATA","KEC","Kailash","KANOHAR"};
    List<String>arrayList=new ArrayList<>();

    public String selected_groupId,selecte_agency;
    List<String> headQuaterNames=new ArrayList<String>();
    List<String> headQuaterId=new ArrayList<String>();
    String target;
    String name,crs="0",tkm="0",rkm="0";
    RadioButton r1,r2;
    Sectionlist sectionlist;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ohe);
        login_response= LoginDB.getLoginResponseAsJSON(AddSectionActivity.this);
       // Log.e("value",login_response);
        Gson gson = new Gson();
        Type type = new TypeToken<List<LoginResultPojo>>() {}.getType();
     /*   String json = gson.toJson(login_response, type);
        System.out.println(json);*/
      bundle=getIntent().getExtras();
     if(bundle!=null)
     {
         sectionlist=(Sectionlist)bundle.getSerializable("data");
         headingText.setText("Update Section");
     }
     else {
         headingText.setText("Add Section");
     }

        loginResultPojo = gson.fromJson(login_response, type);
        loginResultPojo1=loginResultPojo.get(0);
        //loginResultPojo =
        addView();
        initUI();
        getHeadquaterListFromServer();
    }

    @Override
    public void onResume()
    {
        super.onResume();


    }

    private void getHeadquaterListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(AddSectionActivity.this,this,CallType.GET_HQ_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getGroupList(loginResultPojo1.getHeadquater(),loginResultPojo1.getUserId());
    }


    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_add_section, null);
        setView(myView);
    }

    private void initUI() {


        Spinner spin1 = (Spinner) findViewById(R.id.spinner1);
        spin1.setOnItemSelectedListener(this);
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        //selected_date_text=(TextView)findViewById(R.id.selected_date_text);
        targetEditText=(EditText)findViewById(R.id.targetEditText);
        achiveEditText=(EditText)findViewById(R.id.achiveEditText);
        rkmEditText=(EditText)findViewById(R.id.rkmEditText);
        tkmEditText=(EditText)findViewById(R.id.tkmEditText);
        submitBtn=(Button)findViewById(R.id.submitBtn);
        r1=(RadioButton)findViewById(R.id.radioBtnTG);
        r2=(RadioButton)findViewById(R.id.radioBtnNTG);
        if (bundle!=null)
        {
            if(sectionlist.getTargete().equalsIgnoreCase("TG"))
            {
                r1.setChecked(true);
            }
            else {
                r2.setChecked(true);
            }

            if(sectionlist.getTkm()!=null)
            {
                tkmEditText.setText(sectionlist.getTkm());
            }
            if(sectionlist.getRkm()!=null)
            {
                rkmEditText.setText(sectionlist.getRkm());
            }

            achiveEditText.setText(sectionlist.getCrs());
            targetEditText.setText(sectionlist.getSection());
            for (int i=0;i<agencyArray.length;i++)
            {
                if(agencyArray[i].equalsIgnoreCase(sectionlist.getAgency()))
                {
                    arrayList.add(0,agencyArray[i]);
                }
                else {
                    arrayList.add(agencyArray[i]);
                }
            }

        }
        for (int i=0;i<agencyArray.length;i++)
        {

                arrayList.add(agencyArray[i]);
        }


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(r1.isChecked())
                {
                    target="TG";
                    Log.e("check","TG");
                }
                else {
                    target="NTG";
                }

                if(!Util.chechEditTextValue(targetEditText))
                {
                    Util.showShortToast(AddSectionActivity.this,"Enter Name");
                    return;
                }
              /*  else if(!Util.chechEditTextValue(achiveEditText))
                {
                    Util.showShortToast(AddSectionActivity.this,"Enter CRS");
                    return;
                }
                else if(!Util.chechEditTextValue(rkmEditText))
                {
                    Util.showShortToast(AddSectionActivity.this,"Enter RKM");
                    return;
                }
                else if(!Util.chechEditTextValue(tkmEditText))
                {
                    Util.showShortToast(AddSectionActivity.this,"Enter TKM");
                    return;
                }*/
               // loginResultPojo1.setTarget(achiveEditText.getText().toString());
                name=targetEditText.getText().toString();
                crs=achiveEditText.getText().toString();
                tkm=tkmEditText.getText().toString();
                rkm=rkmEditText.getText().toString();
                if(selecte_agency.trim().length()<1  || selected_groupId.length()<1)
                {

                    Util.showLongToast(AddSectionActivity.this,"Please select all fileds");
                    return;
                }

                if (bundle==null)
                {
                    submitValue();
                }
                else {
                    updateValue();
                }
            }
        });


        customAdapter=new CustomAdapter(getApplicationContext(),headQuaterNames);
        spin.setAdapter(customAdapter);
        customAdapter1=new CustomAdapter(getApplicationContext(),arrayList);
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
            Log.e("selected",headquaterlist.get(position).getId());
            selected_groupId=headQuaterId.get(position);
        }
        else {
            selecte_agency=arrayList.get(position);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void getOHETarget()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(AddSectionActivity.this,this, CallType.GET_OHE,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }


    public void submitValue()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(AddSectionActivity.this,this, CallType.SUBMITTED_VALUE,loginResultPojo1,true);
        asyncController.setProgressDialoug(true);
        asyncController.addSectionForHeadquater(loginResultPojo1.getHeadquater(),selected_groupId,name,selecte_agency
        ,crs,target,rkm,tkm);
    }
    public void updateValue()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(AddSectionActivity.this,this, CallType.SUBMITTED_VALUE,loginResultPojo1,true);
        asyncController.setProgressDialoug(true);
        asyncController.updateSectionForHeadquater(loginResultPojo1.getHeadquater(),selected_groupId,name,selecte_agency
                ,crs,target,rkm,tkm,sectionlist.getId());
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
                        // Log.e("value",""+headquaterlist.size());
                        customAdapter.notifyDataSetChanged();

                    } else {
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


            }  else if (callType == CallType.SUBMITTED_VALUE) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showShortToast(AddSectionActivity.this, "Data not saved successfully");
                        }
                        else {
                            Util.showShortToast(AddSectionActivity.this, "Data saved successfully");
                            finish();
                            //selected_date_text.setText("");

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Util.showShortToast(AddSectionActivity.this, "Error in connection");
            }
        }
    }


}
