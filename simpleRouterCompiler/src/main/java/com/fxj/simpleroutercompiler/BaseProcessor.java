package com.fxj.simpleroutercompiler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;

public abstract class BaseProcessor extends AbstractProcessor {
    /**生成文件对象*/
    Filer filer;
    /*用于输出日志信息*/
    Messager mMessager;

    ProcessingEnvironment mProcessingEnv;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer=processingEnv.getFiler();
        mMessager=processingEnv.getMessager();
        mProcessingEnv=processingEnv;
    }
    /**返回注解处理器支持的Java SDK的版本*/
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
