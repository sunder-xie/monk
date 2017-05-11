package com.tqmall.data.monk.biz.im;

import com.tqmall.data.monk.bean.entity.ImUserDO;
import com.tqmall.data.monk.common.bean.ImConstants;
import com.tqmall.data.monk.common.redis.RedisClientTemplate;
import com.tqmall.data.monk.common.redis.RedisKeyBean;
import com.tqmall.data.monk.common.utils.ClipboardDataUtil;
import com.tqmall.data.monk.dao.mapper.ImUserDOMapper;
import com.tqmall.data.monk.exterior.ali.AliUserExt;
import com.tqmall.data.monk.exterior.dubbo.users.StallUsersExt;
import com.tqmall.data.monk.exterior.dubbo.users.UcUsersExt;
import com.tqmall.tqmallstall.domain.result.seller.SellerDTO;
import com.tqmall.ucenter.object.result.shop.ShopDTO;
import com.tqmall.ucenter.object.result.shop.ShopInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Created by zxg on 16/4/12.
 * 15:20
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Slf4j
@Service
public class ImUserBizImpl implements ImUserBiz {

    @Value(value = "${project.version}")
    private String projectVersion;

    @Autowired
    private UcUsersExt ucUsersExt;
    @Autowired
    private StallUsersExt stallUsersExt;
    @Autowired
    private AliUserExt aliUserExt;

    @Autowired
    private ImUserDOMapper imUserDOMapper;

    @Autowired
    private RedisClientTemplate redisClientTemplate;

    @Override
    public Boolean insertUser(ImUserDO userDO) {
        Integer sysId = userDO.getSysId();
        String sysName = userDO.getSysName();
        if(sysId == null || sysName == null){
            return false;
        }

        //此处把云旺用户名和密码生成
        String aliUid = (projectVersion+ImConstants.SysName+sysName+sysId).toLowerCase();
        String aliPas = UUID.randomUUID().toString();

        userDO.setAliUid(aliUid);
        userDO.setAliPassword(aliPas);
        //插入用户其他数据
        Boolean insertOtherData = setUserBySysName(userDO);
        if(!insertOtherData){
            return false;
        }
        try {
            //向云旺注册用户
            String password = aliUserExt.insertUserToYunW(aliUid, aliPas);
            if(password != null) {
                userDO.setAliPassword(password);
                //数据库插入一条用户记录
                imUserDOMapper.insertSelective(userDO);
                log.info("add new user ok:{},{}", sysName, sysId);
                return true;
            }else {
                log.info("add new user wrong:{},{}", sysName, sysId);
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return false;
        }
    }

    @Override
    public ImUserDO getBySys(Integer sysId, String sysName) {
        if(sysId == null || sysName == null){
            return null;
        }
        //redis中寻找
        String key = String.format(RedisKeyBean.IM_USER_SYS_KEY, projectVersion, sysName, sysId);
        ImUserDO userDO = redisClientTemplate.lazyGet(key, ImUserDO.class);
//        ImUserDO userDO = null;
        if(null == userDO){
            userDO = imUserDOMapper.selectBySys(sysId, sysName);
            if(null != userDO){
                redisClientTemplate.lazySet(key,userDO,RedisKeyBean.RREDIS_EXP_MINUTE*10);
            }
        }
        return userDO;
    }

    @Override
    public ImUserDO getBySysWithInsert(Integer sysId, String sysName) {
        ImUserDO userDO = getBySys(sysId, sysName);
        if (userDO == null) {
            //不存在则插入
            userDO = new ImUserDO();
            userDO.setSysId(sysId);
            userDO.setSysName(sysName);
            Boolean isOk = insertUser(userDO);
            if (!isOk) {
                return null;
            }
        }
        userDO.setAppKey(ImConstants.AppKey);
        return userDO;
    }

    @Override
    public ImUserDO getByUid(String aliUid) {
        if(aliUid == null){
            return null;
        }
        //redis中寻找
        String key = String.format(RedisKeyBean.IM_USER_UID_KEY,aliUid);
        ImUserDO userDO = redisClientTemplate.lazyGet(key, ImUserDO.class);
        if(null == userDO){
            userDO = imUserDOMapper.selectByUid(aliUid);
            if(null != userDO){
                redisClientTemplate.lazySet(key,userDO,RedisKeyBean.RREDIS_EXP_MINUTE*10);
            }
        }
        return userDO;
    }


    /*===============private=====================*/
    //sysid：{uc:shop_id lop:seller_id yunpei:org_id+"_w"+warehouse_id}
    private Boolean setUserBySysName(ImUserDO userDO) {
        Integer sysId = userDO.getSysId();
        String sysName = userDO.getSysName();

        //判断是否是系统主动发的，sysId 为0
        if(sysId.equals(0)){
            String companyName = sysName+"系统";
            userDO.setUserName(companyName);
            userDO.setUserNickName(companyName);
            return true;
        }

        //此处根据不同的sysName选择不同的用户信息接入
        if(sysName.equals(ImConstants.SYSNAME_UC)){
            ShopInfoDTO shopInfoDTO = ucUsersExt.getUsersById(sysId);
            if(null == shopInfoDTO){
                return false;
            }
            ShopDTO shopDTO = shopInfoDTO.getShopDTO();
            if(shopDTO == null){
                return false;
            }
            userDO.setUserName(shopDTO.getCompanyName());
            userDO.setUserNickName(shopDTO.getCompanyName());
        }else if(sysName.equals(ImConstants.SYSNAME_LOP)){
            SellerDTO sellerLoginInfoDTO = stallUsersExt.getUsersById(sysId);
            if(null == sellerLoginInfoDTO){
                return false;
            }
            userDO.setUserName(sellerLoginInfoDTO.getCompanyName());
            userDO.setUserNickName(sellerLoginInfoDTO.getCompanyName());
            userDO.setUserPhone(sellerLoginInfoDTO.getSellerMobile());

        }else if(sysName.equals(ImConstants.SYSNAME_YUN_PEI)){
            //云配用户信息的维护
            SellerDTO sellerLoginInfoDTO = stallUsersExt.getSellerByOrgId(sysId);
            if(null == sellerLoginInfoDTO){
                return false;
            }
            String companyName = sellerLoginInfoDTO.getCompanyName();
            if(companyName == null || companyName.trim().equals("")){
                log.info("userName is empty.sysName:{}",sysName);
                return false;
            }
            companyName = "[云配]"+companyName;
            userDO.setUserName(companyName);
            userDO.setUserNickName(companyName);
            userDO.setUserPhone(sellerLoginInfoDTO.getSellerMobile());

        }else if(sysName.equals(ImConstants.SYSNAME_YUN_XIU)){
            //todo 云修用户信息的维护

        }else if(sysName.equals(ImConstants.SYSNAME_IM_SERVER)){
            //客服信息的维护
            ImServerUser(sysId,userDO);
        }else{
            return false;
        }

        String userName = userDO.getUserName();
        if(userName == null || userName.trim().equals("")){
            log.info("userName is empty.sysName:{}",sysName);
            return false;
        }

        return true;

    }

    /**
     * 判断剪切板中圖片的base64字符串
     */
    public String getBase64StrFromClipBoard(){
        return ClipboardDataUtil.getBase64StrFromClipBoard();
    }

    //设置客服基本信息
    private void ImServerUser(Integer sysId,ImUserDO userDO){
        if(sysId.equals(1)){
            String name = "全车件客服-阿勇";
            userDO.setUserName(name);
            userDO.setUserNickName(name);
        }
    }
}
