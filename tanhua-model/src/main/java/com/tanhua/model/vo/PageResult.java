package com.tanhua.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 12:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult implements Serializable {

    private Integer page = 0; // 当前页
    private Integer pagesize = 10; // 每页大小
    private Long pages = 0l; // 总页数
    private Long counts = 0l; // 总记录数

    private List<?> items = Collections.emptyList(); // 每页条目集合

    public PageResult(Integer page, Integer pagesize, Long counts, List items){
        this.page = page;
        this.pagesize = pagesize;
        this.counts = counts;
        this.items = items;
        this.pages = counts / pagesize == 0 ? counts / pagesize : counts / pagesize + 1;
    }


}
