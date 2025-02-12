package com.ksy.exam.spring2502_heathyfood_test.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class Ut {
    public static String historyBack(String resultCode, String msg) {
        if(resultCode == null){
            resultCode="";
        }
        if(msg == null){
            msg="";
        }

        String resultMsg = resultCode + "/" + msg ;

        return Ut.f("""
                <script>
                 let resultMsg = '%s'.trim();
                 if(resultMsg.length > 0){
                 alert(resultMsg);
                 }
                 history.back();
                </script>
                """, resultMsg);
    }

    public static String jsReplace(String resultCode, String msg, String replace) {
        if(resultCode == null){
            resultCode="";
        }
        if(msg == null){
            msg="";
        }
        if(replace == null){
            replace="/";
        }

        String resultMsg = resultCode + "/" + msg ;

        return Ut.f("""
                <script>
                 let resultMsg = '%s'.trim();
                 if(resultMsg.length > 0){
                 alert(resultMsg);
                 }
                 location.replace('%s');
                </script>
                """, resultMsg, replace);
    }

    public static boolean isEmptyOrNull(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return ((String) obj).trim().length() == 0;
        }
        if (obj instanceof Map<?,?>) {
            return ((Map<?,?>) obj).isEmpty();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }

        return false;
    }

    public static String f(String format, Object... object) { // 가변인자 넘어오는 개수나 양식을 정하지 않았다
        return String.format(format, object);
    }
}
