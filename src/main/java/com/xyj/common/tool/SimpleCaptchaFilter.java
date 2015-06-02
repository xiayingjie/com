package com.xyj.common.tool;

import static nl.captcha.Captcha.NAME;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.captcha.Captcha;
import nl.captcha.Captcha.Builder;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.gimpy.BlockGimpyRenderer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.producer.ChineseTextProducer;
import nl.captcha.text.producer.DefaultTextProducer;
import nl.captcha.text.renderer.DefaultWordRenderer;
import nl.captcha.text.renderer.WordRenderer;

/**
 * @className:SimpleCaptchaFilter.java
 * @classDescription: 扩展默认的simpleCaptcha
 * @author:xiayingjie
 * @createTime:2010-10-20
 */


public class SimpleCaptchaFilter extends HttpServlet {

	private static final String PARAM_HEIGHT = "height"; // 高度 默认为50

	private static final String PARAM_WIDTH = "width";// 宽度 默认为200

	private static final String PAEAM_NOISE = "noise";// 干扰线条 默认是没有干扰线条

	private static final String PAEAM_TEXT = "text";// 文本

	protected int _width = 200;
	protected int _height = 50;
	protected boolean _noise = false;
	protected String _text = null;

	/**
	 * 初始化过滤器.将配置文件的参数文件赋值
	 * 
	 * @throws ServletException
	 */
	@Override
	public void init() throws ServletException {
		if (getInitParameter(PARAM_HEIGHT) != null) {
			_height = Integer.valueOf(getInitParameter(PARAM_HEIGHT));
		}

		if (getInitParameter(PARAM_WIDTH) != null) {
			_width = Integer.valueOf(getInitParameter(PARAM_WIDTH));
		}

		if (getInitParameter(PAEAM_NOISE) != null) {
			_noise = Boolean.valueOf(getInitParameter(PAEAM_NOISE));
		}

		if (getInitParameter(PAEAM_NOISE) != null) {
			_text = String.valueOf(getInitParameter(PAEAM_TEXT));
		}
	}

	/**
	 * 因为获取图片只会有get方法
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Builder builder = new Builder(_width, _height);
		// 增加边框
		builder.addBorder();
		// 是否增加干扰线条
		if (_noise == true)
			builder.addNoise();
		// ----------------自定义字体大小-----------
		// 自定义设置字体颜色和大小 最简单的效果 多种字体随机显示
		List<Font> fontList = new ArrayList<Font>();
		// fontList.add(new Font("Arial", Font.HANGING_BASELINE,
		// 40));//可以设置斜体之类的
		fontList.add(new Font("Courier", Font.BOLD, 60));
		DefaultWordRenderer dwr = new DefaultWordRenderer(Color.green, fontList);

		// 加入多种颜色后会随机显示 字体空心
		// List<Color> colorList=new ArrayList<Color>();
		// colorList.add(Color.green);
		// colorList.add(Color.white);
		// colorList.add(Color.blue);
		// ColoredEdgesWordRenderer cwr= new
		// ColoredEdgesWordRenderer(colorList,fontList);

		WordRenderer wr = dwr;
		// 增加文本，默认为5个随机字符.
		if (_text == null) {
			builder.addText();
		} else {
			String[] ts = _text.split(",");
			for (int i = 0; i < ts.length; i++) {
				String[] ts1 = ts[i].split(":");
				if ("chinese".equals(ts1[0])) {
					builder.addText(
							new ChineseTextProducer(Integer.parseInt(ts1[1])),
							wr);
				} else if ("number".equals(ts1[0])) {
					// 这里没有0和1是为了避免歧义 和字母I和O
					char[] numberChar = new char[] { '2', '3', '4', '5', '6',
							'7', '8' };
					builder.addText(
							new DefaultTextProducer(Integer.parseInt(ts1[1]),
									numberChar), wr);
				} else if ("word".equals(ts1[0])) {
					// 原理同上
					char[] numberChar = new char[] { 'a', 'b', 'c', 'd', 'e',
							'f', 'g', 'h', 'k', 'm', 'n', 'p', 'r', 'w', 'x',
							'y' };
					builder.addText(
							new DefaultTextProducer(Integer.parseInt(ts1[1]),
									numberChar), wr);
				} else {
					builder.addText(
							new DefaultTextProducer(Integer.parseInt(ts1[1])),
							wr);
				}
			}

		}

		// --------------添加背景-------------
		// 设置背景渐进效果 以及颜色 form为开始颜色，to为结束颜色
		GradiatedBackgroundProducer gbp = new GradiatedBackgroundProducer();
		gbp.setFromColor(Color.yellow);
		gbp.setToColor(Color.red);
		// 无渐进效果，只是填充背景颜色
		// FlatColorBackgroundProducer fbp=new
		// FlatColorBackgroundProducer(Color.red);
		// 加入网纹--一般不会用
		// SquigglesBackgroundProducer sbp=new SquigglesBackgroundProducer();
		// 没发现有什么用,可能就是默认的
		// TransparentBackgroundProducer tbp = new
		// TransparentBackgroundProducer();

		builder.addBackground(gbp);
		// ---------装饰字体---------------
		// 字体边框齿轮效果 默认是3
		builder.gimp(new BlockGimpyRenderer(1));
		// 波纹渲染 相当于加粗
		// builder.gimp(new RippleGimpyRenderer());
		// 修剪--一般不会用
		// builder.gimp(new ShearGimpyRenderer(Color.red));
		// 加网--第一个参数是横线颜色，第二个参数是竖线颜色
		// builder.gimp(new FishEyeGimpyRenderer(Color.red,Color.yellow));
		// 加入阴影效果 默认3，75
		// builder.gimp(new DropShadowGimpyRenderer());
		// 创建对象
		Captcha captcha = builder.build();

		CaptchaServletUtil.writeImage(resp, captcha.getImage());
		req.getSession().setAttribute(NAME, captcha);
	}

	/**
	 * web.xml中配置 <servlet> <servlet-name>StickyCaptcha</servlet-name>
	 * <servlet-class
	 * >com.xyj.com.tool.captcha.SimpleCaptchaFilter</servlet-class>
	 * <init-param> <param-name>width</param-name>
	 * <param-value>200</param-value> </init-param> <init-param>
	 * <param-name>height</param-name> <param-value>50</param-value>
	 * </init-param> <!-- 不加干扰线 --> <init-param> <param-name>noise</param-name>
	 * <param-value>false</param-value> </init-param> <!-- 意思是3个单词，3个数字 -->
	 * <init-param> <param-name>text</param-name>
	 * <param-value>word:3,number:3</param-value> </init-param> </servlet>
	 * 
	 * <servlet-mapping> <servlet-name>StickyCaptcha</servlet-name>
	 * <url-pattern>/stickyImg</url-pattern> </servlet-mapping>
	 * 
	 * jsp:<img src="/项目名/stickyImg" />
	 * 
	 * java: Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
	 * captcha.isCorrect(answer) anwer为获取的参数值
	 * //如果是wap不能使用session的话，得将request获取值放到页面，然后在页面传递到java类中。
	 * 
	 */
}
