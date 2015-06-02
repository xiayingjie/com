package com.xyj.common.auth.shiro;

import nl.captcha.Captcha;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:13-10-18 下午3:09
 */
public class FormAuthenticationCaptchaFilter extends FormAuthenticationFilter {
//    如果需要修改form表单的账号名和密码以及rememberMe，再这里修改属性
//    public static final String DEFAULT_USERNAME_PARAM = "username";
//    public static final String DEFAULT_PASSWORD_PARAM = "password";
//    public static final String DEFAULT_REMEMBER_ME_PARAM = "rememberMe";
    public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

    private String captchaParam=DEFAULT_CAPTCHA_PARAM;

    /**
     * 重写认证
     * @param request
     * @param response
     * @return
     */
    @Override
    protected UsernamePasswordCaptchaToken createToken(ServletRequest request,
                                              ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        String captcha = getCaptcha(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        return new UsernamePasswordCaptchaToken(username, password, rememberMe,
                host, captcha);
    }

    public String getCaptchaParam() {
        return captchaParam;
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }
    public void setCaptchaParam(String captchaParam) {
        this.captchaParam=captchaParam;

    }



    /**
     * 认证
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request,
                                   ServletResponse response) throws Exception {

        UsernamePasswordCaptchaToken token = createToken(request, response);

        try {
            doCaptchaValidate( (HttpServletRequest)request,token );

            Subject subject = getSubject(request, response);
            subject.login(token);

            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }


    /**
     * 验证码校验
     * @param request
     * @param token
     */

    protected void doCaptchaValidate( HttpServletRequest request
            ,UsernamePasswordCaptchaToken token ){

        Captcha scaptcha = (Captcha)request.getSession().getAttribute(
                Captcha.NAME);
        String simpleCaptcha=scaptcha.getAnswer();

        if( simpleCaptcha!=null &&!simpleCaptcha.equalsIgnoreCase(token.getCaptcha()) ){
            throw new CaptchaException ("验证码错误！");
        }
    }

}
