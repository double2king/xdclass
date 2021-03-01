package com.def.controller;


import com.def.config.JsonData;
import com.def.enums.BizCodeEnum;
import com.def.exception.BizException;
import com.def.model.AddressDO;
import com.def.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 电商-公司收发货地址表 前端控制器
 * </p>
 *
 * @author wr
 * @since 2021-02-20
 */
@Api(tags="地址收货请求",description = "地址:描述")
@RestController
@RequestMapping("/api/address/v1")
public class AddressController {

    @Autowired
    AddressService addressService;

    @ApiOperation("默认方法")
    @GetMapping("/def")
    public void def() {

    }

    @ApiOperation("添加地址信息")
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public JsonData add(@RequestBody AddressDO addressDO) {
        addressService.save(addressDO);
        throw new BizException(BizCodeEnum.ACCOUNT_PWD_ERROR);
        //return JsonData.buildSuccess("添加成功");
    }

}

