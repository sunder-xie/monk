package com.tqmall.data.monk.exterior.dubbo.users;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.monk.common.bean.ImConstants;
import com.tqmall.data.monk.common.utils.JsonUtil;
import com.tqmall.ucenter.object.result.shop.ShopInfoDTO;
import com.tqmall.ucenter.service.shop.RpcShopInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zxg on 16/4/13.
 * 11:31
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Slf4j
@Service
public class UcUsersExt {
    @Autowired
    private RpcShopInfoService rpcShopInfoService;

    public ShopInfoDTO getUsersById(Integer shopId){
        try {
            log.info("get users from uc,shopId:{}", shopId);
            Result<ShopInfoDTO> result = rpcShopInfoService.getShopInfoByShopId(ImConstants.SysName, shopId);
            log.info("get users from uc :{}", JsonUtil.objectToJson(result));

            if(result.isSuccess()){
                return result.getData();
            }
            return null;
        } catch (Exception e) {
            log.error("get users error from uc :",e);
            return null;
        }
    }


}
