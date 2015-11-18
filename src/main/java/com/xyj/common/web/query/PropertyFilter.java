package com.xyj.common.web.query;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @className:PropertyFilter.java
 * @classDescription:过滤条件封装类
 * @author:xiayingjie
 * @createTime:2013-10-22
 */
public class PropertyFilter {

	// 属性比较类型.依次分别是等于，like,小于，大于，小于等于，大于等于
	public enum MatchType {
		EQ, LIKE, LT, GT, LE, GE , IN , NIN ;
	}

	// 属性数据类型. 
	public enum PropertyType {
		S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class);

		private Class<?> clazz;

		PropertyType(Class<?> clazz) {
			this.clazz = clazz;
		}

		public Class<?> getValue() {
			return clazz;
		}
	}
	private String propertyName=null;
	private Class<?> propertyType = null;
	private Object propertyValue = null;
	private MatchType matchType = null;

    private List<PropertyFilter> filterList=new ArrayList<PropertyFilter>();


    public PropertyFilter() {

	}



	/**
	 * @param filterName 比较属性字符串,含待比较的比较类型、属性值类型及属性列表.
	 *                   例如：userName:LIKE_S 或者 module_id__EQ_I(字段module.id)
	 * @param value 待比较的值.
	 */
	private PropertyFilter(final String filterName, String value) {

		String matchTypeStr = StringUtils.substringAfter(filterName, "__");
		String matchTypeCode = StringUtils.substringBefore(matchTypeStr, "_");
		String propertyTypeCode = StringUtils.substringAfter(matchTypeStr, "_");

		try {
			matchType = Enum.valueOf(MatchType.class, matchTypeCode);

		} catch (RuntimeException e) {
			throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性比较类型."+matchTypeCode, e);
		}

		try {
			propertyType = Enum.valueOf(PropertyType.class, propertyTypeCode).getValue();
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性值类型."+propertyTypeCode, e);
		}

		propertyName = StringUtils.substringBefore(filterName, "__").trim();
		if(null==value){
			this.propertyValue="";
		}else{
			this.propertyValue=value.trim();
		}


	}

    /**
     * 将搜索条件进行转换
     */
    public static List<PropertyFilter> parse(Map<String, String> searchMap) {
        List<PropertyFilter> pfList= Lists.newArrayList();

        for (String key : searchMap.keySet()) {
            // 过滤掉空值
            String value = searchMap.get(key);
            if (StringUtils.isBlank(value)) {
                continue;
            }

            pfList.add(new PropertyFilter(StringUtils.substringAfter(key,"_"),value));

        }
        return pfList;
    }

    /**
     * 将搜索条件传递到页面
     */
    public static void setAttribute(HttpServletRequest request,Map<String, String> searchMap) {


        for (String key : searchMap.keySet()) {
            // 过滤掉空值
            String value = searchMap.get(key);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            request.setAttribute(key,value);
        }
    }

    /**
     * 获取显示条件
     */
    public static Map<String,String> parseCondition(Map<String, String> searchMap,QueryDTO queryDTO,String path) {
        Map<String,String>map= Maps.newHashMap();

        String url=parseUrl(searchMap,queryDTO);


        for (String key : searchMap.keySet()) {
            // 过滤掉空值
            String value = searchMap.get(key);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            //显示条件
            if(key.indexOf("__LIKE")>-1){
                value="匹配‘"+value+"'";
            }else if(key.indexOf("__LT")>-1){
                value="小于’"+value+"'";
            }else if(key.indexOf("__GT")>-1){
                value="大于‘"+value+"'";
            }else if(key.indexOf("__EQ")>-1){
                value="等于’"+value+"'";
            }else if(key.indexOf("__LE")>-1){
                value="小于等于‘"+value+"'";
            }else if(key.indexOf("__GT")>-1){
                value="大于等于’"+value+"'";
            }

            String queryUrl=url.replace(key+"="+searchMap.get(key)+"&","");
            map.put(value,path+"?"+queryUrl);

        }
        return map;
    }

    /**
     * 获取搜索条件url
     */
    public static String  parseUrl(Map<String, String> searchMap,QueryDTO queryDTO) {
        StringBuffer urlBuffer=new StringBuffer("");

        for (Map.Entry<String, String> entry : searchMap.entrySet()) {
            // 过滤掉空值
            String key = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.isBlank(value)) {
                continue;
            }
            urlBuffer.append(key+"="+searchMap.get(key)+"&");
        }
        urlBuffer.append("page="+queryDTO.getPage()+"&");
        urlBuffer.append("size="+queryDTO.getSize());
        if(!StringUtils.isBlank(queryDTO.getOrder())){
            urlBuffer.append("&order="+queryDTO.getOrder());
        }
        return urlBuffer.toString();
    }





    /**
     * 获取过滤条件集合
     * @return
     */
    public List<PropertyFilter> getFilterList() {
        return filterList;
    }

    /**
     * 添加过滤条件，方便调用
     * @param filterName
     * @param value
     */
    public void addFilter(final String filterName, final String value){
        filterList.add(new PropertyFilter(filterName,value));
    }

	/**
	 * 获取比较值.
	 */
	public Object getPropertyValue() {
		return propertyValue;
	}
	/**
	 * 设置propertyValue
	 */
	public void setPropertyValue(String propertyValue){
		this.propertyValue=propertyValue;
	}

	/**
	 * 获取比较值的类型.
	 */
	public Class<?> getPropertyType() {
		return propertyType;
	}

	/**
	 * 获取比较方式类型.
	 */
	public MatchType getMatchType() {
		return matchType;
	}
	/**
	 * 获取属性名称.
	 */
	public String getPropertyName() {
		return propertyName;
	}
}
