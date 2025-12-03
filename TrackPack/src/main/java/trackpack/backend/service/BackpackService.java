package trackpack.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import trackpack.backend.common.Page;
import trackpack.backend.controller.backpack.response.BackpackResponse;
import trackpack.backend.controller.rfidtag.response.RfidTagResponse;
import trackpack.backend.entity.Backpack;
import trackpack.backend.entity.RfidTag;
import trackpack.backend.exception.EnumExceptionType;
import trackpack.backend.exception.MyException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface BackpackService {

    /**
     * 激活背包
     * @param backpackId
     * @param backpackName
     * @param backpackBattery
     * @param backpackIpAddress
     * @param backpackMacAddress
     * @param backpackLocation
     * @return BackpackResponse
     * @throws MyException
     */
    BackpackResponse activateBackpack(String backpackId, String backpackName, Double backpackBattery,
                                             String backpackIpAddress, String backpackMacAddress, String backpackLocation) throws MyException;


    /**
     * 停用背包
     * @param backpackId
     * @return BackpackResponse
     * @throws MyException
     */
    BackpackResponse deleteBackpack(String backpackId);

    /**
     * 更新背包信息
     * @param backpackId
     * @param backpackName
     * @param backpackBattery
     * @param backpackLocation
     * @param backpackNetworkStatus
     * @return BackpackResponse
     * @throws MyException
     */
    BackpackResponse updateBackpack(String backpackId, String backpackName, Double backpackBattery,
                                           String backpackLocation, Integer backpackNetworkStatus) throws MyException;

    /**
     * 更新背包的某一个属性
     * @param backpackId
     * @param key (backpack_name: 背包名称, backpack_battery: 模块电量, backpackLocation: 位置, backpackNetworkStatus: 网络状态)
     * @param value
     * @return BackpackResponse
     * @throws MyException
     */
    BackpackResponse updateBackpackAttribute(String backpackId, String key, Object value) throws MyException;

    /**
     * 查询背包信息
     * @param backpackId
     * @return BackpackResponse
     * @throws MyException
     */
    BackpackResponse queryBackpack(String backpackId) throws MyException;

    /**
     * 查询背包的某个属性
     * @param backpackId
     * @param key (backpack_name: 背包名称, backpack_battery: 模块电量, backpackLocation: 位置, backpackNetworkStatus: 网络状态, backpackActivationTime: 激活时间, backpackIpAddress: IP地址, backpackMacAddress: MAC地址, backpackUserId: 用户ID, backpackRfidTagNum: RFID标签数量)
     * @return Object
     * @throws MyException
     */
    public Object queryBackpackAttribute(String backpackId, String key) throws MyException;

    /**
     * 查询背包的所有无源标签
     * @param backpackId
     * @param orderByCreateTimeDesc 是否按照创建时间倒叙排序 true 表示按照创建时间倒叙排序，false 表示按照创建时间正序排序 默认为 false
     * @param page 页码
     * @param pageSize 每页数量
     * @return List<RfidTagResponse>
     * @throws MyException
     */
    public Page<RfidTagResponse> queryBackpackRfidTags(String backpackId, Boolean orderByCreateTimeDesc, Integer page, Integer pageSize) throws MyException;
}
