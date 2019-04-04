package com.menu.manger.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.menu.manger.dto.Members;
import com.menu.manger.dto.MenuFood;
import com.menu.manger.dto.MyCollection;
import com.menu.manger.mapper.MyCollectionMapper;
import com.menu.manger.page.TableDataInfo;
import com.menu.manger.service.IMenuFoodService;
import com.menu.manger.service.impl.PosTransactionSerivce;
import com.menu.manger.util.AjaxResult;
import com.menu.manger.util.ThreadLocalUtil;

/**
 * 餐牌 信息操作处理
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Controller
@RequestMapping("/api/menuFood")
public class MenuFoodController extends BaseController
{    private static final Logger log = LoggerFactory.getLogger(PosTransactionSerivce.class);

	
	@Autowired
	private IMenuFoodService menuFoodService;
	@Autowired
	private MyCollectionMapper myCollerctMapper;
	
	/**
	 * 查询餐牌列表
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/getListByType.do")
	@ResponseBody
	public TableDataInfo getListByType(String json,HttpServletRequest request) throws UnsupportedEncodingException
	{
		request.setCharacterEncoding("utf-8");
		MenuFood menuFood =JSONObject.parseObject(json,MenuFood.class);
		startPage(menuFood.getPageNum(),menuFood.getPageSize(),null,null);
		menuFood.setIsValid("yes");
		log.info("query List By Type,json={}",json);
        List<MenuFood> list = menuFoodService.selectMenuFoodList(menuFood);
        if(menuFood.getBranchStoreId() !=null && list ==null || list.size() ==0){
        	 menuFood.setBranchStoreId(null);
        	 list = menuFoodService.selectMenuFoodList(menuFood);
        }
        for (MenuFood menuFood2 : list) {
        	MyCollection myCollection =new MyCollection();
			myCollection.setResourceId(menuFood2.getId());
			myCollection.setResourceTable("myCoupon");
			Members mem =(Members)ThreadLocalUtil.getUserInfo();
			myCollection.setMemuId(mem.getId());
			List<MyCollection> selectMyCollectionList = myCollerctMapper.selectMyCollectionList(myCollection);
			menuFood2.setIsColle("no");
			if(selectMyCollectionList !=null && selectMyCollectionList.size() >0){
				menuFood2.setIsColle("yes");
			}
		}
		return getDataTable(list);
	}
	
	/**
	 * 查询餐牌列表
	 */
	@RequestMapping("/selectById.do")
	@ResponseBody
	public AjaxResult selectById(Integer id)
	{
        return AjaxResult.success("", menuFoodService.selectMenuFoodById(id));
	}
	/**
	 * 查询餐牌列表
	 */
	@RequestMapping("/selectFoodType.do")
	@ResponseBody
	public AjaxResult selectFoodType()
	{
        return AjaxResult.success("", menuFoodService.selectFoodType());
	}
	/**
	 * 查询餐牌列表
	 */
	@RequestMapping("/getFoodByScose.do")
	@ResponseBody
	public AjaxResult getFoodByScose()
	{
		Integer score =getUserInfo().getScore();
		score = score ==null? 0:score;
		MenuFood foodByScose = menuFoodService.getFoodByScose(score);
		if(foodByScose ==null)
		{
			foodByScose =menuFoodService.getFoodByScoseEnd(getUserInfo().getScore());
		}
        return AjaxResult.success("", foodByScose);
	}
	/**
	 * 查询餐牌列表
	 */
	@RequestMapping("/requestExchege.do")
	@ResponseBody
	public AjaxResult requestExchege(Integer id,Integer num,String code){
		 return  menuFoodService.requestExchege(id,num,code,getUserInfo().getId());
		
	}
	/**
	 * 贈送
	 * @param id
	 * @return
	 */
	@RequestMapping("/giveAway.do")
	@ResponseBody
	public AjaxResult giveAway(@RequestParam("id") Integer id,@RequestParam("phone") String phone){
		 return  menuFoodService.giveAway(id,phone);
	}
}
