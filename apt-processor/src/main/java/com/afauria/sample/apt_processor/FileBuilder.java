package com.afauria.sample.apt_processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by Afauria on 12/13/21.
 */
class FileBuilder {
    //省略getter、setter、add代码，直接操作成员变量
    TypeElement mTypeElement;
    String mPackageName;
    String mClassName;
    //保存资源绑定的信息：如变量名、资源id
    List<BindResourceInfo> mBindingResources = new ArrayList<>();
    //保存视图绑定的信息：如变量名，viewId
    List<BindViewInfo> mBindingViews = new ArrayList<>();
    //保存事件绑定的信息：如方法名，viewId
    List<BindMethodInfo> mBindingMethods = new ArrayList<>();

    public FileBuilder(TypeElement typeElement, Elements elementsUtil) {
        mTypeElement = typeElement;
        mPackageName = elementsUtil.getPackageOf(typeElement).toString();
        mClassName = typeElement.getSimpleName() + "_ViewBinding";
    }

    //拼接Java类代码
    public String generateJavaCode() {
        StringBuilder sb = new StringBuilder();
        //添加包名
        sb.append("package ").append(mPackageName).append(";\n");
        sb.append("\nimport android.view.View;\n");
        //添加类定义
        sb.append("\npublic class ").append(mClassName).append(" {\n");
        //添加构造方法
        sb.append("\tpublic ").append(mClassName).append("(final ").append(mTypeElement.getSimpleName()).append(" target) {\n");
        //遍历添加资源绑定代码
        for (BindResourceInfo bindResourceInfo : mBindingResources) {
            sb.append(bindResourceInfo.bindResourceCode());
        }
        //遍历添加视图绑定代码
        for (BindViewInfo bindViewInfo : mBindingViews) {
            sb.append(bindViewInfo.bindViewCode());
        }
        //遍历添加事件绑定代码
        for (BindMethodInfo bindMethodInfo : mBindingMethods) {
            sb.append(bindMethodInfo.bindMethodCode());
        }
        sb.append("\t}\n");
        sb.append("}\n");
        return sb.toString();
    }

    public JavaFile generateJavaFileByPoet() {
        //定义构造方法
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(mTypeElement.asType()), "target", Modifier.FINAL);
        //遍历添加资源绑定代码
        for (BindResourceInfo bindResourceInfo : mBindingResources) {
            //addStatement会添加缩进、换行、分号结尾
            //addCode直接添加代码，不会添加缩进、换行、分号结尾
            constructor.addCode(bindResourceInfo.bindResourceCode());
        }
        //遍历添加视图绑定代码
        for (BindViewInfo bindViewInfo : mBindingViews) {
            constructor.addCode(bindViewInfo.bindViewCode());
        }
        //遍历添加事件绑定代码
        for (BindMethodInfo bindMethodInfo : mBindingMethods) {
            //创建OnClickListener匿名内部类
            TypeSpec listener = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(ClassName.get("android.view", "View", "OnClickListener"))
                    .addMethod(MethodSpec.methodBuilder("onClick")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.VOID)
                            .addParameter(ClassName.get("android.view", "View"), "view")
                            .addStatement("target.$N()", bindMethodInfo.methodName)
                            .build())
                    .build();
            //添加监听器
            constructor.addStatement("target.findViewById($L).setOnClickListener($L)", bindMethodInfo.resId, listener);
        }
        //定义类
        TypeSpec typeSpec = TypeSpec
                .classBuilder(mClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constructor.build())
                .build();
        //生成java文件对象
        return JavaFile.builder(mPackageName, typeSpec).build();
    }
}
//生成类对照
//final class AptActivity_ViewBinding {
//    public void AptActivity_ViewBinding(final AptActivity target) {
//        target.text1 = target.getString(R.string.app_name);
//        target.btn1 = target.findViewById(R.id.aptBtn1);
//        //target.btn1.setOnClickListener，这个地方想拿到变量设置监听器较麻烦，因此直接再find一次View
//        target.btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                target.onBtn1Click();
//            }
//        });
//    }
//}