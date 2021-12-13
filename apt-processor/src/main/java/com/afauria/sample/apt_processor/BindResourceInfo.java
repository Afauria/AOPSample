package com.afauria.sample.apt_processor;

/**
 * Created by Afauria on 12/13/21.
 */
class BindResourceInfo {
    int resId;
    String fieldName;

    public BindResourceInfo(int resId, String fieldName) {
        this.resId = resId;
        this.fieldName = fieldName;
    }

    public String bindResourceCode() {
        return String.format("\t\ttarget.%s = target.getString(%s);\n", fieldName, resId);
    }
}
