package com.afauria.sample.apt_processor;

/**
 * Created by Afauria on 12/13/21.
 */
class BindViewInfo {
    int resId;
    String fieldName;

    public BindViewInfo(int resId, String fieldName) {
        this.resId = resId;
        this.fieldName = fieldName;
    }

    public String bindViewCode() {
        return String.format("\t\ttarget.%s = target.findViewById(%s);\n", fieldName, resId);
    }


}
