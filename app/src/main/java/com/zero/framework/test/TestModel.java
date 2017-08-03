package com.zero.framework.test;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Zero on 2017/5/31.
 */

public class TestModel {

    @SerializedName("tabs")
    public List<TabsBean> tabs;

    public static class TabsBean {
        /**
         * code : home_page
         * name : 首页
         * title :
         * tip : 0
         */

        @SerializedName("code")
        public String code;
        @SerializedName("name")
        public String name;
        @SerializedName("title")
        public String title;
        @SerializedName("tip")
        public String tip;
    }
}
