package com.tqmall.data.monk.exterior.dubbo.users;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.monk.common.utils.JsonUtil;
import com.tqmall.tqmallstall.domain.result.seller.SellerDTO;
import com.tqmall.tqmallstall.service.openplatform.RpcSellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zxg on 16/4/26.
 * 14:40
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Slf4j
@Service
public class StallUsersExt {
    @Autowired
    private RpcSellerService rpcSellerService;


    //
    public SellerDTO getUsersById(Integer sellId){

        try {
            log.info("get users from stall,sellerId:{}", sellId);
            Result<SellerDTO> result = rpcSellerService.getById(sellId);
            log.info("get users from stall :{}", JsonUtil.objectToJson(result));

            if(result.isSuccess()){
                return result.getData();
            }
            return null;
        } catch (Exception e) {
            log.error("get users error from stall :",e);
            return null;
        }
    }

    public SellerDTO getSellerByOrgId(Integer orgId) {
        log.info("get seller by org id, orgId:{}", orgId);

        Result<SellerDTO> result = rpcSellerService.getSellerByOrgId(orgId);
        if(result!=null && result.isSuccess()){
            return result.getData();
        }

        log.info("get seller by org id, failed, result:{}", JsonUtil.objectToJson(result));
        return null;
    }
}
