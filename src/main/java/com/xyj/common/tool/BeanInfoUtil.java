package com.xyj.common.tool;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xyj.common.tool.upload.spring.FileMeta;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @classDescription:bean 将bean转换成map
 * @author:xiayingjie
 * @createTime:15/5/26
 */

public class BeanInfoUtil {
    static  Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性
            .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd")//时间转化为特定格式
//             .setDateFormat(DateFormat.LONG)
                    // .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
            .setPrettyPrinting() //对json结果格式化.
            .setVersion(1.0)    //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.
                    //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么
                    //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.
            .create();

    /**
     * 将bean 转换成map对象包含对象
     *
     * @param bean
     * @param include 需要包含的对象 包含替换“old:new”用逗号隔开 "name,content:Content"
     * @return
     */
    public static Map<String, Object> bean2mapi(Object bean, String include) {
        Map<String, Object> map = Maps.newHashMap();
        try {
            Map<String,String>replaceMap=new HashMap<String,String>();
            String[]is=null;
            if (!StringUtils.isBlank(include)) {
                is = StringUtils.split(include, ",");
            }
            for(String s:is){
                if(s.indexOf(":")>-1) {
                    String[] rs = StringUtils.split(s, ":");
                    replaceMap.put(rs[0],rs[1]);
                }else{
                    replaceMap.put(s,null);
                }
            }


            BeanInfo b = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = b.getPropertyDescriptors();

            for (PropertyDescriptor p : propertyDescriptors) {
                String key=p.getName();
                if (!key.equals("class")) {

                    Method readMethod = p.getReadMethod();
                    Object result = readMethod.invoke(bean);

                    if(null!=replaceMap) {
                        if(replaceMap.containsKey(key)) {
                            key = replaceMap.get(key)==null?key:replaceMap.get(key);


                            boolean tag=false;
                            List list=null;
                            //用来解决数组进行传递参数的问题
                            if(result instanceof String){
                                if(((String) result).startsWith("[") && ((String) result).endsWith("]")){
                                    String res=EncodeUtil.htmlUnescape((String) result);
                                    list=gson.fromJson(res, new TypeToken<ArrayList>() {
                                    }.getType());
                                    tag=true;
                                }
                            }
                            if(tag){
                                map.put(key,list);
                            }else {
                                map.put(key, result);
                            }

                        }
                    }
                    }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }


        return map;

    }


    /**
     * 将bean 转换成map对象
     *
     * @param bean
     * @param exclude "id,roles" 移除id和roles属性
     * @param includeMap 添加新的map
     * @param replace 替换属性 “id:userId” 比如将id替换成userId
     * @return
     */
    public static Map<String, Object> bean2map(Object bean, String exclude,Map<String,Object> includeMap,String replace) {
        Map<String, Object> map = Maps.newLinkedHashMap();
        try {
            Set<String> excludeSet= null;
            Map<String,String>replaceMap=null;
            if (!StringUtils.isBlank(exclude)) {
                String[]es = StringUtils.split(exclude, ",");
                excludeSet = Sets.newHashSet(es);
            }
            if (!StringUtils.isBlank(replace)) {
                String[]rss = StringUtils.split(replace, ",");
                replaceMap=Maps.newLinkedHashMap();
                for(String s:rss){
                    String[] rs= StringUtils.split(s, ":");
                    replaceMap.put(rs[0],rs[1]);
                }
            }
            BeanInfo b = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = b.getPropertyDescriptors();

            for (PropertyDescriptor p : propertyDescriptors) {
                String key=p.getName();
                if (!key.equals("class")) {
                    boolean flag = true;
                    if(null!=excludeSet) {
                        if (excludeSet.contains(key)){
                            flag = false;
                        }
                    }

                    if (flag) {
                        Method readMethod = p.getReadMethod();
                        Object result = readMethod.invoke(bean);


                        if(null!=replaceMap) {
                            if(replaceMap.containsKey(key)) {
                                key = replaceMap.get(key);
                            }
                        }
                        boolean tag=false;
                        List list=null;
                        //用来解决数组进行传递参数的问题
                        if(result instanceof String){
                            if(((String) result).startsWith("[") && ((String) result).endsWith("]")){
                                String res=EncodeUtil.htmlUnescape((String) result);
                                list=gson.fromJson(res, new TypeToken<ArrayList>() {
                                }.getType());
                                tag=true;
                            }
                        }
                        if(tag){
                            map.put(key,list);
                        }else {
                            map.put(key, result);
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if(null!=includeMap){
            map.putAll(includeMap);
        }

       return map;

    }

    /**
     * 将bean 转换成map对象
     *
     * @param bean
     * @param exclude "id,roles" 移除id和roles属性
     * @param includeMap 添加新的map
     * @return
     */
    public static Map<String, Object> bean2map(Object bean, String exclude,Map<String,Object> includeMap){
        return bean2map(bean,exclude,includeMap,null);
    }
    /**
     * 将bean 转换成map对象
     *
     * @param bean
     * @param exclude "id,roles" 移除id和roles属性
     * @return
     */
    public static Map<String, Object> bean2map(Object bean, String exclude) {
        return bean2map(bean,exclude,null);
    }

    /**
     * 将bean 转换成map对象
     *
     * @param bean
     * @param includeMap 添加新的map
     * @return
     */
    public static Map<String, Object> bean2map(Object bean ,Map<String,Object> includeMap) {
        return bean2map(bean,null,includeMap);
    }
    /**
     * 将bean 转换成map对象
     *
     * @return
     */
    public static Map<String, Object> bean2map(Object bean ){
        return bean2map(bean,null,null);
    }

    /**
     * 将list转换成map对象
     * @param beanlist 被转换的list对象
     * @param exclude  list里面的属性移除
     * @return
     */
    public static List<Map<String,Object>> list2map(List<Object> beanlist,String exclude){
        List<Map<String,Object>> list=Lists.newArrayList();
        for(Object bean:beanlist){
            list.add(bean2map(bean,exclude));
        }
        return list;
    }
    /**
     * 将list转换成map对象
     * @param beanlist 被转换的list对象
     * @return
     */
    public static List<Map<String,Object>> list2map(List<Object> beanlist){
        List<Map<String,Object>> list=Lists.newArrayList();
        for(Object bean:beanlist){
            list.add(bean2map(bean));
        }
        return list;
    }


    /**
     * 只支持基本类型
     * @param src
     * @param desc
     * @return
     */
    public static  Object bean2bean(Object src,Object desc){
        return bean2bean(src,desc,null);
    }
    /**
     * 只支持基本类型
     * @param src
     * @param desc
     * @param exclude "user,name"  不包含某些属性
     * @return
     */
    public static  Object bean2bean(Object src,Object desc,String exclude){
        try {
            BeanInfo srcBean = Introspector.getBeanInfo(src.getClass());
            BeanInfo descBean = Introspector.getBeanInfo(desc.getClass());
            PropertyDescriptor[] descPDs = descBean.getPropertyDescriptors();
            PropertyDescriptor[] srcPDs = srcBean.getPropertyDescriptors();

            //不覆盖的属性
            Set<String> excludeSet=null;
            if (!StringUtils.isBlank(exclude)) {
                String[]es = StringUtils.split(exclude, ",");
                excludeSet = Sets.newHashSet(es);

            }
            //循环遍历属性
            for (PropertyDescriptor dp : descPDs) {
                String name=dp.getName();
                if (!name.equals("class") && (excludeSet==null || !excludeSet.contains(name))) {

                    for(PropertyDescriptor sp:srcPDs){

                        if(name.equals(sp.getName()) ){
                            Method readMethod = sp.getReadMethod();
                            Object result = readMethod.invoke(src);

                            if(checkNull(result)) {
                                Method writeMethod = dp.getWriteMethod();
                                writeMethod.invoke(desc, result);
                            }
                            break;
                        }

                    }

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return desc;
    }

    /**
     * 检查对象是否为空
     * @param result
     * @return
     */
    public static boolean checkNull(Object result){
        boolean flag=false;
        if(result instanceof String){
            flag=!StringUtils.isBlank((String)result);
        }else if(result instanceof Integer){
            flag=(int)result!=0?true:false;
        }else if(result instanceof Long){
            flag=(long)result!=0L?true:false;
        }else if(result instanceof Float){
            flag=(float)result!=0L?true:false;
        }else if(result instanceof Double){
            flag=(double)result!=0D?true:false;
        }else if(null!=result){
            flag=true;
        }
        return flag;

    }
    public static void main(String[] args) {
        FileMeta f=new FileMeta();
        f.setFileName("12323435");
        f.setFileSize("sdfsdf");
        f.setFileType("feg");
        FileMeta f1=new FileMeta();
        f1.setFileName("good");
        f1.setFileSize("bady");
        FileMeta f3=(FileMeta)bean2bean(f, f1);
        System.out.println(f.getFileName());
        System.out.println(f1.getFileName());
        System.out.printf("======");
        System.out.println(f.getFileSize());
        System.out.println(f1.getFileSize());
        System.out.printf("======");
        System.out.println(f3.getFileName());
        System.out.println(f3.getFileSize());
        String ss ="se";
//        String[]so=ss.split(ss);
//        System.out.println(so[0]);
    }
}