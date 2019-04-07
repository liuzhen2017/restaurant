/**
 * 
 */
package storelle.api.pos;

import java.text.ParseException;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.menu.manger.service.IPosTransactionSerivce;

/**
 * @author liuzhen
 *
 */
@WebService(serviceName="pos", targetNamespace="http://flqd.majiangyun.com:8899/storelle/api/pos",endpointInterface="storelle.api.pos.IPosTransaction")
@Component
public class PosTransaction implements IPosTransaction {



	@Autowired
	IPosTransactionSerivce posTransService;
	/* (non-Javadoc)
	 * @see com.menu.manger.soap.IPosTransaction#queryMemberById(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@WebMethod(operationName="memberEnquiry")
	@Override
	public MemberEnquiryResponse memberEnquiry(String memberID, String phone,
			String brandID, String t, String h,String pos) {
		MemberEnquiryResponse response =new MemberEnquiryResponse();
		try {
			return   posTransService.queryMemberById(memberID, phone, brandID, t, h);
		} catch (ParseException e) {
			e.printStackTrace();
			response.setResult(1);
			response.setErrCode(2);
			response.setErrMsg(e.getMessage());
		}
		return response;
		
	}

	@WebMethod(operationName="submitRedeemption")
	@Override
	public SubmitRedeemptionResponse submitRedeemption(String memberID, String brandID,
			String shopCode, String redeemDatetime, String couponID,
			String serialNo, String couponStatus, String t, String h,String pos) {
		try {
			return posTransService.submitRedemption(memberID, brandID, shopCode, redeemDatetime, couponID, serialNo, couponStatus, t, h);
		} catch (Exception e) {
			e.printStackTrace();
			SubmitRedeemptionResponse response =new SubmitRedeemptionResponse();
			response.setResult(1);
			response.setErrCode(2);;
			response.setErrMsg(e.getMessage());;
			return response;
		}
	}

	@Override
	@WebMethod(operationName="closeTransaction")
	public CloseTransactionResponse closeTransaction(String brandID, String transactionDatetime,
			String shopCode, String invoiceNo, String invoiceAmount,
			String netAmount, String pax, String coupons, String memberID,
			String t, String h,String pos) {
		try {
			return posTransService.closeTransaction(brandID, transactionDatetime, shopCode, invoiceNo, invoiceAmount, netAmount, pax, coupons, memberID, t, h);
		} catch (Exception e) {
			e.printStackTrace();
			CloseTransactionResponse response =new CloseTransactionResponse();
			response.setStatus(1);
			response.setErrCode(2);;
			response.setResult(1);
			response.setErrMsg(e.getMessage());;
			return response;
		}
	}

	@Override
	@WebMethod(operationName="reverseTransaction")
	public ReverseTransactionResponse reverseTransaction(String brandID, String transactionDatetime,
			String invoiceNo, String t, String h,String pos) {
		try {
			return posTransService.backTransaction(brandID, transactionDatetime, invoiceNo, t, h);
		} catch (Exception e) {
			e.printStackTrace();
			ReverseTransactionResponse response =new ReverseTransactionResponse();
			response.setResult(1);
			response.setStatus(1);
			response.setErrCode(2);;
			response.setErrMsg(e.getMessage());;
			return response;
		}
	}
}