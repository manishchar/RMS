package alina.com.rms.controller;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alina.com.rms.R;
import alina.com.rms.app.AppController;
import alina.com.rms.model.GetActivityDatum;
import alina.com.rms.model.GetFoundationDatum;
import alina.com.rms.model.GetMastDatum;
import alina.com.rms.model.GetWiringDatum;
import alina.com.rms.model.LoginDto;
import alina.com.rms.model.LoginResultPojo;
import alina.com.rms.util.AppHelper;
import alina.com.rms.util.CallType;
import alina.com.rms.util.URL;
import alina.com.rms.util.Util;

import static alina.com.rms.util.URL.url;

/**
 * Created by HP on 02-01-2018.
 */

public class AsyncController implements Response.ErrorListener {

    // Tag used to cancel the request
    String tag_string_req = "string_req";
    String TAG = AsyncController.class.getName();//"Response";
    Dialog openDialog;
    Context mContext;
    AsyncCompleteListner asyncCompleteListner;
    CallType callType;
    Object object;
    boolean flag;

    public AsyncController(Context mContext, AsyncCompleteListner asyncCompleteListner, CallType callType, Object object, boolean flag) {
        this.mContext = mContext;
        this.asyncCompleteListner = asyncCompleteListner;
        this.callType = callType;
        this.object = object;
        this.flag = false;

    }

    public void setProgressDialoug(boolean flag) {
        flag = false;
        if (flag) {
            AppCompatActivity appCompatActivity = (AppCompatActivity)mContext;
            openDialog = new Dialog(appCompatActivity);
            openDialog.setContentView(R.layout.progress_dialoug);
            // openDialog.setTitle("Custom Dialog Box");
            WebView webviewSpinner = (WebView) openDialog.findViewById(R.id.webView);
            webviewSpinner.setWebViewClient(new MyBrowser());
            webviewSpinner.getSettings().setJavaScriptEnabled(true);
            webviewSpinner.setBackgroundColor(Color.TRANSPARENT);
            webviewSpinner.setScrollContainer(false);
            webviewSpinner.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            webviewSpinner.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webviewSpinner.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webviewSpinner.loadDataWithBaseURL("file:///android_asset/", Util.htmlString, "text/html", "utf-8", "");
            openDialog.setCancelable(false);
            // disable scroll on touch
            webviewSpinner.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return (event.getAction() == MotionEvent.ACTION_MOVE);
                }
            });
            openDialog.show();
        }

    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public void loginRequest() {
        final LoginDto loginDto = (LoginDto) object;

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.login,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("email", loginDto.getEmail());
                Log.e("password", loginDto.getPassword());
                params.put("email", loginDto.getEmail());
                params.put("password", loginDto.getPassword());

                return params;
            }

        };

// Adding request to request queue
        setTimeOutTime(jsonObjReq);

    }


    public void getOHERequest() {
        String fun_name = "";
        if (callType == CallType.GET_OHE) {
            fun_name = URL.getOhe;
        } else if (callType == CallType.GET_SP_TARGET) {
            fun_name = URL.getSPOhe;
        } else if (callType == CallType.GET_TSS_TARGET) {
            fun_name = URL.getTSSTarget;
        }
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + fun_name, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this);
        setTimeOutTime(jsonObjReq);

// Adding request to request queue
        //  AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void getTargetValueRequest() {
        String funName = "";
        if (callType == CallType.GET_TARGET_VALUE) {
            funName = URL.getTarget;
        } else if (callType == CallType.GET_SP_TARGET_VALUE) {
            funName = URL.getSPTarget;
        } else if (callType == CallType.GET_SSP_TARGET_VALUE) {
            funName = URL.getSSPTarget;
        } else if (callType == CallType.GET_TSS_TARGET_VALUE) {
            funName = URL.getTSSTargetValue;
        }

        Log.isLoggable("Volley", Log.VERBOSE);
        final LoginResultPojo loginDto = (LoginResultPojo) object;
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + funName,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater", loginDto.getHeadquater());
                params.put("group", loginDto.getGroup());
                params.put("date", loginDto.getDate());
                params.put("ohetype", loginDto.getOhetype());
                params.put("section_id", loginDto.getSectionId());
                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void saveTargetValueRequest() {

        String funName = "";
        if (callType == CallType.SUBMITTED_VALUE) {
            funName = URL.submit_ohetargetvalue;
        } else if (callType == CallType.SAVE_SP_TARGET_VALUE) {
            funName = URL.submit_SPtargetvalue;
        } else if (callType == CallType.SAVE_SSP_TARGET_VALUE) {
            funName = URL.submit_SSPtargetvalue;
        } else if (callType == CallType.SAVE_TSS_TARGET_VALUE) {
            funName = URL.submit_TSSStargetvalue;
        }

        Log.isLoggable("Volley", Log.VERBOSE);
        final LoginResultPojo loginDto = (LoginResultPojo) object;
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + funName,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
/*                Log.e("headquater", loginDto.getHeadquater());
                Log.e("group", loginDto.getGroup());
                Log.e("date", loginDto.getDate());
                Log.e("ohetype", loginDto.getOhetype());
                Log.e("target", loginDto.getTarget());
                Log.e("user_id", loginDto.getUserId());*/


                params.put("headquater", loginDto.getHeadquater());
                params.put("group", loginDto.getGroup());
                params.put("date", loginDto.getDate());
                params.put("ohetype", loginDto.getOhetype());
                params.put("target", loginDto.getTarget());
                params.put("user_id", loginDto.getUserId());
                params.put("section_id", loginDto.getSectionId());
                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void recoveryPasswordRequest(final String email) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.recovery_password,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };

        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void getHQList() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_headquater, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this);

        setTimeOutTime(jsonObjReq);
    }


    public void addHeadQuaterForSuperAdmin(final String headquaterName) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.add_headquater,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater", headquaterName);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void updateHeadQuaterForSuperAdmin(final String headquaterName, final String id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.update_headquater,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater", headquaterName);
                params.put("id", id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void deleteHeadQuaterForSuperAdmin(final String id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.delete_headquater,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void deleteUserForSuperAdmin(final String id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.delete_admin_user,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void getSuperAdminUserList() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_super_admin_user_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this);

        setTimeOutTime(jsonObjReq);
    }


    public void addUserForSuperAdmin(final String headquaterName, final String name, final String email
            , final String mobile, final String password) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.add_super_admin_user,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater", headquaterName);
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("password", password);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void updateUserForSuperAdmin(final String headquaterName, final String name, final String email
            , final String mobile, final String password, final String id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.update_super_admin_user,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("headquater", headquaterName);
                Log.e("name", name);
                Log.e("email", email);
                Log.e("mobile", mobile);
                Log.e("password", password);
                Log.e("id", id);

                params.put("headquater", headquaterName);
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("password", password);
                params.put("id", id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    ///Group

    public void getUser(final String user_id, final String group) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.get_group,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", user_id);
                params.put("group", group);

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void addUserForGroup(final String headquaterName, final String name, final String email
            , final String mobile, final String password, final String user_id, final String group) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.add_group,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater", headquaterName);
                params.put("userid", user_id);
                params.put("group", group);
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("password", password);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void updateUserForGroup(final String headquaterName, final String name, final String email
            , final String mobile, final String password, final String user_id, final String group) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.update_group,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("headquater", headquaterName);
                Log.e("id", user_id);
                Log.e("group", group);
                Log.e("name", name);
                Log.e("email", email);
                Log.e("mobile", mobile);
                Log.e("password", password);


                params.put("headquater", headquaterName);
                params.put("id", user_id);
                params.put("group", group);
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("password", password);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void deleteUserForGroup(final String id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.delete_group,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };

        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void getHQListForGroup(final String id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_hq_list_group, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater_id", id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }


    public void getGroupList(final String headquater, final String userid) {
        Log.e("Url", URL.url + URL.get_group_list_headquater);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_group_list_headquater, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("headquater", headquater);
                Log.e("userid", userid);

                params.put("headquater", headquater);
                params.put("userid", userid);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }

    public void getGroupList1(final String headquater, final String userid) {
        Log.e("Url", URL.url + URL.get_new_group_list_headquater);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_new_group_list_headquater, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("headquater", headquater);
                Log.e("userid", userid);

                params.put("headquater", headquater);
                params.put("userid", userid);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }


    public void getGroupListForEntryUser(final String headquater, final String userid) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_group_list_entry_user, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater", headquater);
                params.put("userid", userid);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }

    public void addGroupForHeadquater(final String headquaterName, final String userid, final String name) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.add_group_headquater,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater", headquaterName);
                params.put("userid", userid);
                params.put("name", name);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };

        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void updateGroupForHeadQuater(final String headquaterName, final String id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.update_group_headquater,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", headquaterName);
                params.put("id", id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void deleteGroupForHeadQuater(final String id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.delete_group_headquater,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getHeadquaterUserList(final String id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_head_quater_user_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };

        setTimeOutTime(jsonObjReq);
    }


    ////headquarter, userid, group, name, email, mobile, password

    public void addUserForGroup1(final String headquaterName, final String name, final String email,
                                 final String mobile, final String password,
                                 final String group, final String userid, final String usertype) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.add_group_user,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

/*                Log.e("Headquater", headquaterName);
                Log.e("name", name);
                Log.e("email", email);
                Log.e("mobile", mobile);
                Log.e("password", password);
                Log.e("group", group);
                Log.e("userid",userid);*/


                params.put("headquater", headquaterName);
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("password", password);
                params.put("group", group);
                params.put("userid", userid);
                params.put("usertype", usertype);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };

        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void updateUserForGroup1(final String headquaterName, final String name, final String email,
                                    final String mobile, final String password,
                                    final String group, final String userid, final String usertype) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.update_group_user,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater", headquaterName);
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("password", password);
                params.put("group", group);
                params.put("id", userid);
                params.put("usertype", usertype);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void deleteUserForGroup1(final String id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.delete_group_user,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void getHeadquaterSectionList(final String headquater_id, final String group_id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_head_quater_section_list_for_group, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("headquater_id", headquater_id);
                Log.e("group_id", group_id);

                params.put("headquater_id", headquater_id);
                params.put("group_id", group_id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }

    public void getHeadquaterSectionList1(final String headquater_id, final String group_id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_head_quater_section_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("headquater_id", headquater_id);
                Log.e("group_id", group_id);

                params.put("headquater_id", headquater_id);
                params.put("group_id", group_id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }

    public void deleteSectionForHeadquater(final String id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.delete_headquater_section,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void addSectionForHeadquater(final String headquater, final String group, final String name,
                                        final String agency, final String crs, final String targete
            , final String rkm, final String tkm) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.add_section_headquater,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("headquater", headquater);
                Log.e("group", group);
                Log.e("name", name);
                Log.e("agency", agency);
                Log.e("crs", crs);
                Log.e("targete", targete);
                Log.e("tkm", tkm);
                Log.e("rkm", rkm);

                params.put("headquater", headquater);
                params.put("group", group);
                params.put("name", name);
                params.put("agency", agency);
                params.put("crs", crs);
                params.put("target", targete);
                params.put("rkm", rkm);
                params.put("tkm", tkm);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };


        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void updateSectionForHeadquater(final String headquater, final String group, final String name, final String agency,
                                           final String crs, final String targete
            , final String rkm, final String tkm, final String id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.update_section_headquater,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("id", id);
                Log.e("headquater", headquater);
                Log.e("group", group);
                Log.e("name", name);
                Log.e("agency", agency);
                Log.e("crs", crs);
                Log.e("targete", targete);
                Log.e("tkm", tkm);
                Log.e("rkm", rkm);

                params.put("id", id);
                params.put("headquater", headquater);
                params.put("group", group);
                params.put("name", name);
                params.put("agency", agency);
                params.put("crs", crs);
                params.put("target", targete);
                params.put("rkm", rkm);
                params.put("tkm", tkm);
                //params.put("password", loginDto.getPassword());

                return params;
            }
        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void getValueForHeadquater(final String headquater_id, final String group_id, final String section_id
            , final String month
            , final String year) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.get_target_value_headquater,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("headquater_id", headquater_id);
                Log.e("group_id", group_id);
                Log.e("section_id", section_id);
                Log.e("month", month);
                Log.e("year", year);

                params.put("headquater_id", headquater_id);
                params.put("group_id", group_id);
                params.put("section_id", section_id);
                params.put("month", month);
                params.put("year", year);
                //params.put("password", loginDto.getPassword());

                return params;
            }
        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void setTargetValueForHeadquater(final String headquater_id, final String group_id, final String section_id,
                                            final String month, final String year,
                                            final String eigdate, final String crsdate
            , final List<String> keyValues, final List<String> pairValues) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.set_ohetarget,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater_id", headquater_id);
                params.put("group_id", group_id);
                params.put("section_id", section_id);
                params.put("month", month);
                params.put("year", year);
                params.put("eigdate", eigdate);
                params.put("crsdate", crsdate);

                for (int i = 0; i < keyValues.size(); i++) {
                    params.put(keyValues.get(i), pairValues.get(i));
                    Log.e(keyValues.get(i), pairValues.get(i));
                }
                Log.e("headquater_id", headquater_id);
                Log.e("group_id", group_id);
                Log.e("section_id", section_id);
                Log.e("month", month);
                Log.e("year", year);
                Log.e("eigdate", eigdate);
                Log.e("crsdate", crsdate);

                //params.put("password", loginDto.getPassword());

                return params;
            }
        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void getValueForSPHeadquater(final String headquater_id, final String group_id, final String section_id
            , final String month
            , final String year, final String sp_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.get_sp_target_value_headquater,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("headquater_id", headquater_id);
                Log.e("group_id", group_id);
                Log.e("section_id", section_id);
                Log.e("month", month);
                Log.e("year", year);
                Log.e("sp_id", sp_id);

                params.put("headquater_id", headquater_id);
                params.put("group_id", group_id);
                params.put("section_id", section_id);
                params.put("month", month);
                params.put("year", year);
                params.put("sp_id", sp_id);
                //params.put("password", loginDto.getPassword());

                return params;
            }
        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    //URL.set_sp_target
    public void setSPTargetValueForHeadquater(final String headquater_id, final String group_id, final String section_id,
                                              final String month, final String year,
                                              final List<String> keyValues_civil, final List<String> pairValues_civil,
                                              final String sp_id
    ) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.set_sp_target,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater_id", headquater_id);
                params.put("group_id", group_id);
                params.put("section_id", section_id);
                params.put("month", month);
                params.put("year", year);
                params.put("sp_id", sp_id);

                for (int i = 0; i < keyValues_civil.size(); i++) {
                    params.put(keyValues_civil.get(i), pairValues_civil.get(i));
                    Log.e(keyValues_civil.get(i), pairValues_civil.get(i));
                }

                Log.e("headquater_id", headquater_id);
                Log.e("group_id", group_id);
                Log.e("section_id", section_id);
                Log.e("month", month);
                Log.e("year", year);
                Log.e("sp_id", sp_id);

                //params.put("password", loginDto.getPassword());

                return params;
            }
        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void getValueForTSSHeadquater(final String headquater_id, final String group_id, final String section_id
            , final String month
            , final String year, final String tss_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.get_tss_target_value_headquater,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("headquater_id", headquater_id);
                Log.e("group_id", group_id);
                Log.e("section_id", section_id);
                Log.e("month", month);
                Log.e("year", year);
                Log.e("tss_id", tss_id);

                params.put("headquater_id", headquater_id);
                params.put("group_id", group_id);
                params.put("section_id", section_id);
                params.put("month", month);
                params.put("year", year);
                params.put("tss_id", tss_id);

                //params.put("password", loginDto.getPassword());

                return params;
            }
        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    //  URL.set_tss_target
    public void setTSSTargetValueForHeadquater(final String headquater_id, final String group_id, final String section_id,
                                               final String month, final String year,
                                               final List<String> keyValues_civil, final List<String> pairValues_civil, final List<String> keyValues_electrical,
                                               final List<String> pairValues_elactrical,
                                               final String tss_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.set_tss_target,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater_id", headquater_id);
                params.put("group_id", group_id);
                params.put("section_id", section_id);
                params.put("month", month);
                params.put("year", year);
                params.put("tss_id", tss_id);


                for (int i = 0; i < keyValues_civil.size(); i++) {
                    params.put(keyValues_civil.get(i), pairValues_civil.get(i));
                    Log.e(keyValues_civil.get(i), pairValues_civil.get(i));
                }
                for (int i = 0; i < keyValues_electrical.size(); i++) {
                    params.put(keyValues_electrical.get(i), pairValues_elactrical.get(i));
                    Log.e(keyValues_electrical.get(i), pairValues_elactrical.get(i));
                }

                Log.e("headquater_id", headquater_id);
                Log.e("group_id", group_id);
                Log.e("section_id", section_id);
                Log.e("month", month);
                Log.e("year", year);
                Log.e("tss_id", tss_id);


                //params.put("password", loginDto.getPassword());

                return params;
            }
        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void getValueForSSPHeadquater(final String headquater_id, final String group_id, final String section_id
            , final String month
            , final String year, final String ssp_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.get_ssp_target_value_headquater,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("headquater_id", headquater_id);
                Log.e("group_id", group_id);
                Log.e("section_id", section_id);
                Log.e("month", month);
                Log.e("year", year);
                Log.e("ssp_id", ssp_id);
                params.put("headquater_id", headquater_id);
                params.put("group_id", group_id);
                params.put("section_id", section_id);
                params.put("month", month);
                params.put("year", year);
                params.put("ssp_id", ssp_id);
                //params.put("password", loginDto.getPassword());

                return params;
            }
        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    // set_ssp_target
    public void setSSPTargetValueForHeadquater(final String headquater_id, final String group_id, final String section_id,
                                               final String month, final String year,
                                               final List<String> keyValues_civil, final List<String> pairValues_civil
            , final String ssp_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.set_ssp_target,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater_id", headquater_id);
                params.put("group_id", group_id);
                params.put("section_id", section_id);
                params.put("month", month);
                params.put("year", year);
                params.put("ssp_id", ssp_id);

                for (int i = 0; i < keyValues_civil.size(); i++) {
                    params.put(keyValues_civil.get(i), pairValues_civil.get(i));
                    Log.e(keyValues_civil.get(i), pairValues_civil.get(i));
                }

                Log.e("headquater_id", headquater_id);
                Log.e("group_id", group_id);
                Log.e("section_id", section_id);
                Log.e("month", month);
                Log.e("year", year);
                Log.e("ssp_id", ssp_id);

                //params.put("password", loginDto.getPassword());

                return params;
            }
        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void getTargetProcessValueRequest(final String headquater, final String group,
                                             final String section_id,
                                             final String date) {

        Log.isLoggable("Volley", Log.VERBOSE);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.getTarget,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater", headquater);
                params.put("group", group);
                params.put("section_id", section_id);
                params.put("date", date);

                Log.e("headquater", headquater);
                Log.e("group", group);
                Log.e("section_id", section_id);
                Log.e("date", date);
                // params.put("year", year);
                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void saveTargetProcessValueRequest(final String headquater, final String group,
                                              final String section_id,
                                              final String date, final String user_id,
                                              final List<String> keyValues, final List<String> pairValues, final String masterValue) {
        String funName = "";
        if (callType == CallType.SUBMITTED_VALUE) {
            funName = URL.submit_ohetargetvalue;
        } else if (callType == CallType.SAVE_SP_TARGET_VALUE) {
            funName = URL.submit_SPtargetvalue;
        } else if (callType == CallType.SAVE_SSP_TARGET_VALUE) {
            funName = URL.submit_SSPtargetvalue;
        } else if (callType == CallType.SAVE_TSS_TARGET_VALUE) {
            funName = URL.submit_TSSStargetvalue;
        }
        Log.isLoggable("Volley", Log.VERBOSE);
        final LoginResultPojo loginDto = (LoginResultPojo) object;
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + funName,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
/*                Log.e("headquater", loginDto.getHeadquater());
                Log.e("group", loginDto.getGroup());
                Log.e("date", loginDto.getDate());
                Log.e("ohetype", loginDto.getOhetype());
                Log.e("target", loginDto.getTarget());
                Log.e("user_id", loginDto.getUserId());*/


                params.put("headquater", headquater);
                params.put("group", group);
                params.put("section_id", section_id);
                params.put("date", date);
                params.put("user_id", user_id);
                if (masterValue != null) {
                    params.put("sp_id", masterValue);
                    params.put("ssp_id", masterValue);
                }

                for (int i = 0; i < keyValues.size(); i++) {
                    params.put(keyValues.get(i), pairValues.get(i));
                    Log.e(keyValues.get(i), pairValues.get(i));
                }
                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void saveTargetValueForProcess(final List<String> keyValues_civil, final List<String> pairValues_civil, final List<String> keyValues_electrical,
                                          final List<String> pairValues_elactrical, final String tss_id) {

        String funName = URL.submit_TSSStargetvalue;

        Log.isLoggable("Volley", Log.VERBOSE);
        final LoginResultPojo loginDto = (LoginResultPojo) object;
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + funName,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
/*                Log.e("headquater", loginDto.getHeadquater());
                Log.e("group", loginDto.getGroup());
                Log.e("date", loginDto.getDate());
                Log.e("ohetype", loginDto.getOhetype());
                Log.e("target", loginDto.getTarget());
                Log.e("user_id", loginDto.getUserId());*/


                params.put("headquater", loginDto.getHeadquater());
                params.put("group", loginDto.getGroup());
                params.put("date", loginDto.getDate());
                params.put("tss_id", tss_id);
/*                params.put("ohetype", loginDto.getOhetype());
                params.put("target", loginDto.getTarget());*/
                for (int i = 0; i < keyValues_civil.size(); i++) {
                    params.put(keyValues_civil.get(i), pairValues_civil.get(i));
                    Log.e(keyValues_civil.get(i), pairValues_civil.get(i));
                }
                for (int i = 0; i < keyValues_electrical.size(); i++) {
                    params.put(keyValues_electrical.get(i), pairValues_elactrical.get(i));
                    Log.e(keyValues_electrical.get(i), pairValues_elactrical.get(i));
                }
                params.put("user_id", loginDto.getUserId());
                params.put("section_id", loginDto.getSectionId());
                Log.e("tss_id", tss_id);
                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void getProcessTargetValueRequest() {
        String funName = "";
        if (callType == CallType.GET_TARGET_VALUE) {
            funName = URL.getTarget;
        } else if (callType == CallType.GET_SP_TARGET_VALUE) {
            funName = URL.getSPTarget;
        } else if (callType == CallType.GET_SSP_TARGET_VALUE) {
            funName = URL.getSSPTarget;
        } else if (callType == CallType.GET_TSS_TARGET_VALUE) {
            funName = URL.getTSSTargetValue;
        }

        Log.isLoggable("Volley", Log.VERBOSE);
        final LoginResultPojo loginDto = (LoginResultPojo) object;
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + funName,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("headquater", loginDto.getHeadquater());
                params.put("group", loginDto.getGroup());
                params.put("date", loginDto.getDate());
                params.put("section_id", loginDto.getSectionId());

                Log.e("headquater", loginDto.getHeadquater());
                Log.e("group", loginDto.getGroup());
                Log.e("date", loginDto.getDate());
                Log.e("section_id", loginDto.getSectionId());
                if (loginDto.getMaster_id() != null) {
                    Log.e("ssp_id", loginDto.getMaster_id());
                    params.put("tss_id", loginDto.getMaster_id());
                    params.put("sp_id", loginDto.getMaster_id());
                    params.put("ssp_id", loginDto.getMaster_id());
                }

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void addMasterForHeadquater(final String headquater, final String group, final String name,
                                       final String section_id, final String type) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.add_headquater_master,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("headquater", headquater);
                Log.e("group", group);
                Log.e("name", name);
                Log.e("section_id", section_id);
                Log.e("type", type);


                params.put("headquater", headquater);
                params.put("group", group);
                params.put("name", name);
                params.put("section_id", section_id);
                params.put("type", type);

                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getHeadquaterMasterList(final String headquater_id, String urlName) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + urlName, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("headquater_id", headquater_id);

                params.put("headquater_id", headquater_id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }


    public void getHeadquaterSP_SSP_TSSList(final String section_id, String urlName) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + urlName, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("section_id", section_id);

                params.put("section_id", section_id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }


    public void updateMasterForHeadquater(final String headquater, final String group, final String name,
                                          final String section_id, final String type, final String id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.update_master_name,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("hq_id", headquater);
                Log.e("group", group);
                Log.e("name", name);
                Log.e("section_id", section_id);
                Log.e("type", type);
                Log.e("id", id);

                params.put("headquater", headquater);
                params.put("group", group);
                params.put("name", name);
                params.put("section_id", section_id);
                params.put("type", type);
                params.put("id", id);

                //params.put("password", loginDto.getPassword());

                return params;
            }

        };

        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void deleteMasterForHeadquater(final String id, final String type) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.delete_master_name,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("id", id);
                Log.e("type", type);

                params.put("id", id);
                params.put("type", type);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void getHeadquaterSP_SSP_TSSListBYGRoupID(final String group_id, final String urlName) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + urlName, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("URL", URL.url + urlName);
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("group_id", group_id);

                params.put("group_id", group_id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }

    public void getOrgnaization() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_orgnaization_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this);
        setTimeOutTime(jsonObjReq);

    }

    public void getGroupListForPublicUser(final String headquater) {
        Log.e("Url", URL.url + URL.get_group_list);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_group_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e(" project_id", headquater);

                params.put(" project_id", headquater);

                return params;
            }

        };

        setTimeOutTime(jsonObjReq);
    }


    public void getSectionListForPublicUser(final String group_id, final String orgn_id) {
        Log.e("Url", URL.url + URL.get_section_list);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_section_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("group_id", group_id);

                params.put("group_id", group_id);
                params.put("orgn_id", orgn_id);

                return params;
            }

        };

        setTimeOutTime(jsonObjReq);
    }

    public void getStationList(final String section_id,final String type) {
        Log.e("Url", URL.url + URL.stationList);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.stationList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("section_id", section_id);

                params.put("section_id", section_id);
                params.put("type",type);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }

    public void getNAMEList(final String section_id,final String nameList) {
        Log.e("Url", URL.url + nameList);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + nameList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("section_id", section_id);

                params.put("section_id", section_id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }


    public void getNewStationList(final String section_id) {
        Log.e("Url", URL.url + URL.newstationList);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.newstationList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("sectionid", section_id);

                params.put("sectionid", section_id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }


    public void getDivisionList(final String section_name) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_division_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("section_name", section_name);
                params.put("section_name", section_name);


                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }


    public void getStateList(final String section_name, final String division) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_state_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("section_name", section_name);
                Log.e("division", division);
                params.put("section_name", section_name);
                params.put("division", division);


                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getZoneList(final String section_name, final String division) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_zone_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("section_name", section_name);
                params.put("section_name", section_name);
                params.put("division", division);


                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void setRKMForPublicUser(final String proj_name, final String group_name, final String section_name,
                                    final String zone,
                                    final String state, final String org_name, final String rkm,
                                    final String month, final String division) {
        Log.e("Url", URL.url + URL.set_RKM);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.set_RKM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("proj_name", proj_name);
                Log.e("group_name", group_name);
                Log.e("section_name", section_name);
                Log.e("zone", zone);
                Log.e("state", state);
                Log.e("org_name", org_name);
                Log.e("rkm", rkm);
                Log.e("month", month);
                Log.e("division", division);


                params.put("proj_name", proj_name);
                params.put("group_name", group_name);
                params.put("section_name", section_name);
                params.put("zone", zone);
                params.put("state", state);
                params.put("org_name", org_name);
                params.put("rkm", rkm);
                params.put("month", month);
                params.put("division", division);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }

    public void saveRKM(final String rkm_id, final String crs, final String month
            , final String crs_id) {
        Log.e("Url", URL.url + URL.set_RKM);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.set_RKM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("rkm_id", rkm_id);
                Log.e("crs", crs);
                Log.e("month", month);
                Log.e("crs_id", crs_id);
                params.put("rkm_id", rkm_id);
                params.put("crs", crs);
                params.put("month", month);
                params.put("crs_id", crs_id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }


    public void updateRKMForPublicUser(final String proj_id, final String proj_name, final String group_name, final String section_name,
                                       final String zone,
                                       final String state, final String org_name, final String rkm,
                                       final String month, final String division) {
        Log.e("Url", URL.url + URL.update_RKM);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.set_RKM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("proj_id", proj_id);
                Log.e("proj_name", proj_name);
                Log.e("group_name", group_name);
                Log.e("section_name", section_name);
                Log.e("zone", zone);
                Log.e("state", state);
                Log.e("org_name", org_name);
                Log.e("rkm", rkm);
                Log.e("month", month);
                Log.e("division", division);

                params.put("proj_id", proj_id);
                params.put("proj_name", proj_name);
                params.put("group_name", group_name);
                params.put("section_name", section_name);
                params.put("zone", zone);
                params.put("state", state);
                params.put("org_name", org_name);
                params.put("rkm", rkm);
                params.put("month", month);
                params.put("division", division);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }


    public void getPublicUserList(final String role, final String name, final String proj_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.get_public_module_list,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("role", role);
                Log.e("org_name", name);
                params.put("role", role);
                params.put("org_name", name);
                params.put("proj_id", proj_id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void getPublicUserList(final String role, final String name) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.get_public_module_list,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("role", role);
                Log.e("org_name", name);
                params.put("role", role);
                params.put("org_name", name);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void deletePublicModuleForSuperAdmin(final String id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.delete_pulic_module,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("crs_id", id);
                params.put("crs_id", id);
                //params.put("password", loginDto.getPassword());

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void getRKM(final String section_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.get_rkm, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("section_id", section_id);
                params.put("section_id", section_id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }


    public void getCRSList(final String crs_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.editCrs, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("crs_id", crs_id);
                params.put("crs_id", crs_id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }


    public void getProjectList() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.projectList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this);
        setTimeOutTime(jsonObjReq);
    }

    public void getProjectList1() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.projectList1, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this);
        setTimeOutTime(jsonObjReq);
    }

    public void getActivityList() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.activityList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this);
        setTimeOutTime(jsonObjReq);
    }

    public void getSPActivityList() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.getSPList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this);
        setTimeOutTime(jsonObjReq);
    }


    public void getTSSActivityList() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.getTSSList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this);
        setTimeOutTime(jsonObjReq);
    }


    public void getFoundationList(final String section_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.getFoundationData,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("station_id", section_id);
                params.put("station_id", section_id);

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void saveFoundationListItem(final List<GetFoundationDatum> getFoundationData,
                                       final String month, final String foundation_prog1__id
    ) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.addFoundationProgress,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // Log.e("section_id", section_id);
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArray1 = new JSONArray();
                String temp = "";
                String temp1 = "";
                String temp2 = "";
                for (GetFoundationDatum getFoundationDatum : getFoundationData) {
                    temp += getFoundationDatum.getFoundation() + ",";
                    temp1 += getFoundationDatum.getFoundation_prog_no() + ",";
                    temp2 += getFoundationDatum.getStationId() + ",";

                }
                temp = temp.substring(0, temp.length() - 1);
                temp1 = temp1.substring(0, temp1.length() - 1);
                temp2 = temp2.substring(0, temp2.length() - 1);
                params.put("foundation", temp);
                params.put("foundation_prog_no", temp1);
                params.put("station_id", temp2);
                params.put("foundation_prog1__id", foundation_prog1__id);
                params.put("month", month);
                params.put("foundation_id", getFoundationData.get(0).getFoundationId());
                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);


    }

    public void saveMastListItem(final List<GetMastDatum> getFoundationData,
                                 final String month, final String mast_prog_id
    ) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.addMastProgress,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // Log.e("section_id", section_id);
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArray1 = new JSONArray();
                String temp = "";
                String temp1 = "";
                String temp2 = "";
                for (GetMastDatum getFoundationDatum : getFoundationData) {
                    temp += getFoundationDatum.getMast() + ",";
                    temp1 += getFoundationDatum.getMast_prog_no() + ",";
                    temp2 += getFoundationDatum.getStationId() + ",";

                }
                temp = temp.substring(0, temp.length() - 1);
                temp1 = temp1.substring(0, temp1.length() - 1);
                temp2 = temp2.substring(0, temp2.length() - 1);
                params.put("mast", temp);
                params.put("mast_prog_no", temp1);
                params.put("station_id", temp2);
                params.put("mast_prog_id", mast_prog_id);
                params.put("month", month);
                params.put("mast_id", getFoundationData.get(0).getMastId());
                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void saveWiringProgressListItem(final List<GetWiringDatum> getFoundationData,
                                           final String month, final String mast_prog_id
    ) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.addWiringProgress,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // Log.e("section_id", section_id);
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArray1 = new JSONArray();
                String temp = "";
                String temp1 = "";
                String temp2 = "";
                for (GetWiringDatum getFoundationDatum : getFoundationData) {
                    temp += getFoundationDatum.getWiring() + ",";
                    temp1 += getFoundationDatum.getWiring_prog_no() + ",";
                    temp2 += getFoundationDatum.getStationId() + ",";

                }
                temp = temp.substring(0, temp.length() - 1);
                temp1 = temp1.substring(0, temp1.length() - 1);
                temp2 = temp2.substring(0, temp2.length() - 1);
                params.put("wiring", temp);
                params.put("wiring_prog_no", temp1);
                params.put("station_id", temp2);
                params.put("wiring_prog_id", mast_prog_id);
                params.put("month", month);
                params.put("wiring_id", getFoundationData.get(0).getWiringId());
                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void getFoundationProgressList() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.foundationProgressList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this);
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        setTimeOutTime(jsonObjReq);
    }

    public void getFoundationProgressList1(final String proj_id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.foundationProgressList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("proj_id", proj_id);
                params.put("proj_id", proj_id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }


    public void getMastErictionProgressList() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.mastErictionProgressList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this);
        setTimeOutTime(jsonObjReq);
    }

    public void getMastErictionProgressList1(final String proj_id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.mastErictionProgressList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("proj_id", proj_id);
                params.put("proj_id", proj_id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }

    public void getWiringProgressList1(final String proj_id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.wiringProgressList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("proj_id", proj_id);
                params.put("proj_id", proj_id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }

    public void getActivityProgressList(final String proj_id,final String role,
                                        final String user_id,final String name,
                                        final String user_type) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.activityProgressList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                /*Log.e("proj_id", proj_id);*/
                params.put("proj_id", proj_id);
                params.put("role", role);
                params.put("user_id", user_id);
                params.put("name", name);
                params.put("user_type", user_type);
                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }

    public void getSPActivityProgressList(final String proj_id,final String role,
                                          final String user_id,final String name,
                                          final String user_type) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.activityspProgressList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("proj_id", proj_id);
                Log.e("role", role);
                Log.e("user_id", user_id);
                Log.e("name", name);
                Log.e("user_type", user_type);

                params.put("proj_id", proj_id);
                params.put("role", role);
                params.put("user_id", user_id);
                params.put("name", name);
                params.put("user_type", user_type);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }

    public void getSSPActivityProgressList(final String proj_id,final String role,
                                           final String user_id,final String name,
                                           final String user_type) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.activitysspProgressList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("proj_id", proj_id);
                Log.e("role", role);
                Log.e("user_id", user_id);
                Log.e("name", name);
                Log.e("user_type", user_type);

                params.put("proj_id", proj_id);
                params.put("role", role);
                params.put("user_id", user_id);
                params.put("name", name);
                params.put("user_type", user_type);
                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }
    public void getTSSActivityProgressList(final String proj_id,final String role,
                                           final String user_id,final String name,
                                           final String user_type) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.tssActivityProgressList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("proj_id", proj_id);
                Log.e("role", role);
                Log.e("user_id", user_id);
                Log.e("name", name);
                Log.e("user_type", user_type);

                params.put("proj_id", proj_id);
                params.put("role", role);
                params.put("user_id", user_id);
                params.put("name", name);
                params.put("user_type", user_type);
                return params;
            }

        };
        setTimeOutTime(jsonObjReq);
    }

    public void getWiringProgressList() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.wiringProgressList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this);
        setTimeOutTime(jsonObjReq);
    }

    public void getMastErictionDataList(final String section_id) {
        Log.e("url", url + URL.getmastErictionData);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.getmastErictionData,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("station_id", section_id);
                params.put("station_id", section_id);

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void getWiringProgrssDataList(final String section_id) {
        Log.e("url", url + URL.getWiringData);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.getWiringData,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("station_id", section_id);
                params.put("station_id", section_id);

                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }


    public void getOHEProgrssDataList(final String section_id, final String type) {
        Log.e("url", url + URL.getActivityData);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url + URL.getActivityData,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("station_id", section_id);
                Log.e("type", type);
                params.put("station_id", section_id);
                params.put("type", type);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getProgrssDataList(final String section_id, final String type,final String callType) {
        Log.e("url", url + callType);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url +callType,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            onCompleteResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("station_id", section_id);
                Log.e("type", type);

                params.put("station_id", section_id);
                params.put("type", type);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getDeleteViewFoundation(final String foundation_prog1__id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.delete_foundation_prog, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("foundation_prog1__id", foundation_prog1__id);
                params.put("foundation_prog1__id", foundation_prog1__id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getDeleteViewMAstEriction(final String foundation_prog1__id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.delete_mastEriction_prog, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("mast_prog_id", foundation_prog1__id);
                params.put("mast_prog_id", foundation_prog1__id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getDeleteViewWiring(final String wiring_prog_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.delete_wiring_prog, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("wiring_prog_id", wiring_prog_id);
                params.put("wiring_prog_id", wiring_prog_id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getDeleteProgressActivity(final String wiring_prog_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.delete_activity_prog, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("wiring_prog_id", wiring_prog_id);
                params.put("wiring_prog_id", wiring_prog_id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getDeleteSPProgressActivity(final String wiring_prog_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.delete_sp_activity_prog, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("wiring_prog_id", wiring_prog_id);
                params.put("wiring_prog_id", wiring_prog_id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getDeleteSSPProgressActivity(final String wiring_prog_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.delete_ssp_activity_prog, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("wiring_prog_id", wiring_prog_id);
                params.put("wiring_prog_id", wiring_prog_id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getDeleteTSSProgressActivity(final String wiring_prog_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.delete_tss_activity_prog, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("wiring_prog_id", wiring_prog_id);
                params.put("wiring_prog_id", wiring_prog_id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getEditProgressViewFoundation(final String foundation_prog1__id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.editProgress, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("foundation_prog1__id", foundation_prog1__id);
                params.put("foundation_prog1__id", foundation_prog1__id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getEditProgressViewMAstEriction(final String foundation_prog1__id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.editMastProgress, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("foundation_prog1__id", foundation_prog1__id);
                params.put("mast_prog_id", foundation_prog1__id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getEditProgressViewWiring(final String wiring_prog_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.editWiringProgress, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("wiring_prog_id", wiring_prog_id);
                params.put("wiring_prog_id", wiring_prog_id);

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getEditProgressView(final String wiring_prog_id,
                                    final String type, final String station_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.editactivityProgress, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("wiring_prog_id", wiring_prog_id);
                params.put("wiring_prog_id", wiring_prog_id);
                Log.e("type", type);
                params.put("type", type);
/*                Log.e("station_id", station_id);
                params.put("station_id", station_id);*/

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }


    public void getEditTssProgressView(final String wiring_prog_id,
                                    final String type, final String station_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.edittssactivityProgress, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("wiring_prog_id", wiring_prog_id);
                params.put("wiring_prog_id", wiring_prog_id);
                Log.e("type", type);
                params.put("type", type);
/*                Log.e("station_id", station_id);
                params.put("station_id", station_id);*/

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getEditSPProgressView(final String wiring_prog_id,
                                       final String type, final String station_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.editspactivityProgress, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("wiring_prog_id", wiring_prog_id);
                params.put("wiring_prog_id", wiring_prog_id);
                Log.e("type", type);
                params.put("type", type);
/*                Log.e("station_id", station_id);
                params.put("station_id", station_id);*/

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void getEditSSPProgressView(final String wiring_prog_id,
                                      final String type, final String station_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL.url + URL.editsspactivityProgress, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    onCompleteResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("wiring_prog_id", wiring_prog_id);
                params.put("wiring_prog_id", wiring_prog_id);
                Log.e("type", type);
                params.put("type", type);
/*                Log.e("station_id", station_id);
                params.put("station_id", station_id);*/

                return params;
            }

        };
        setTimeOutTime(jsonObjReq);

    }

    public void saveProfileAccount(final List<GetFoundationDatum> getFoundationData,
                                   final String month, final String foundation_prog1__id) {
        // loading or check internet connection or something...
        // ... then
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                url + URL.addFoundationProgress, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.e("PALASH", resultResponse);
                try {
                    onCompleteResponse(resultResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, this) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("foundation", getFoundationData.get(0).getFoundation());
                params.put("foundation_prog_no", getFoundationData.get(0).getFoundation_prog_no());
                params.put("station_id", getFoundationData.get(0).getStationId());
                params.put("foundation_prog1__id", foundation_prog1__id);
                params.put("month", month);
                params.put("foundation_id", getFoundationData.get(0).getFoundationId());
                params.put("lat", getFoundationData.get(0).getLat());
                params.put("lng", getFoundationData.get(0).getLng());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("add_img", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(mContext, getFoundationData.get(0).getBitmap()), "image/jpeg"));
                //params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };
        setTimeOutForMultiPart(multipartRequest);
    }

    public void saveProfileAccount1(final List<GetMastDatum> getFoundationData,
                                    final String month, final String foundation_prog1__id) {
        // loading or check internet connection or something...
        // ... then
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                url + URL.addMastProgress, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.e("PALASH", resultResponse);
                try {
                    onCompleteResponse(resultResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, this) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mast", getFoundationData.get(0).getMast());
                Log.e("mast_prog_no", getFoundationData.get(0).getMast_prog_no());
                params.put("mast_prog_no", getFoundationData.get(0).getMast_prog_no());
                params.put("station_id", getFoundationData.get(0).getStationId());
 /*               params.put("mast_prog_id",getFoundationData.get(0).getMast_prog_id());*/
                params.put("month", month);
                params.put("mast_prog_id", foundation_prog1__id);
                params.put("mast_id", getFoundationData.get(0).getMastId());
                params.put("lat", getFoundationData.get(0).getLat());
                params.put("lng", getFoundationData.get(0).getLng());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("add_img", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(mContext, getFoundationData.get(0).getBitmap()), "image/jpeg"));
                //params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };
        setTimeOutForMultiPart(multipartRequest);
    }


    public void saveProfileAccount2(final List<GetWiringDatum> getFoundationData,
                                    final String month, final String foundation_prog1__id) {
        // loading or check internet connection or something...
        // ... then
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                url + URL.addWiringProgress, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.e("PALASH", resultResponse);
                try {
                    onCompleteResponse(resultResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, this) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("wiring", getFoundationData.get(0).getWiring());
                params.put("wiring_prog_no", getFoundationData.get(0).getWiring_prog_no());
                params.put("station_id", getFoundationData.get(0).getStationId());
                params.put("wiring_prog_id", foundation_prog1__id);
                params.put("wiring_prog1__id", foundation_prog1__id);
                params.put("month", month);
                params.put("wiring_id", getFoundationData.get(0).getWiringId());
                params.put("lat", getFoundationData.get(0).getLat());
                params.put("lng", getFoundationData.get(0).getLng());


                Log.e("wiring", getFoundationData.get(0).getWiring());
                Log.e("wiring_prog_no", getFoundationData.get(0).getWiring_prog_no());
                Log.e("station_id", getFoundationData.get(0).getStationId());
                Log.e("wiring_prog_id", foundation_prog1__id);
                Log.e("wiring_prog1__id", foundation_prog1__id);
                Log.e("month", month);
                Log.e("wiring_id", getFoundationData.get(0).getWiringId());
                Log.e("lat", getFoundationData.get(0).getLat());
                Log.e("lng", getFoundationData.get(0).getLng());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("add_img", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(mContext, getFoundationData.get(0).getBitmap()), "image/jpeg"));
                //params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };
        setTimeOutForMultiPart(multipartRequest);
    }


    public void saveProfileAccount3(final List<GetActivityDatum> getFoundationData,
                                    final String month, final String foundation_prog1__id, final String type) {
        // loading or check internet connection or something...
        // ... then
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                url + URL.addActivityProgress, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.e("PALASH", resultResponse);
                try {
                    onCompleteResponse(resultResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, this) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("wiring", getFoundationData.get(0).getActivity());
                params.put("wiring_prog_no", getFoundationData.get(0).get_prog_no());
                params.put("station_id", getFoundationData.get(0).getStationId());
                params.put("wiring_prog_id", foundation_prog1__id);
                params.put("wiring_prog1__id", foundation_prog1__id);
                params.put("month", month);
                params.put("wiring_id", getFoundationData.get(0).getId());
                params.put("lat", getFoundationData.get(0).getLat());
                params.put("lng", getFoundationData.get(0).getLng());
                params.put("type", type);
                params.put("user_id",getFoundationData.get(0).getUser_id());

                Log.e("wiring", getFoundationData.get(0).getActivity());
                Log.e("wiring_prog_no", getFoundationData.get(0).get_prog_no());
                Log.e("station_id", getFoundationData.get(0).getStationId());
                Log.e("wiring_prog_id", foundation_prog1__id);
                Log.e("wiring_prog1__id", foundation_prog1__id);
                Log.e("month", month);
                Log.e("wiring_id", getFoundationData.get(0).getId());
                Log.e("lat", getFoundationData.get(0).getLat());
                Log.e("lng", getFoundationData.get(0).getLng());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("add_img", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(mContext, getFoundationData.get(0).getBitmap()), "image/jpeg"));
                //params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };
        setTimeOutForMultiPart(multipartRequest);

    }

    public void saveProfileAccount(final List<GetActivityDatum> getFoundationData,
                                    final String month, final String foundation_prog1__id,
                                   final String type,final String urlLink,final String type_name) {
        // loading or check internet connection or something...
        Log.e("url",url+urlLink);
        // ... then
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                url + urlLink, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.e("PALASH", resultResponse);
                try {
                    onCompleteResponse(resultResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, this) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("wiring", getFoundationData.get(0).getActivity());
                params.put("wiring_prog_no", getFoundationData.get(0).get_prog_no());
                params.put("station_id", getFoundationData.get(0).getStationId());
                params.put("wiring_prog_id", foundation_prog1__id);
                params.put("wiring_prog1__id", foundation_prog1__id);
                params.put("month", month);
                params.put("wiring_id", getFoundationData.get(0).getId());
                params.put("lat", getFoundationData.get(0).getLat());
                params.put("lng", getFoundationData.get(0).getLng());
                params.put("type", type);
                params.put("user_id",getFoundationData.get(0).getUser_id());
                params.put(type_name,getFoundationData.get(0).getTss_name());

                Log.e("wiring", getFoundationData.get(0).getActivity());
                Log.e("wiring_prog_no", getFoundationData.get(0).get_prog_no());
                Log.e("station_id", getFoundationData.get(0).getStationId());
                Log.e("wiring_prog_id", foundation_prog1__id);
                Log.e("wiring_prog1__id", foundation_prog1__id);
                Log.e("month", month);
                Log.e("wiring_id", getFoundationData.get(0).getId());
                Log.e("lat", getFoundationData.get(0).getLat());
                Log.e("lng", getFoundationData.get(0).getLng());
                Log.e("type", type);
                Log.e("user_id",getFoundationData.get(0).getUser_id());
                Log.e(type_name,getFoundationData.get(0).getTss_name());
                Log.e("user_id",getFoundationData.get(0).getUser_id());
                Log.e(type_name,getFoundationData.get(0).getTss_name());

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("add_img", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(mContext, getFoundationData.get(0).getBitmap()), "image/jpeg"));
                //params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };
        setTimeOutForMultiPart(multipartRequest);

    }


    public void onCompleteResponse(String response) throws Exception {
        if (openDialog != null) {
            openDialog.dismiss();
        }
        if (response != null) {

            Object json = new JSONTokener(response).nextValue();
            if (json instanceof JSONObject) {
                //you have an object
                asyncCompleteListner.asyncCompleteListner(response, callType);
            } else if (json instanceof JSONArray) {
                //you have an array
                asyncCompleteListner.asyncCompleteListner(response, callType);
            } else {
                Util.showLongToast(mContext, "Error in server");

                asyncCompleteListner.asyncCompleteListner(null, callType);
                //asyncCompleteListner.asyncCompleteListner(null,callType);
            }
        } else {
            Util.showLongToast(mContext, "Error in server");
            asyncCompleteListner.asyncCompleteListner(null, callType);
        }
    }


    private void setTimeOutTime(StringRequest jsonObjReq) {
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }

    private void setTimeOutForMultiPart(VolleyMultipartRequest multipartRequest) {
        multipartRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                try {
                    onCompleteResponse(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        VolleySingleton.getInstance(mContext).addToRequestQueue(multipartRequest);
    }


    /**
     * Callback method that an error has been occurred with the
     * provided error code and optional user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        NetworkResponse networkResponse = error.networkResponse;
        String errorMessage = "Unknown error";

        if (networkResponse == null) {
            if (error.getClass().equals(TimeoutError.class)) {
                errorMessage = "Request timeout";
            } else if (error.getClass().equals(NoConnectionError.class)) {
                errorMessage = "Failed to connect server";
            }
        } else {
            String result = new String(networkResponse.data);
            try {
                JSONObject response = new JSONObject(result);
                String status = response.getString("status");
                String message = response.getString("message");

                Log.e("Error Status", status);
                Log.e("Error Message", message);

                if (networkResponse.statusCode == 404) {
                    errorMessage = "Resource not found";
                } else if (networkResponse.statusCode == 401) {
                    errorMessage = message + " Please login again";
                } else if (networkResponse.statusCode == 400) {
                    errorMessage = message + " Check your inputs";
                } else if (networkResponse.statusCode == 500) {
                    errorMessage = message + " Something is getting wrong";
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
        Log.i("Error", errorMessage);
        error.printStackTrace();


        VolleyLog.d(TAG, "Error: " + error.getMessage());
        try {
            onCompleteResponse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
