package alina.com.rms.controller;

import alina.com.rms.util.CallType;

/**
 * Created by HP on 02-01-2018.
 */

public interface AsyncCompleteListner {

    public void asyncCompleteListner(String response, CallType callType);
}
