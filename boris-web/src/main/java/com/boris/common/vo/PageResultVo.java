package com.boris.common.vo;

import java.util.List;

public class PageResultVo<T> {
    //总条数
    private long count;

    //当前页数据
    private List<T> results;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
