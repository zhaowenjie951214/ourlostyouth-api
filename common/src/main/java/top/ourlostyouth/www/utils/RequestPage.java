package top.ourlostyouth.www.utils;

/**
 * @Author wudanjun
 * @Description：
 * @Date: Create in 13:55 2018/11/7
 * @Modified By:
 */
public class RequestPage {
    public static final String ORDER_DIRECTION_ASC = "ASC";
    public static final String ORDER_DIRECTION_DESC = "DESC";
    private Integer rows = Integer.valueOf(10);
    private Integer page = Integer.valueOf(1);
    protected long total = 0L;
    private String orderField = "id";
    private String orderDirection = "DESC";

    public RequestPage() {
    }

    public Integer getRows() {
        return this.rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getOrderField() {
        return this.orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrderDirection() {
        return this.orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }
}
