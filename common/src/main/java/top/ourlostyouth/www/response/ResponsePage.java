package top.ourlostyouth.www.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Author wudanjun
 * @Description：返回的page对象，只包含数据和页码
 * @Date: Create in 9:15 2018/3/29
 * @Modified By:
 */
@ApiModel(value = "返回的页面对象")
public class ResponsePage<T> {
    @ApiModelProperty(value = "数据集合")
    List<T> rows;
    @ApiModelProperty(value = "数据总数")
    long total;


    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public ResponsePage() {
    }

    public ResponsePage(List rows, Long total) {
        this.rows = rows;
        this.total = total;
    }
}
