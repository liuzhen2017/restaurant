package com.menu.manger.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.menu.manger.dto.MyCollection;
import com.menu.manger.page.TableDataInfo;
import com.menu.manger.service.IMyCollectionService;
import com.menu.manger.util.AjaxResult;

/**
 * 收藏 信息操作处理
 * 
 * @author liuzhen
 * @date 2019-02-15
 */
@Controller
@RequestMapping("/api/myCollection")
public class MyCollectionController extends BaseController
{
    private String prefix = "api/myCollection";
	
	@Autowired
	private IMyCollectionService myCollectionService;
	
	@GetMapping()
	public String myCollection()
	{
	    return prefix + "/myCollection";
	}
	
	/**
	 * 查询收藏列表
	 */
	@PostMapping("/list.do")
	@ResponseBody
	public TableDataInfo list(MyCollection myCollection)
	{
		startPage();
		myCollection.setMemuId(getUserInfo().getId());
		if(myCollection.getType().equals(1)){
			return getDataTable(myCollectionService.selectMyShop(myCollection));	
		}else{
			return getDataTable(myCollectionService.selectMyCoup(myCollection));
		}
	}
	
	
	
	
	/**
	 * 新增保存收藏
	 */
	@PostMapping("/addOrRemove.do")
	@ResponseBody
	public AjaxResult addOrRemove(Integer tableId,Integer type,String picUrl)
	{		
		MyCollection myCollection =new MyCollection();
		myCollection.setMemuId(getUserInfo().getId());
		myCollection.setResourceId(tableId);
		myCollection.setType(type);
		if(type ==1){
			myCollection.setResourceTable("myShop");
		}else if(type ==2){
			myCollection.setResourceTable("myCoupon");
		}
		List<MyCollection> myCollectionList = myCollectionService.selectMyCollectionList(myCollection);
		if(myCollectionList !=null && myCollectionList.size() >0){
			myCollectionService.deleteMyCollectionByIds(myCollectionList.get(0).getId()+"");
			return AjaxResult.success("刪除收藏成功!");
		}else{
			myCollection.setPicUrl(picUrl);
			myCollection.setCreateDate(new Date());
			myCollectionService.insertMyCollection(myCollection);
			return AjaxResult.success("添加收藏成功!");
		}
	}
	
	
}
