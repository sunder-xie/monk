package test.dubbo;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.monk.common.utils.JsonUtil;
import com.tqmall.part.dubbo.base.WarehouseDTO;
import com.tqmall.part.dubbo.base.WarehouseRemoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * Created by zxg on 16/9/5.
 * 19:05
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@ContextConfiguration(locations = "classpath*:test-dubbo-consume.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class partTest {

    @Autowired
    private WarehouseRemoteService warehouseRemoteService;

    @Test
    public void getByWareHouse() {
//        Integer wareHouse = 22125;
//        Integer wareHouse = 12058;
//        Result<WarehouseDTO> result = warehouseRemoteService.queryWarehouseById(wareHouse);

        Integer org_id = 50364;
        Result<ArrayList<WarehouseDTO>> result =  warehouseRemoteService.queryWarehouseByOrgId(org_id);

        System.out.println(JsonUtil.objectToJson(result));

    }
}
