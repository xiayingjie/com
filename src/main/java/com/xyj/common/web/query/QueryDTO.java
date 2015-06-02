package com.xyj.common.web.query;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:13-10-21 下午3:04
 */
public class QueryDTO {

    private String order;  //排序 格式 “userName:desc”
    private int size; //每页显示的条数
    private int page;//当前页

    public QueryDTO(){}

    public QueryDTO(int page,int size){
        this.page=page;
        this.size=size;
    }
    public int getSize() {
         return size < 1 ? 10 :size ;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page < 0 ? 0 :page ;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }


}
