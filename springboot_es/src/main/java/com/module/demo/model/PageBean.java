package com.module.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PageBean {

    private List list;
    private Integer totalRecord;
    private Integer pageSize;
    private Integer totalPage;
    private Integer currentPage;
    private Integer showPage;
    private Integer beginPage;
    private Integer endPage;
    private String pageURL;
    private Object queryBean;

    public PageBean(List list, Integer totalRecord, Integer pageSize, Integer currentPage) {
        this.list = list;
        this.totalRecord = totalRecord;
        this.pageSize = pageSize;
        this.totalPage = countTotalPage(totalRecord, pageSize);
        this.currentPage = currentPage;
        this.showPage = 10;
        countBeginEnd(totalPage, currentPage, showPage);
    }

    private Integer countTotalPage(Integer totalRecord, Integer pageSize) {
        int totalPage = 0;
        if (totalRecord % pageSize == 0) {
            totalPage = totalRecord / pageSize;
        } else {
            totalPage = totalRecord / pageSize + 1;
        }
        return totalPage;
    }

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
