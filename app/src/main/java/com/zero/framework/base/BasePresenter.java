package com.zero.framework.base;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Zero on 2017/5/25.
 */
public abstract class BasePresenter {
	
    abstract protected void initBaseData(Context context, Handler handler, BaseInterface iView, Intent intent);
    
    abstract  protected void handMsg(Message msg);

}
