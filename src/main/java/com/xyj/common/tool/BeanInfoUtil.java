package com.xyj.common.tool;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @classDescription:bean 将bean转换成map
 * @author:xiayingjie
 * @createTime:15/5/26
 */

public class BeanInfoUtil {


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
        Map<String, Object> map = Maps.newHashMap();
        try {
            Set<String> excludeSet= null;
            Map<String,String>replaceMap=null;
            if (!StringUtils.isBlank(exclude)) {
                String[]es = StringUtils.split(exclude, ",");
                excludeSet = Sets.newHashSet(es);
            }
            if (!StringUtils.isBlank(replace)) {
                String[]rss = StringUtils.split(replace, ",");
                replaceMap=Maps.newHashMap();
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
                        map.put(key,result);

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
    public static void main(String[] args) {

    }
}