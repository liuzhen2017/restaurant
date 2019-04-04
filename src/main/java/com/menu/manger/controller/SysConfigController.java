package com.menu.manger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.menu.manger.dto.SysConfig;
import com.menu.manger.service.ISysConfigService;

/**
 * 参数配置 信息操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/api/config")
public class SysConfigController extends BaseController
{

    @Autowired
    private ISysConfigService configService;


    /**
     * 查询参数配置列表
     */
    @RequestMapping("queryByKey.do")
    @ResponseBody
    public String queryByKey(String key)
    {
        return configService.selectConfigByKey(key);
        
    }


    /**
     * 查询参数配置列表
     */
    @RequestMapping("queryBuyVIP.do")
    @ResponseBody
    public SysConfig queryBuyVIP()
    {
        return configService.selectByKey("VIPPrice");
        
    }
    /**
     * 查询参数配置列表
     */
    @RequestMapping("payUserWEbKey.do")
    @ResponseBody
    public SysConfig payUserWEbKey()
    {
        return configService.selectByKey("pay_user_web_key");
        
    }
}
