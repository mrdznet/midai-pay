package com.midai.pay.web.vo.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class DeviceIostorageCheckResult<T> implements Serializable {

	private static final long serialVersionUID = -373403141237589181L;
	
	@ApiModelProperty("检查结果-success或fail")
    private String result;
    @ApiModelProperty("检查fail后,错误的编号列表")
    private List<String> existsResult;
    @ApiModelProperty("检查success时,返回正常编号列表")
    private T successResult;
    @ApiModelProperty("检查fail后,返回的消息")
    private String errorMsg;

    public enum ResultType{
        SUCCESS,
        FAIL;
    }
}
