package com.example.demo.pageinfo;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageInfo {
    private final int pageNum; //페이지번호 0개의 사용위치
    private final int rowBlockCount; //한페이지에 보여질 행의 갯수
    private final int pageBlockCount;//한페이지에 보여질 페이지 갯수
    private final int totalRowCount; //전체 글의 갯수
    //계산결과
    private final int startRow; //시작행번호
    private final int endRow; //끝행번호
    private final int totalPageCount; //전체페이지의 갯수
    private final int startPageNum; //시작페이지
    private final int endPageNum; //끝페이지

    /**
     *
     * @param pageNum 페이지번호
     * @param rowBlockCount 한페이지에 보여질 행의 갯수
     * @param pageBlockCount 한페이지에 보여질 페이지 갯수
     * @param totalRowCount 전체 행의 갯수
     */


    public PageInfo(int pageNum,
                    int rowBlockCount,
                    int pageBlockCount,
                    int totalRowCount){
        this.pageNum=pageNum;
        this.rowBlockCount=rowBlockCount;
        this.pageBlockCount=pageBlockCount;
        this.totalRowCount=totalRowCount;

        startRow=(pageNum-1)*rowBlockCount+1;
        endRow=startRow+rowBlockCount-1;
        totalPageCount=(int)Math.ceil(totalRowCount/(double)rowBlockCount);
        //start
        startPageNum=(pageNum-1)/pageBlockCount*pageBlockCount+1;
        //if문
        int tempEndPageNum = this.startPageNum+this.pageBlockCount-1;
        this.endPageNum=Math.min(tempEndPageNum,this.totalPageCount);
    }
}

