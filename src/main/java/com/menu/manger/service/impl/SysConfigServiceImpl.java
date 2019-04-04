package com.menu.manger.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.menu.manger.dto.SysConfig;
import com.menu.manger.mapper.SysConfigMapper;
import com.menu.manger.service.ISysConfigService;
import com.menu.manger.util.Convert;

/**
 * 参数配置 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService
{
    @Autowired
    private SysConfigMapper configMapper;

    /**
     * 查询参数配置信息
     * 
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public SysConfig selectConfigById(Long configId)
    {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return configMapper.selectConfig(config);
    }

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey)
    {
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = configMapper.selectConfig(config);
        return retConfig !=null ? retConfig.getConfigValue() : "";
    }

    /**
     * 查询参数配置列表
     * 
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config)
    {
        return configMapper.selectConfigList(config);
    }

    /**
     * 新增参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfig config)
    {
        return configMapper.insertConfig(config);
    }

    /**
     * 修改参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(SysConfig config)
    {
        return configMapper.updateConfig(config);
    }

    /**
     * 批量删除参数配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteConfigByIds(String ids)
    {
        return configMapper.deleteConfigByIds(Convert.toStrArray(ids));
    }

	@Override
	public SysConfig selectByKey(String key) {
		return configMapper.selectByKey(key);
	}

}
