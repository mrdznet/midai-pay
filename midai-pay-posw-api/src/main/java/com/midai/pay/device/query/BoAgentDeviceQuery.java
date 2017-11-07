package com.midai.pay.device.query;

import com.midai.framework.query.PaginationQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
public class BoAgentDeviceQuery extends PaginationQuery implements Serializable {

    private static final long serialVersionUID = 1935100134231549471L;

    @ApiModelProperty("机身号始")
    private String bodyNoStart;

    @ApiModelProperty("数量")
    private Integer num;

    private String agentNo;

    /*private String bodyNoEnd;*/

    private String bodyNos;
}
