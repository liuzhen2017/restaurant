package storelle.api.pos;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


/**
 * @author liuzhen
 * 
 */
@WebService(targetNamespace="http://meokbang.com.hk/storelle/api/pos")
public interface IPosTransaction {
	/**
	 * 根据会员ID 和手机号查询是否存在
	 * @param memberID
	 * @param phone
	 * @param brandID
	 * @param t
	 * @param h
	 * @return
	 */
	@WebMethod(action="http://meokbang.com.hk/storelle/api/pos")
	public MemberEnquiryResponse memberEnquiry(@WebParam(name="memberID") String memberID,@WebParam(name="phone")String phone,@WebParam(name="brandID")String brandID,@WebParam(name="t")String t,@WebParam(name="h")String h,@WebParam(name="pos") String pos);
	
	/**
	 * 兑换优惠券 
	 * @param memberID 会员ID
	 * @param brandID 店铺ID
	 * @param shopCode 商品code
	 * @param redeemDatetime 兑换日期
	 * @param couponID 优惠券编号
	 * @param serialNo 序列号
	 * @param couponStatus 优惠券状态 1已兑现 2有效
	 * @param t
	 * @param h
	 * @return
	 */
	@WebMethod(action="http://meokbang.com.hk/storelle/api/pos")
	public SubmitRedeemptionResponse submitRedeemption(@WebParam(name="memberID") String memberID,@WebParam(name="brandID") String brandID,@WebParam(name="shopCode") String shopCode,@WebParam(name="redeemDatetime") String redeemDatetime,@WebParam(name="couponID") String couponID,
			@WebParam(name="serialNo") String serialNo,
			@WebParam(name="couponStatus") String couponStatus,
			@WebParam(name="t") String t,
			@WebParam(name="h") String h
			,@WebParam(name="pos") String pos);
	/**
	 * 提交交易
	 * @param brandID 店铺ID
	 * @param transactionDatetime 交易时间
	 * @param shopCode 商品代码
	 * @param invoiceNo  发票号码
	 * @param invoiceAmount 发票金额 
	 * @param netAmount 净额
	 * @param pax 商品数量
	 * @param coupons 优惠券集合
	 * @param memberID 会员ID
	 * @param t
	 * @param h
	 * @return
	 */
	@WebMethod(action="http://meokbang.com.hk/storelle/api/pos")
	public CloseTransactionResponse closeTransaction(@WebParam(name="brandID")String brandID,@WebParam(name="transactionDatetime")String transactionDatetime,@WebParam(name="shopCode")String shopCode,@WebParam(name="invoiceNo")String invoiceNo,
			@WebParam(name="invoiceAmount")String invoiceAmount,@WebParam(name="netAmount")String netAmount,@WebParam(name="pax")String pax,@WebParam(name="coupons")String coupons,@WebParam(name="memberID")String memberID,
			@WebParam(name="t")String t,@WebParam(name="h")String h,@WebParam(name="pos")String pos);
	/**
	 * 撤销交易
	 * @param brandID 店铺ID
	 * @param transactionDatetime 交易时间
	 * @param invoiceNo 发票号码
	 * @param t 
	 * @param h
	 * @return
	 */
	@WebMethod(action="http://meokbang.com.hk/storelle/api/pos")
	public ReverseTransactionResponse reverseTransaction(@WebParam(name="brandID")String brandID,@WebParam(name="transactionDatetime")String transactionDatetime,@WebParam(name="invoiceNo")String invoiceNo,@WebParam(name="t")String t,@WebParam(name="h")String h,@WebParam(name="pos") String pos);
	
}
