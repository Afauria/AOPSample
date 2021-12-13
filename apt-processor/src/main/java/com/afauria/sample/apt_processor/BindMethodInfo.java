package com.afauria.sample.apt_processor;

/**
 * Created by Afauria on 12/13/21.
 */
class BindMethodInfo {
    int resId;
    String methodName;

    public BindMethodInfo(int resId, String methodName) {
        this.resId = resId;
        this.methodName = methodName;
    }

    public String bindMethodCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\ttarget.findViewById(").append(resId).append(").setOnClickListener(new View.OnClickListener() {\n");
        sb.append("\t\t\tpublic void onClick(View view) {\n");
        sb.append("\t\t\t\ttarget.").append(methodName).append("();\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t});\n");
        return sb.toString();
    }
}
