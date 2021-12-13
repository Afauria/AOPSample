package com.afauria.sample.apt_processor;

import com.afauria.sample.apt_annotation.AptBindString;
import com.afauria.sample.apt_annotation.AptBindView;
import com.afauria.sample.apt_annotation.AptOnClick;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;

import net.ltgt.gradle.incap.IncrementalAnnotationProcessor;
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.DYNAMIC)
public class AptProcessor extends AbstractProcessor {
    private Filer mFiler;
    private Messager mMessager;
    private Elements mElements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
        mElements = processingEnvironment.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(AptBindView.class.getCanonicalName());
        types.add(AptBindString.class.getCanonicalName());
        types.add(AptOnClick.class.getCanonicalName());
        return types;
    }

    private void error(Element element, String msg) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, msg, element);
    }

    //按类生成文件，一个类可能包含多个注解，因此新建一个Map，保存类和对应的类信息
    //生成类需要的信息包括：包名、类名、资源绑定、视图绑定、事件绑定等信息。
    //定义一个FileBuilder类保存，等到所有注解解析完毕之后，再统一生成文件
    Map<TypeElement, FileBuilder> builderMap = new HashMap<>();

    private FileBuilder getOrCreateBuilder(TypeElement element) {
        FileBuilder builder = builderMap.get(element);
        if (builder == null) {
            builder = new FileBuilder(element, mElements);
            builderMap.put(element, builder);
        }
        return builder;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //每次执行清空下buildMap，避免下个round重复创建文件
        builderMap.clear();
        if (set.isEmpty()) {
            return false;
        }
        //解析@AptBindString注解，并加入到对应的FileBuilder中
        findAndParseBindingResources(roundEnvironment);
        //解析@AptBindView注解，并加入到对应的FileBuilder中
        findAndParseBindingView(roundEnvironment);
        //解析@AptOnClick注解，并加入到对应的FileBuilder中
        findAndParseBindingMethod(roundEnvironment);
        for (FileBuilder fileBuilder : builderMap.values()) {
            try {
                //方式一：使用拼接源代码方式生成Java文件
//                generateJavaFile(fileBuilder);
                //方式二：使用JavaPoet生成Java文件
                fileBuilder.generateJavaFileByPoet().writeTo(mFiler);
            } catch (IOException e) {
                //使用Messager打印日志，打印error并不会中断process执行，但是会导致编译失败
                error(fileBuilder.mTypeElement, String.format("Unable to write ViewBinding for type %s: %s", fileBuilder.mTypeElement, e.getMessage()));
            }
        }
        return true;
    }

    private void generateJavaFile(FileBuilder fileBuilder) throws IOException {
        //创建Java源文件对象
        JavaFileObject javaFileObject = mFiler.createSourceFile(fileBuilder.mPackageName + "." + fileBuilder.mClassName);
        //根据存储的信息拼接Java代码
        String code = fileBuilder.generateJavaCode();
        Writer writer = javaFileObject.openWriter();
        //写入文件
        writer.write(code);
        writer.flush();
        writer.close();
    }

    private void findAndParseBindingResources(RoundEnvironment env) {
        //获取所有被@BindString注解的元素
        Set<? extends Element> s = env.getElementsAnnotatedWith(AptBindString.class);
        for (Element element : s) {
            //获取注解的值
            int resId = element.getAnnotation(AptBindString.class).value();
            //获取Field变量名称
            final String name = element.getSimpleName().toString();
            //获取父元素
            TypeElement parentElement = (TypeElement) element.getEnclosingElement();
            //获取缓存的生成类信息
            FileBuilder builder = getOrCreateBuilder(parentElement);
            //构造资源绑定信息，加入对应的FileBuilder中
            builder.mBindingResources.add(new BindResourceInfo(resId, name));
        }
    }

    private void findAndParseBindingView(RoundEnvironment env) {
        Set<? extends Element> s = env.getElementsAnnotatedWith(AptBindView.class);
        for (Element element : s) {
            int resId = element.getAnnotation(AptBindView.class).value();
            final String name = element.getSimpleName().toString();
            TypeElement parentElement = (TypeElement) element.getEnclosingElement();
            FileBuilder builder = getOrCreateBuilder(parentElement);
            builder.mBindingViews.add(new BindViewInfo(resId, name));
        }
    }

    private void findAndParseBindingMethod(RoundEnvironment env) {
        Set<? extends Element> s = env.getElementsAnnotatedWith(AptOnClick.class);
        for (Element element : s) {
            int resId = element.getAnnotation(AptOnClick.class).value();
            final String name = element.getSimpleName().toString();
            TypeElement parentElement = (TypeElement) element.getEnclosingElement();
            FileBuilder builder = getOrCreateBuilder(parentElement);
            builder.mBindingMethods.add(new BindMethodInfo(resId, name));
        }
    }
}