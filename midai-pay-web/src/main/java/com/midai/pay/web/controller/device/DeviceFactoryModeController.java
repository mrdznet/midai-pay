package com.midai.pay.web.controller.device;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.query.PageVo;
import com.midai.pay.device.query.BoFactoryModeQuery;
import com.midai.pay.device.service.BoFactoryService;
import com.midai.pay.device.vo.DeviceFactoryModeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiModel("设备厂商型号管理")
@RestController
@RequestMapping("/device/factory")
public class DeviceFactoryModeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceFactoryModeController.class);

    @Reference
    private BoFactoryService factoryService;

    @ApiOperation("设备厂商型号列表")
    @PostMapping("/list")
    public PageVo<DeviceFactoryModeVo> factoryModeList(@RequestBody BoFactoryModeQuery query){
        if(query==null){
            throw new RuntimeException("设备厂商型号列表查询参数非法：值为:"+query);
        }

        return factoryService.paginageAllDeviceMode(query);
    }
}
