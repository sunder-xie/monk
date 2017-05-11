package test.dubbo;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.monk.common.utils.JsonUtil;
import com.tqmall.ucenter.object.result.shop.ShopInfoDTO;
import com.tqmall.ucenter.service.shop.RpcShopInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zxg on 16/4/13.
 * 11:13
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@ContextConfiguration(locations = "classpath*:test-dubbo-consume.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UcTest {
    @Autowired
    private RpcShopInfoService rpcShopInfoService;

//    private String sources = "monk";
    private String sources = "MONK";
    @Test
    public void getByPhone(){
        String mobile = "15990076616";
        Result<ShopInfoDTO> result = rpcShopInfoService.getShopInfoByMobile(sources,mobile);

        System.out.println(JsonUtil.objectToJson(result));
    }

    @Test
    public void getById(){
        Integer id = 37890;
        Result<ShopInfoDTO> result = rpcShopInfoService.getShopInfoByShopId(sources, id);
        System.out.println(JsonUtil.objectToJson(result));

    }
}
