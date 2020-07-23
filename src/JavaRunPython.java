import org.python.util.PythonInterpreter;
import java.util.Properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;


public class JavaRunPython {

    //规范一下Java调用Python一些信息，否则运行会出错
    static {
        Properties props = new Properties();
        props.put("python.home", "path to the Lib folder");
        props.put("python.console.encoding", "UTF-8");
        props.put("python.security.respectJavaAccessibility", "false");
        props.put("python.import.site", "false");
        Properties preprops = System.getProperties();
        PythonInterpreter.initialize(preprops, props, new String[0]);
    }

    //Java调用Python的第1种方式：直接写python语句
    public static void method1(){
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("a='method1'; ");
        interpreter.exec("print(a);");
    }

    //Java调用Python的第2种方式：直接执行已经编写好的Python脚本
    public static void method2(){
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("pythonFiles/method2.py");
    }

    //Java调用Python的第3种方式：使用Runtime.getRuntime()执行python脚本文件（推荐使用）
    public static void method3(){
        Process proc;
        try {
            //注意：下面的写法为 “python+空格+地址” ！！！！
            proc = Runtime.getRuntime().exec("python pythonFiles/method3.py");
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Java调用Python的第4种方式：调用python脚本中的函数
    public static void method4(){
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("pythonFiles/method4.py");//函数功能：a+b

        // 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
        PyFunction pyFunction = interpreter.get("add", PyFunction.class);
        int a = 5, b = 10;
        //调用函数，如果函数需要参数，在Java中必须先将参数转化为对应的“Python类型”
        PyObject pyobj = pyFunction.__call__(new PyInteger(a), new PyInteger(b));
        System.out.println("a + b = " + pyobj);
    }

    public static void main(String[] args) {
        method1();
        method2();
        method3();
        method4();
    }
}
