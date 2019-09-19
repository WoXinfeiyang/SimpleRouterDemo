package com.fxj.simpleroutercompiler;

import com.fxj.simpleRouterAnnotation.Router;
import com.google.auto.service.AutoService;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;



/**
 * Router注解处理器
 * */
@AutoService(Processor.class)/*注册注解处理器*/
public class RouteProcessor extends BaseProcessor {

    private static final String TAG=RouteProcessor.class.getSimpleName();

    /**返回当前处理器要处理的注解都有哪些*/
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types= new HashSet<String>();
        types.add(Router.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mMessager.printMessage(Diagnostic.Kind.NOTE,TAG+"**RouteProcessor.process**start");
        /*通过此方法拿到这个模块下面所有用到Router这个注解的节点*/
        Set<? extends Element> elements =roundEnv.getElementsAnnotatedWith(Router.class);
        /*存储注解@Router的path和类签名,k--注解@Router的path和,v--被注解@Router修饰的类签名*/
        Map<String,String> activitiesInfo= new ConcurrentHashMap<String,String>();

        for(Element element:elements){
            TypeElement typeElement= (TypeElement) element;/*由于注解@Router是作用在类节点上的，故强转成类节点*/
            String path= typeElement.getAnnotation(Router.class).path();/*获取这个类节点上注解@Router的path值*/
            String className= typeElement.getQualifiedName().toString();/*获取这个类节点的类签名*/
            activitiesInfo.put(path,className);
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE,TAG+"**RouteProcessor.process**activitiesInfo.size()="+activitiesInfo.size());
        if(activitiesInfo.size()>0){
            Writer writer=null;
            /*定义com.fxj.simplerouter.IRouter实现类的包名*/
            String packageNameForIRouter="com.fxj.irouterimpl";
            /*定义com.fxj.simplerouter.IRouter实现类的类名,之所以加时间戳是为了避免在不同模块下生成的IRouter实现类的类名相同*/
            String classNameForIRouter="IRouterImpl"+System.currentTimeMillis();
            mMessager.printMessage(Diagnostic.Kind.NOTE,TAG+"**RouteProcessor.process**classNameForIRouter="+classNameForIRouter);
            try {
                /*创建一个.java源文件*/
                JavaFileObject sourceFileForIRouterImpl=filer.createSourceFile(packageNameForIRouter+"."+classNameForIRouter);
                writer=sourceFileForIRouterImpl.openWriter();

                /*
                package com.fxj.irouterimpl;

                import com.fxj.simplerouter.IRouter;
                import com.fxj.simplerouter.SimpleRouter;
                import android.util.Log;

                public class IRouterImpl54321 implements IRouter {
                    @Override
                    public void loadInto() {
                        log.d("IRouterImpl54321","path="+path+",className="+className);
                        SimpleRouter.getInstance().putActivity("module1/main",Module1MainActivity.class);
                    }
                }
                * */

                writer.write("package  "+packageNameForIRouter+";\n" +
                        "\n" +
                        "import com.fxj.simplerouter.IRouter;\n" +
                        "import com.fxj.simplerouter.SimpleRouter;\n" +
//                        "import android.util.Log;\n"+
                        "\n" +
                        "public class "+classNameForIRouter+" implements IRouter {\n" +
                        "    @Override\n" +
                        "    public void loadInto() {");

                Iterator<String> iterator=activitiesInfo.keySet().iterator();
                while(iterator.hasNext()){
                    String path=iterator.next();
                    String activityClassName=activitiesInfo.get(path);
                    mMessager.printMessage(Diagnostic.Kind.NOTE,TAG+"**RouteProcessor.process**path="+path+",activityClassName="+activityClassName);
                    writer.write("        SimpleRouter.getInstance().putActivity(\""+path+"\","+activityClassName+".class);\n");
                }

                writer.write("    }\n" +
                        "}");

            }catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(writer!=null){
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        mMessager.printMessage(Diagnostic.Kind.NOTE,TAG+"**RouteProcessor.process**end");
        return true;
    }
}
