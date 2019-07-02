package com.module.demo.model;

import lombok.Data;

import java.util.List;

/* 分页Bean */
@Data
public class PageBean {

    private List list; // 分页列表
    private Integer totalRecord; // 总记录数
    private Integer pageSize; // 每页记录数
    private Integer totalPage; // 总页数
    private Integer currentPage; // 当前页
    private Integer showPage; // 展示多少页
    private Integer beginPage; // 头页（展示页的第一页）
    private Integer endPage; // 尾页（展示页的最后一页）
    private String pageURL; // 分页URL
    private Object queryBean; // 查询条件实体类

    /* 非编译器提供的默认无参构造器 */
    public PageBean() {
    }

    /* 分页构造函数 */
    public PageBean(List list, Integer totalRecord, Integer pageSize, Integer currentPage) {
        this.list = list;
        this.totalRecord = totalRecord;
        this.pageSize = pageSize;
        this.totalPage = countTotalPage(totalRecord, pageSize);
        this.currentPage = currentPage;
        this.showPage = 10;
        countBeginEnd(totalPage, currentPage, showPage);
    }

    /* 计算总页数 */
    private Integer countTotalPage(Integer totalRecord, Integer pageSize) {
        int totalPage = 0;
        if (totalRecord % pageSize == 0) {
            totalPage = totalRecord / pageSize;
        } else {
            totalPage = totalRecord / pageSize + 1;
        }
        return totalPage;
    }

    /* 计算头尾页 */
    private void countBeginEnd(Integer totalPage, Integer currentPage, Integer showPage) {
        int beginPage = currentPage;
        int endPage = currentPage + showPage - 1;
        if (endPage > totalPage) {
            endPage = totalPage;
            beginPage = totalPage - showPage + 1;
        }
        if (beginPage < 1) {
            beginPage = 1;
        }
        this.beginPage = beginPage;
        this.endPage = endPage;
    }

}
