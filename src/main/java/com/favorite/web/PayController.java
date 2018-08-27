package com.favorite.web;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.favorite.comm.aop.LoggerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by cdc on 2018/8/22.
 */
@Controller
@RequestMapping("/")
public class PayController extends BaseController{

    @Value("${APP_ID}")
    private static String APP_ID;
    @Value("${RSA_PRIVATE_KEY")
    private static String RSA_PRIVATE_KEY;
    @Value("${NOTIFY_URL}")
    private static String NOTIFY_URL ;
    @Value("${RETURNA_URL}")
    private static String RETURNA_URL ;
    @Value("${URL}")
    private static String URL;
    @Value("${CHARSET}")
    private static String CHARSET;
    @Value("${FORMA}")
    private static String FORMAT;
    @Value("${ALIPAY_PUBLIC_KEY}")
    private static String ALIPAY_PUBLIC_KEY ;
    @Value("${SIGN_TYPE}")
    private static String SIGN_TYPE;

    @RequestMapping(value = "/payConfirm")
    @LoggerManager(description = "支付确认")
    public String payConfirm() {
        return "pay/pay";
    }

    @RequestMapping(value = "/pcPay")
    @LoggerManager(description = "支付宝电脑网站支付")
    @ResponseBody
    public void pcPay(HttpServletResponse response) {
        AlipayClient alipayClient = new DefaultAlipayClient(URL,APP_ID,RSA_PRIVATE_KEY,FORMAT,CHARSET,ALIPAY_PUBLIC_KEY,SIGN_TYPE); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        logger.info("支付成功");
        alipayRequest.setReturnUrl(RETURNA_URL);
        alipayRequest.setNotifyUrl(NOTIFY_URL);//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"20170320010101001\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":88.88," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"body\":\"Iphone6 16G\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            logger.error("pay fail,",e);
        }
        try {
            response.setContentType("text/html;charset=" + CHARSET);
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            logger.error("response exception,",e);
        }
        //return form;
    }

    /*@PostMapping("notify")
    private String aliPayNotify(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                System.out.println(valueStr);
            }
            map.put(name, valueStr);
        }
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(map,ALIPAY_PUBLIC_KEY,CHARSET,SIGN_TYPE);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return ("fail");// 验签发生异常,则直接返回失败
        }
        if (signVerified) {
            //处理你的业务逻辑，更细订单状态等
            return ("success");
        } else {
            System.out.println("验证失败,不去更新状态");
            return ("fail");
        }

    }

    @GetMapping("return")
    private String alipayReturn(Map<String, String> params, HttpServletRequest request, String out_trade_no,String trade_no,String total_amount)
            throws AlipayApiException {
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                System.out.println(valueStr);
            }
            map.put(name, valueStr);
        }
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(map, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return ("fail");// 验签发生异常,则直接返回失败
        }
        if (signVerified) {
            return ("success");
        } else {
            System.out.println("验证失败,不去更新状态");
            return ("fail");
        }
    }*/

}
