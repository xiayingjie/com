package com.xyj.common.tool.chart;


import com.xyj.common.tool.Dom4jUtil;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;



/**
 * @className:FusionChartUtil.java
 * @classDescription:fusionChart操作类
 * @author:xiayingjie
 * @createTime:2010-11-26
 */

public class FusionChartUtil {
	// 一般提供如下图：3D/2D柱形图，曲线图，3D/2D饼图，区域图，堆栈图，联合图，蜡烛图，漏斗图，甘特图，
	// 按程序来分:单系列图，多系列图，堆栈图，组合图，财经图，漏斗图，甘特图
	// 此程序做 单系列图，多系列图，组合图，其他图形暂时不考虑。

	/**
	 * 通用属性 attibutesMap:caption(标题);subCaption(子标题);
	 * xAxisName(X轴标签);yAxisName(y轴标签)
	 * yaxisminvalue(Y轴最小数字);yaxismaxvalue(Y轴最大);
	 * xaxisminvalue(X轴最小数字)xaxismaxvalue(X轴最大) numberPrefix(加上前缀比如为“￥”)
	 * numberSuffix(后缀比如加上百分号"%25") decimalSeparator(小数分隔符默认为'.')
	 * thousandSeparator(前位分隔符默认为',') formatNumberScale(格式化小数，如果为0则去掉默认的逗号如
	 * 12,345)decimalPrecision（指定小数位位数，如果不写则默认是2位）;
	 * 
	 */
	public static Element getRootElement(Map<String, String> attributesMap) {
		// 创建Docment对象
		Document doc = Dom4jUtil.createDocument();
		Element graph = doc.addElement("graph");
		for (String name : attributesMap.keySet()) {
			graph.addAttribute(name, attributesMap.get(name));
		}
		return graph;
	}

	/**
	 * 获取结果
	 */
	/** ----------------------------单系列图形-------------------------------- */
	/**
	 * FCF_Column3D.swf FCF_Column2D.swf FCF_Line.swf FCF_Area2D.swf
	 * FCF_Bar2D.swf FCF_Pie2D.swf FCF_Pie3D.swf FCF_Doughnut2D.swf
	 */

	/**
	 * 设置值
	 * 
	 * @param root
	 *            根目录
	 * @param attributesMap
	 *            属性配置 （可选项）
	 *            name(x名称),value(值),link(图像加上链接，可以是js也可以是路径，新窗口前面打开加上"n-")
	 */
	public static void setSingerElement(Element root,
			Map<String, String> attributesMap) {
		Element set = root.addElement("set");
		for (String name : attributesMap.keySet()) {
			set.addAttribute(name, attributesMap.get(name));
		}
	}

	/** ----------------------------多系列图形+组合图形-------------------------------- */
	/**
	 * -----多系列图------ FCF_MSColumn2D.swf FCF_MSColumn3D.swf FCF_MSLine.swf
	 * FCF_MSBar2D.swf FCF_MSArea2D.swf
	 */

	/**
	 * -----组合图------- FCF_MSColumn2DLineDY.swf FCF_MSColumn3DLineDY.swf
	 */
	/**
	 * 设置多系列图的x坐标name
	 * 
	 * @param root
	 *            根节点
	 * @param attributesMap
	 *            相关属性，比如设置字体大小 font='Arial' fontSize='11' fontColor='000000'
	 * @param values
	 *            x具体的名称
	 * @return
	 */
	public static Element setCategoryElement(Element root,
			Map<String, String> attributesMap, String[] values) {
		// 设置多种类
		Element categories = root.addElement("categories");
		for (String name : attributesMap.keySet()) {
			categories.addAttribute(name, attributesMap.get(name));
		}
		for (String value : values) {
			categories.addElement("category").addAttribute("name", value);
		}
		return categories;
	}

	/**
	 * 设置值
	 * 
	 * @param root
	 * @param attributesMap
	 *            seriesname='Grain' color='C9198D'
	 * @param values
	 * @return
	 */
	public static Element setDatasetElement(Element root,
			Map<String, String> attributesMap, String[] values) {
		// 设置多种类
		Element dateset = root.addElement("dataset");
		for (String name : attributesMap.keySet()) {
			dateset.addAttribute(name, attributesMap.get(name));
		}
		for (String value : values) {
			dateset.addElement("set").addAttribute("value", value);
		}
		return dateset;
	}

	// ----------------------------趋势线方便比较----------------------
	/**
	 * 添加趋势线
	 * 
	 * @param root
	 * @return
	 */
	public static Element setTrendLine(Element root) {
		return root.addElement("trendlines");
	}

	/**
	 * 添加趋势线
	 * 
	 * @param trendline
	 * @param attributesMap
	 *            startValue = '26000' color = '91C728' displayValue = 'Target'
	 *            showOnTop = '1 '
	 * @return
	 */
	public static Element setLine(Element trendline,
			Map<String, String> attributesMap) {
		Element line = trendline.addElement("line");
		for (String name : attributesMap.keySet()) {
			line.addAttribute(name, attributesMap.get(name));
		}
		return line;
	}

	/**
	 * 将root转换成String
	 * 
	 * @param root
	 * @return
	 */
	public static String toStringByRoot(Element root) {
		return Dom4jUtil.docToString(root.getDocument(), true);
	}

	public static void main(String[] args) {
		// ---------------单系列图形-----------------

		Map rootAttributeMap = new HashMap();
		rootAttributeMap.put("caption", "每月销售额柱形图");// 主标题
		rootAttributeMap.put("subcaption", "2006-2007");// 符标题
		rootAttributeMap.put("xAxisName", "月份");// x轴名称
		rootAttributeMap.put("yAxisName", "units");// Y轴名称 免费版不支持中文显示
		rootAttributeMap.put("decimalPrecision", "0");
		Element root = FusionChartUtil.getRootElement(rootAttributeMap);

		Map singerAttributeMap = new HashMap();
		String[] clors = { "00", "11", "22", "33", "44", "55", "66", "77",
				"88", "99", "AA", "BB", "CC", "DD", "EE", "FF" };

		for (int i = 1; i <= 12; i++) {
			singerAttributeMap.put("name", i + "月");
			Random r = new Random();
			int value = r.nextInt(30000);
			int i1 = r.nextInt(clors.length);
			int i2 = r.nextInt(clors.length);
			int i3 = r.nextInt(clors.length);
			singerAttributeMap.put("value", String.valueOf(value));
			singerAttributeMap.put("color",
					String.valueOf(clors[i1] + clors[i2] + clors[i3]));
			FusionChartUtil.setSingerElement(root, singerAttributeMap);
		}

		Map lam = new HashMap();
		lam.put("startValue", "26000");
		lam.put("color", "91C728");

		FusionChartUtil.setLine(FusionChartUtil.setTrendLine(root), lam);

		System.out.println(FusionChartUtil.toStringByRoot(root));

		// ---------------------多系列图--------------------------
		// Map rootAttributeMap=new HashMap();
		// rootAttributeMap.put("caption", "每月销售额柱形图");//主标题
		// rootAttributeMap.put("subcaption", "2006-2007");//符标题
		// rootAttributeMap.put("xAxisName", "月份");//x轴名称
		// rootAttributeMap.put("yAxisName", "units");//Y轴名称 免费版不支持中文显示
		// rootAttributeMap.put("showvalues", "0");//是否显示数字，0为不显示，1为显示
		//
		//
		//
		//
		// Element root=FusionChartUtil.getRootElement(rootAttributeMap);
		//
		// Map categoryAttributeMap=new HashMap();
		// categoryAttributeMap.put("font", "Arial");
		// categoryAttributeMap.put("fontSize", "13");
		// categoryAttributeMap.put("fontColor", "000000");
		// String[]
		// values={"1月","","3月","","5月","","7月","","9月","","11月","12月"};
		//
		// FusionChartUtil.setCategoryElement(root,categoryAttributeMap,values);
		//
		//
		// Map datasetAttributeMap=new HashMap();
		// datasetAttributeMap.put("seriesName", "2006");
		// datasetAttributeMap.put("color", "c4e3f7");
		//
		//
		// Random r=new Random();
		// String[] vss=new String[12];
		// for(int i=0;i<12;i++){
		// vss[i]=String.valueOf(r.nextInt(30000));
		// }
		// FusionChartUtil.setDatasetElement(root, datasetAttributeMap, vss);
		//
		// datasetAttributeMap.put("seriesName", "2007");
		// datasetAttributeMap.put("color", "Fad35e");
		// for(int i=0;i<12;i++){
		// vss[i]=String.valueOf(r.nextInt(30000));
		// }
		// FusionChartUtil.setDatasetElement(root, datasetAttributeMap, vss);
		//
		// System.out.println(FusionChartUtil.toStringByRoot(root));

		// ------------------组合图-------------------
		// Map rootAttributeMap=new HashMap();
		// rootAttributeMap.put("caption", "每月销售额柱形图");//主标题
		// rootAttributeMap.put("subcaption", "2006-2007");//符标题
		// rootAttributeMap.put("xAxisName", "月份");//x轴名称
		// //rootAttributeMap.put("yAxisName", "units");//Y轴名称 免费版不支持中文显示
		// rootAttributeMap.put("PYAxisName", "数量av");
		// rootAttributeMap.put("SYAxisName", "uv");
		// rootAttributeMap.put("showvalues", "0");//是否显示数字，0为不显示，1为显示
		//
		//
		//
		//
		// Element root=FusionChartUtil.getRootElement(rootAttributeMap);
		//
		// Map categoryAttributeMap=new HashMap();
		// categoryAttributeMap.put("font", "Arial");
		// categoryAttributeMap.put("fontSize", "13");
		// categoryAttributeMap.put("fontColor", "000000");
		// String[]
		// values={"1月","","3月","","5月","","7月","","9月","","11月","12月"};
		//
		// FusionChartUtil.setCategoryElement(root,categoryAttributeMap,values);
		//
		//
		// Map datasetAttributeMap=new HashMap();
		// datasetAttributeMap.put("seriesName", "2006");
		// datasetAttributeMap.put("color", "c4e3f7");
		// datasetAttributeMap.put("parentYAxis", "P");//P表示是主轴
		//
		//
		// Random r=new Random();
		// String[] vss=new String[12];
		// for(int i=0;i<12;i++){
		// vss[i]=String.valueOf(r.nextInt(30000));
		// }
		// FusionChartUtil.setDatasetElement(root, datasetAttributeMap, vss);
		//
		// datasetAttributeMap.put("seriesName", "2007");
		// datasetAttributeMap.put("color", "Fad35e");
		// for(int i=0;i<12;i++){
		// vss[i]=String.valueOf(r.nextInt(30000));
		// }
		// FusionChartUtil.setDatasetElement(root, datasetAttributeMap, vss);
		// //次轴
		// datasetAttributeMap.put("seriesName", "用户UV");
		// datasetAttributeMap.put("color", "FF11BB");
		// datasetAttributeMap.put("parentYAxis", "S");//s表示是次轴
		// for(int i=0;i<12;i++){
		// vss[i]=String.valueOf(r.nextInt(300));
		// }
		// FusionChartUtil.setDatasetElement(root, datasetAttributeMap, vss);
		//
		// System.out.println(FusionChartUtil.toStringByRoot(root));
	}

}
