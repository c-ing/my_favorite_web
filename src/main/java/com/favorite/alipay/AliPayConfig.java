package com.favorite.alipay;

/**
 * Created by cdc on 2018/8/22.
 */
public class AliPayConfig {

    // 商户appid
    public static String APP_ID = "2016090800465865";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCoLi83oaeXQbK83MZLBhRoeFlVVXGYX0x9VLPsc/xzSs2JjgjNZhsm8kLi5vrL/DzkuurJGWEvGYs5pGjDSKQM/CjLg5ZMQP4bBwv7gtuGdkCWz7hA7oOuQmZGQPiEEtGkmGT8zSJaRmg99TAxLEZydmxEczzSprHUmscXi+vGt9tFogmXYEui0r/2yfUs7mYVL3kL+KFjua55eS+Zf8pl4+Rm6g1Vyjlp2GV3pB9gMIVQjysVY99nvmEgXTuvoFPgWPmjLov6tCK9xDhoh1j4Sk4N+SI0NJlpYJLWqPGjGeGTEgHXaZyWYqsd8KSdbj2VKslbnLuC6YcGiI74UwDFAgMBAAECggEAfd8jI8Xvgrt/JO8v0hLYz1/TrJRokpwwyVTN3NjqRvfjYWJWoDQdekazXNN3PZipkp96/cvq+q9pjljIt+/0KGvJtJfls2ipRl090J4d40M6ECWjR6W8FH5Jwab62Q6krUYJv1NgtKQhfM23pfX3O8/6NDjzes2cJv04qRzl+DGGRqpv56bbik7ZCn8QNH/3nNBfUsJ6VCtzJJVT2xuLE6D8Bux42jruTZQ3mnPZS5SolmrYTW6tFQxCqQDZHGPXNwaiYEiTn0q6qfZnm6GeXKKNC2CFtuyRK6mkMogRsuzz3sDE58HvD63UFaPfNcDlf8QOm01YFykZf2Yp1BbrAQKBgQDpRrY2xxoFeTKmNZi6Bvt1qbUUSeQKHG2teU8wltwof83+XvBvSNnLjqq2HQQosp2FXjP9Y88c96VXHfqYxpZxqWVPqaWpvtpcQkw0K6EFOVTNwmwDG+8mhmEpDjo3gKGF5lksKlm1Hcf0+M6jT48NfMKQxACuwKrX5sJ8X3x3ZQKBgQC4kCgHXr5G/dhTKJAB5XMcXHJtrU8nmxt9ej+PNqq9bdCXAMfL4a8SbJeTXYeSDjZmh2sf3zOw2coQcXOhHm+mIza2ZD2iYhr2gWRnubRT3AO4Rrh3VTgeWHIO3+gtA4G06dsgWVz8qbZHxd900RIUIKqIqFT/0rMsIYTI8V894QKBgQCbrEO3JIFZ+7pwLr5ADp7Ks7kS1AipMrWCmbzeCR7crAV05jh/m1c0v31u0MhQvHngYbBCPdQeWaluDKSKRaRqL4hbdDQIpkOkY93kxuTKzqqoMUHRhVfAFtdKBaOWHHIuID2L+qso8bnZ4hxL4bDuaD6TSynFdO/N6sIJK9Os/QKBgQC2zwL3Gd76zmDBLcJGPpFgHCmzfSCUKEx4Yugl4I4KB4W+rCU6SxZv92PDsbag8rbro2J2NU0r9OzykI1pn44TT3nX+z4ZY3c1h0B+hTCah6iflF0OxjhJxm1jUmDUhU6qdaB4UEuMcEV+nf4R5Sw1B44iwik/I+IY6jJuHrapwQKBgDC2DGPyrEuQOV4fQmY5q3kjIwKXN2nR6JNGJTYeNFdiPsvjQEQpznXqwD3D4lN/ePzR0dIJ6y5xbn6+WrNhuhAgKRS/fQO/slmVHjLshsosLnisPOb9nG0TwAQFn5O3v+Pu9J1O96CkPjSd9w537B2V/LVN86xDcRZgaFiPCeel";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://商户网关地址/alipay.trade.wap.pay-JAVA-UTF-8/notify_url.jsp";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://商户网关地址/alipay.trade.wap.pay-JAVA-UTF-8/return_url.jsp";
    // 请求网关地址, 支付宝网关
    public static String URL = "https://openapi.alipaydev.com/gateway.do";//
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlXzDsQHO0dPuHBXJJw8dkUjPpJIG0Q45A2H7Ep1hJVR4HjM627h0iwVm2AkRUNR2nN7VNazH4diZWZox5lXByZJundvW36MUrHdJPYwssBZ0HRwxr4Bxk48vd5pW5/5siz3XI1snAhkCOAXwpaN2s7UG+lyObSlYUDYvSBh1FlbcrjgaB4ApBTJ2IJbwaEFsFeEFRv6HDZVM3XFUVBgK/9dQ8gz4oaO7DoGXCY1x5jVam57hGCbUL2AS+rxHp+Gd+zEaMgU+QRfTU4rS20BqvrAqMm6MkplXNumQCRf/8zDboH0dsNgJgX0n7WSr5dv51OjBBIojudhmFLzV2Dl/gQIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGN_TYPE = "RSA2";
}
