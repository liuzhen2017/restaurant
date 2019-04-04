package com.menu.manger.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.WriterException;
import com.menu.manger.dto.MyCoupon;
import com.menu.manger.page.TableDataInfo;
import com.menu.manger.service.IMyCouponService;
import com.menu.manger.util.AjaxResult;
import com.menu.manger.util.QrCodeCreateUtil;

/**
 * 我的优惠券 信息操作处理
 * 
 * @author liuzhen
 * @date 2019-02-15
 */
@Controller
@RequestMapping("/api/myCoupon")
public class MyCouponController extends BaseController
{
    private String prefix = "api/myCoupon";
	
	@Autowired
	private IMyCouponService myCouponService;
	
	@RequiresPermissions("api:myCoupon:view")
	@GetMapping()
	public String myCoupon()
	{
	    return prefix + "/myCoupon";
	}
	
	/**
	 * 查询我的优惠券列表
	 */
	@PostMapping("/list.do")
	@ResponseBody
	public TableDataInfo list(MyCoupon myCoupon)
	{
		startPage();
		myCoupon.setMembersId(getUserInfo().getId());
        List<MyCoupon> list = myCouponService.selectMyCouponList(myCoupon);
		return getDataTable(list);
	}
	/**
	 * 查询我的优惠券列表
	 */
	@PostMapping("/isVaild.do")
	@ResponseBody
	public TableDataInfo isVaild(MyCoupon myCoupon)
	{
		startPage();
		myCoupon.setMembersId(getUserInfo().getId());
        List<Map<String, Object>> list = myCouponService.isVaild(myCoupon);
		return getDataTable(list);
	}
	
	@RequestMapping("/queryByCode.do")
	@ResponseBody
	public AjaxResult queryByCode(Integer id)
	{		
		MyCoupon myCoupon =new MyCoupon();
		//myCoupon.setMembersId(getUserInfo().getId());
		myCoupon.setId(id);
		List<MyCoupon> list = myCouponService.selectMyCouponList(myCoupon);
		if(list ==null || list.size() ==0){
			return AjaxResult.error("優惠券錯誤!");
		}
		return AjaxResult.success(null,list.get(0));
	}
	/**
	 * 查询会员二維碼
	 */
	@GetMapping("/queryQC.do")
	public void queryMembersQC(HttpServletResponse response,String code)
	{
		OutputStream outputStream;
		try {
			outputStream = response.getOutputStream();
			QrCodeCreateUtil.createQrCode(outputStream, code, 900, "JPEG");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 消费优惠券
	 * @param code
	 * @param status
	 * @param msg
	 * @return
	 */
	@RequestMapping("/userMyCoupon.do")
	@ResponseBody
	public AjaxResult userMyCoupon(@RequestParam String couponCode,@RequestParam String status,@RequestParam String msg,@RequestParam String branchStoreId,@RequestParam String userNo)
	{		
		
		return myCouponService.userMyCoupon(couponCode,status,msg,branchStoreId,userNo);
		
	}
	
	/**
	 * 修改保存我的优惠券
	 */
	@PostMapping("/edit.do")
	@ResponseBody
	public AjaxResult editSave(MyCoupon myCoupon)
	{		
		return toAjax(myCouponService.updateMyCoupon(myCoupon));
	}
	
	/**
	 * 删除我的优惠券
	 */
	@PostMapping( "/remove.do")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(myCouponService.deleteMyCouponByIds(ids));
	}
	/**
	 * 根據code計算VIP折扣
	 * @param code
	 * @return
	 */
	@RequestMapping( "/calVIPCode.do")
	@ResponseBody
	public AjaxResult calVIPCode(String code) {
		return myCouponService.calVIPCode(code);
	}
	
}
