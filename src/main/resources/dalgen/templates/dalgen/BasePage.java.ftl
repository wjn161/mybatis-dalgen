<@pp.dropOutputFile />
<#list dalgen.pagings as paging>
    <@pp.changeOutputFile name = "/main/java/${paging.baseClassPath}/BasePage.java" />
package ${paging.basePackageName};

import java.util.List;

/**
 * 用于分页的工具类
 */
public class BasePage<T> {

    private List<T> datas;
    private int     total           = 0;
    private int     limit           = 20;
    private int     pageNos         = 1;
    private int     currPageNo      = 1;

    private boolean isFirstPage     = false;
    private boolean isLastPage      = false;
    private boolean hasPreviousPage = false;
    private boolean hasNextPage     = false;

    private int     navigatePages   = 8;
    private int[]   navigatePageNos;

    private void init() {
        this.pageNos = (this.total - 1) / this.limit + 1;

        if (currPageNo < 1) {
            this.currPageNo = 1;
        } else if (currPageNo > this.pageNos) {
            this.currPageNo = this.pageNos;
        } else {
            this.currPageNo = currPageNo;
        }
        calcNavigatePageNumbers();
        judgePageBoudary();
    }

    private void calcNavigatePageNumbers() {
        if (pageNos <= navigatePages) {
            navigatePageNos = new int[pageNos];
            for (int i = 0; i < pageNos; i++) {
                navigatePageNos[i] = i + 1;
            }
        } else {
            navigatePageNos = new int[navigatePages];
            int startNum = currPageNo - navigatePages / 2;
            int endNum = currPageNo + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                for (int i = 0; i < navigatePages; i++) {
                    navigatePageNos[i] = startNum++;
                }
            } else if (endNum > pageNos) {
                endNum = pageNos;
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatePageNos[i] = endNum--;
                }
            } else {
                for (int i = 0; i < navigatePages; i++) {
                    navigatePageNos[i] = startNum++;
                }
            }
        }
    }
    private void judgePageBoudary() {
        isFirstPage = currPageNo == 1;
        isLastPage = currPageNo == pageNos && currPageNo != 1;
        hasPreviousPage = currPageNo > 1;
        hasNextPage = currPageNo < pageNos;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        init();
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getPageNos() {
        return pageNos;
    }

    public int getCurrPageNo() {
        return currPageNo;
    }

    public int[] getNavigatePageNos() {
        return navigatePageNos;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public boolean hasPreviousPage() {
        return hasPreviousPage;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public void setCurrPageNo(int currPageNo){
        if(currPageNo==0){
            this.currPageNo =1;
        }else {
            this.currPageNo = currPageNo;
        }
    }

    public int getStartRow(){
        return (this.currPageNo-1)*this.limit;
    }

                public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[").append("total=").append(total).append(",pageNos=").append(pageNos)
                .append(",currPageNo=").append(currPageNo).append(",limit=").append(limit)
                .append(",isFirstPage=").append(isFirstPage).append(",isLastPage=")
                .append(isLastPage).append(",hasPreviousPage=").append(hasPreviousPage)
                .append(",hasNextPage=").append(hasNextPage).append(",navigatePageNos=");
        int len = navigatePageNos.length;
        if (len > 0)
            sb.append(navigatePageNos[0]);
        for (int i = 1; i < len; i++) {
            sb.append(" " + navigatePageNos[i]);
        }
        sb.append(",datas.size=" + datas.size());
        sb.append("]");
        return sb.toString();
    }
}
</#list>