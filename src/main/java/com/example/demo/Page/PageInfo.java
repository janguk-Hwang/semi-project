package com.example.demo.Page;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString

public class PageInfo {
    private final int pageNum;
    private final int rowBlockCount;
    private final int pageBlockCount;
    private final int totalRowCount;

    private final int startRow;
    private final int endRow;
    private final int totalPageCount;
    private final int startPageNum;
    private final int endPageNum;

    public PageInfo(int pageNum,
                    int rowBlockCount,
                    int pageBlockCount,
                    int totalRowCount) {
        this.pageNum = pageNum;
        this.rowBlockCount = rowBlockCount;
        this.pageBlockCount = pageBlockCount;
        this.totalRowCount = totalRowCount;

        startRow = (pageNum-1)*rowBlockCount+1;
        endRow = startRow+rowBlockCount-1;
        totalPageCount = (int)Math.ceil((double)totalRowCount/rowBlockCount);
        startPageNum = (pageNum-1)/pageBlockCount*pageBlockCount+1;
        int temp =this.startPageNum+this.pageBlockCount-1;
        this.endPageNum = Math.min(temp,this.totalPageCount);
    }
    public boolean isHasPrev(){
        return pageNum>1;
    }
    public boolean isHasNext(){
        return pageNum<totalPageCount;
    }
    public int getPrevPageNum(){
        return pageNum-1;
    }
    public int getNextPageNum(){
        return pageNum+1;
    }

}
