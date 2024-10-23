package com.messranger.model;

import java.util.List;

public class PageRequest {

    private Integer limit;

    private Long offset;

    private List<String> sortBy;

    private String sortDirection;

    public PageRequest(Integer limit, Long offset, List<String> sortBy) {
        this.limit = limit;
        this.offset = offset;
        this.sortBy = sortBy;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public List<String> getSortBy() {
        return sortBy;
    }

    public void setSortBy(List<String> sortBy) {
        this.sortBy = sortBy;
    }

}
