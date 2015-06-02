package com.xyj.common.web;



import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.io.Serializable;

/**
 * @classDescription:抽出基本的DAO
 * @author:xiayingjie
 * @createTime:15/5/21
 */

public  interface BaseDAO<T,ID extends Serializable> extends JpaSpecificationExecutor<T>, PagingAndSortingRepository<T, ID> {
}
