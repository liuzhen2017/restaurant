
package storelle.api.pos;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="result">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="errCode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *               &lt;enumeration value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="errMsg" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Ҫ�����_"/>
 *               &lt;enumeration value="���T̖�a�����_"/>
 *               &lt;enumeration value="���Tδ춵�䁵�ӛ"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="member" type="{https://api.storellet.com/storellet/api/pos}member" minOccurs="0"/>
 *         &lt;element name="points" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="discount" type="{https://api.storellet.com/storellet/api/pos}discount" minOccurs="0"/>
 *         &lt;element name="coupons" type="{https://api.storellet.com/storellet/api/pos}coupon" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "result",
    "errCode",
    "errMsg",
    "member",
    "points",
    "discount",
    "coupon"
})
@XmlRootElement(name = "memberEnquiryResponse")
public class MemberEnquiryResponse {

    protected int result;
    protected Integer errCode;
    protected String errMsg;
    protected Member member;
    protected int points;
    protected Discount discount;
    protected List<Coupon> coupon;

    public void setCoupons(List<Coupon> coupon) {
		this.coupon = coupon;
	}

	/**
     * ��ȡresult���Ե�ֵ��
     * 
     */
    public int getResult() {
        return result;
    }

    /**
     * ����result���Ե�ֵ��
     * 
     */
    public void setResult(int value) {
        this.result = value;
    }

    /**
     * ��ȡerrCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getErrCode() {
        return errCode;
    }

    /**
     * ����errCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setErrCode(Integer value) {
        this.errCode = value;
    }

    /**
     * ��ȡerrMsg���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrMsg() {
        return errMsg;
    }

    /**
     * ����errMsg���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrMsg(String value) {
        this.errMsg = value;
    }

    /**
     * ��ȡmember���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Member }
     *     
     */
    public Member getMember() {
        return member;
    }

    /**
     * ����member���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Member }
     *     
     */
    public void setMember(Member value) {
        this.member = value;
    }

    /**
     * ��ȡpoints���Ե�ֵ��
     * 
     */
    public int getPoints() {
        return points;
    }

    /**
     * ����points���Ե�ֵ��
     * 
     */
    public void setPoints(int value) {
        this.points = value;
    }

    /**
     * ��ȡdiscount���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Discount }
     *     
     */
    public Discount getDiscount() {
        return discount;
    }

    /**
     * ����discount���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Discount }
     *     
     */
    public void setDiscount(Discount value) {
        this.discount = value;
    }

	public List<Coupon> getCoupon() {
		return coupon;
	}

	public void setCoupon(List<Coupon> coupon) {
		this.coupon = coupon;
	}


}
