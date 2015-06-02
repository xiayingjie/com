package com.xyj.common.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @classDescription:简体中文转换
 * @author:xiayingjie
 * @createTime:13-10-15 下午4:51
 */
public class ChineseConvertor {
    private Map<Character, Character> ts;  //Traditional Chinese
    private Map<Character, Character> st;  //Simplified Chinese
    private static ChineseConvertor convertor;
    /**
     * 获取构建实例
     * @return
     * @throws IOException
     */
    public static ChineseConvertor getInstance() throws IOException {
        if(convertor == null) convertor = new ChineseConvertor();
        return(convertor);
    }

    /**
     * 繁体转简体
     * @param s
     * @return
     */
    public String t2s(final String s) {
        char[] cs = new char[s.length()];
        for(int i=0; i<s.length(); i++) cs[i] = t2s(s.charAt(i));
        return(new String(cs));
    }

    /**
     * 简体转繁体
     * @param s
     * @return
     */
    public String s2t(final String s) {
        char[] cs = new char[s.length()];
        for(int i=0 ; i<s.length(); i++) cs[i] = s2t(s.charAt(i));
        return(new String(cs));
    }


    /**
     * 繁体转简体（单个字符）
     * @param c
     * @return
     */
    public Character t2s(final char c) {
        if(ts.get(c) == null ) return(c);
        return(ts.get(c));
    }

    /**
     * 简体转繁体（单个字符）
     * @param c
     * @return
     */
    public Character s2t(final char c) {
        if(st.get(c) == null) return(c);
        return(st.get(c));
    }

    /**
     * 加载简繁体字典
     * @return
     * @throws IOException
     */
    private List<Character> loadTable() throws IOException {

        List<Character> cs = loadChar("/dist/ts.tab", "UTF-8");
        if((cs.size() % 2) != 0) throw new RuntimeException("The conversion table may be damaged or not exists");
        else return(cs);
    }

    /**
     * 中文转换
     * @throws IOException
     */
    private ChineseConvertor() throws IOException {
        List<Character> cs = loadTable();
        ts = new HashMap<Character, Character>();
        st = new HashMap<Character, Character>();

        for(int i=0; i<cs.size(); i=i+2) {
            ts.put(cs.get(i), cs.get(i+1));
            st.put(cs.get(i+1), cs.get(i));
        }
    }

    /**
     * 加载指定文件和字符
     * @param file
     * @param charset
     * @return
     * @throws IOException
     */
    private List<Character> loadChar(String file, String charset) throws IOException {
        List<Character> content = new ArrayList<Character>();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(this.getClass().getResourceAsStream(file), charset)
        );
        int c;
        while((c = in.read()) != -1) content.add((char) c);
        in.close();
        return(content);
    }




    public static void main(String[] args) throws IOException {

        System.out.println( ChineseConvertor.getInstance().s2t("龙的传人"));
        String root=ChineseConvertor.class.getClass().getResource("/").getPath();//System.getProperty("user.dir");//Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(root);
        System.out.println(ChineseConvertor.class.getClass().getResourceAsStream("/dist/ts.tab"));

    }
}
