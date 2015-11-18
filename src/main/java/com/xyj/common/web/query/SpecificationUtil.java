package com.xyj.common.web.query;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @classDescription:搜索过滤工具类
 * @author:xiayingjie
 * @createTime:13-10-22 上午10:23
 */
public class SpecificationUtil {

    /**
     * 根据List获取查询对象
     * @param filters
     * @param entityClazz
     * @param <T>
     * @return
     */
    public static <T> Specification<T> bySearchFilter(final List<PropertyFilter> filters, final Class<T> entityClazz) {
           return new Specification<T>() {
               @Override
               public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                   if(null!=filters && !filters.isEmpty()){
                      List<Predicate> predicates = Lists.newArrayList();

                      for(PropertyFilter pf:filters){
                          Path path;
                          if(pf.getPropertyName().indexOf("_")>-1){
                              String[]names=pf.getPropertyName().split("_");
                                  path=root.get(names[0]).get(names[1]);
                          }else{
                              path=root.get(pf.getPropertyName());
                          }
                          if(null==pf.getPropertyValue()){
                              continue;
                          }
                          Object value=pf.getPropertyValue();

                          if(pf.getPropertyType().equals(Date.class)){
                              value=new Date(pf.getPropertyValue().toString());
                          }


                          switch(pf.getMatchType()){
                              case EQ:
                                  predicates.add(cb.equal(path,value));
                                  break;
                              case LIKE:
                                  predicates.add(cb.like(path, "%" + value + "%"));
                                  break;
                              case LT:
                                  predicates.add(cb.lessThan(path, (Comparable) value));
                                  break;
                              case GT:
                                  predicates.add(cb.greaterThan(path, (Comparable) value));
                                  break;
                              case LE:
                                  predicates.add(cb.lessThanOrEqualTo(path, (Comparable) value));
                                  break;
                              case GE:
                                  predicates.add(cb.greaterThanOrEqualTo(path, (Comparable) value));
                                  break;
                              case IN:
                                  predicates.add(path.in(String.valueOf(value).split(",")));
                                  break;
                              case NIN:
                                  //暂时没实现
                                  predicates.add(null);
                                  break;
                          }


                      }
                       if(predicates.size()>0){
                           return cb.and(predicates.toArray(new Predicate[predicates.size()]));
                       }
//                       for( Predicate predicate:predicates){
//                           query.where(predicate);
//                       }

                   }

                   return cb.conjunction();
               //    return query.getRestriction();
               }
           } ;
    }

    /**
     * 根据map获取查询对象
     * @param map
     * @param entityClazz
     * @param <T>
     * @return
     */
    public static <T> Specification<T> bySearchFilter(Map<String ,String> map, final Class<T> entityClazz) {
        PropertyFilter pf=new PropertyFilter();
        for(String key:map.keySet()){
            if(!StringUtils.isBlank(map.get(key))){
               pf.addFilter(StringUtils.substringAfter(key,"_"),map.get(key));
            }
        }
        return  bySearchFilter(pf.getFilterList(),entityClazz);
    }

    /**
     * 获取分页对象
     * @param queryDTO
     * @return
     */
    public static PageRequest getPageRequest(QueryDTO queryDTO){
         Sort sort=null;
         if(!StringUtils.isBlank(queryDTO.getOrder())){
             if(StringUtils.startsWith(queryDTO.getOrder(),"-")){
                 sort = new Sort(Sort.Direction.DESC,StringUtils.substringAfter(queryDTO.getOrder(),"-"));
             }else{
                 sort = new Sort(Sort.Direction.ASC, queryDTO.getOrder());
             }

         }
         return new PageRequest(queryDTO.getPage(),queryDTO.getSize(),sort);
    }
}
