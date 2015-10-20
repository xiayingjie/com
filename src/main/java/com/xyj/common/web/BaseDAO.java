package com.xyj.common.web;



import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.io.Serializable;

/**
 * @classDescription:抽出基本的DAO
 * @author:xiayingjie
 * @createTime:15/5/21
 */
@NoRepositoryBean//运行时不能创建实例
public  interface BaseDAO<T,ID extends Serializable> extends JpaSpecificationExecutor<T>, PagingAndSortingRepository<T, ID> {

    //计算数量------------
    //Long countByLastname(String lastname);

    //删除------------
    //Long deleteByLastname(String lastname);
    //List<User> removeByLastname(String lastname);

    //查询-----------find…By, read…By, query…By, count…By, and get...By
    //Between, LessThan, GreaterThan, Like  And Or
//     Enables the distinct flag for the query
//    List<Person> findDistinctPeopleByLastnameOrFirstname(String lastname, String firstname);
//    List<Person> findPeopleDistinctByLastnameOrFirstname(String lastname, String firstname);
//
//    // Enabling ignoring case for an individual property
//    List<Person> findByLastnameIgnoreCase(String lastname);
//    // Enabling ignoring case for all suitable properties
//    List<Person> findByLastnameAndFirstnameAllIgnoreCase(String lastname, String firstname);
//
//    // Enabling static ORDER BY for a query
//    List<Person> findByLastnameOrderByFirstnameAsc(String lastname);
//    List<Person> findByLastnameOrderByFirstnameDesc(String lastname);


//    //Person属性Address对象 ，Address对象里面有ZipCode 子查询
//    List<Person> findByAddress_ZipCode(ZipCode zipCode);

    //特殊参数处理-----
//    Page<User> findByLastname(String lastname, Pageable pageable);
//
//    Slice<User> findByLastname(String lastname, Pageable pageable);
//
//    List<User> findByLastname(String lastname, Sort sort);
//
//    List<User> findByLastname(String lastname, Pageable pageable);

    //查询部分记录   -----top---first Distinct
//    User findFirstByOrderByLastnameAsc();
//
//    User findTopByOrderByAgeDesc();
//
//    Page<User> queryFirst10ByLastname(String lastname, Pageable pageable);
//
//    Slice<User> findTop3ByLastname(String lastname, Pageable pageable);
//
//    List<User> findFirst10ByLastname(String lastname, Sort sort);
//
//    List<User> findTop10ByLastname(String lastname, Pageable pageable);

    // Java 8 Stream<T>---
//    @Query("select u from User u")
//    Stream<User> findAllByCustomQueryAndStream();
//
//    Stream<User> readAllByFirstnameNotNull();
//
//    @Query("select u from User u")
//    Stream<User> streamAllPaged(Pageable pageable);

    //异步查询结果
//-Use java.util.concurrent.Future as return type.
//    @Async
//    Future<User> findByFirstname(String firstname);
//-Use a Java 8 java.util.concurrent.CompletableFuture as return type.
//    @Async
//    CompletableFuture<User> findOneByFirstname(String firstname);
//-Use a org.springframework.util.concurrent.ListenableFuture as return type
//    @Async
//    ListenableFuture<User> findOneByLastname(String lastname);

    //--加载配置文件 如果是自定义Repositories，接口实现结尾是Impl 否则需要制定后缀 repository-impl-postfix=“”
//    <repositories base-package="com.acme.repositories">
//      <context:exclude-filter type="regex" expression=".*SomeRepository" />---移除
//              <include-filter /> ---包含
//    </repositories>
}
