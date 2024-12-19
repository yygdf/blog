package com.iksling.blog.aspect;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.entity.ExceptionLog;
import com.iksling.blog.entity.OperationLog;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.ExceptionLogMapper;
import com.iksling.blog.mapper.OperationLogMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.util.RabbitUtil;
import com.iksling.blog.util.IpUtil;
import com.iksling.blog.util.LocaleUtil;
import com.iksling.blog.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Date;

import static com.iksling.blog.constant.CommonConst.ADMIN_CONTACT_EMAIL;
import static com.iksling.blog.constant.CommonConst.WEBSITE_URL_BACK;
import static com.iksling.blog.constant.LogConst.QUERY;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private OperationLogMapper operationLogMapper;
    @Autowired
    private ExceptionLogMapper exceptionLogMapper;

    @Resource
    private HttpServletRequest request;

    @Pointcut("@annotation(com.iksling.blog.annotation.OptLog)")
    public void optLogPointCut() {}

    @Pointcut("@annotation(io.swagger.annotations.ApiImplicitParam))")
    public void exceptionLogPointCut() {}

    @Async
    @Transactional(rollbackFor = Exception.class)
    @AfterReturning(value = "optLogPointCut()", returning = "keys")
    public void saveOptLog(JoinPoint joinPoint, Object keys) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Api api = (Api) signature.getDeclaringType().getAnnotation(Api.class);
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        OptLog optLog = method.getAnnotation(OptLog.class);
        String ipAddress = IpUtil.getIpAddress(request);
        String ipSource = IpUtil.getIpSource(ipAddress);
        operationLogMapper.insert(OperationLog.builder()
                .userId(UserUtil.getLoginUser().getUserId())
                .optUri(request.getRequestURI())
                .optType(optLog.optType())
                .optDesc(apiOperation.value())
                .optModule(api.tags()[0])
                .optMethod(joinPoint.getTarget().getClass().getName() + "." + method.getName())
                .requestParam(JSON.toJSONString(joinPoint.getArgs()))
                .responseData(JSON.toJSONString(keys))
                .ipSource(ipSource)
                .ipAddress(ipAddress)
                .createUser(UserUtil.getLoginUser().getUserId())
                .createTime(new Date())
                .build());
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    @AfterThrowing(value = "exceptionLogPointCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        LoginUser loginUser = UserUtil.getLoginUser();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Api api = (Api) signature.getDeclaringType().getAnnotation(Api.class);
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        OptLog optLog = method.getAnnotation(OptLog.class);
        String ipAddress = IpUtil.getIpAddress(request);
        String ipSource = IpUtil.getIpSource(ipAddress);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        ExceptionLog exceptionLog = ExceptionLog.builder()
                .userId(loginUser.getUserId())
                .optUri(request.getRequestURI())
                .optType(optLog == null ? QUERY : optLog.optType())
                .optDesc(apiOperation.value())
                .optModule(api.tags()[0])
                .optMethod(joinPoint.getTarget().getClass().getName() + "." + method.getName())
                .requestParam(JSON.toJSONString(joinPoint.getArgs()))
                .exceptionMessage(e.toString())
                .exceptionStackTrace(sw.toString())
                .ipSource(ipSource)
                .ipAddress(ipAddress)
                .createUser(loginUser.getUserId())
                .createTime(new Date())
                .build();
        if (e instanceof IllegalRequestException) {
            exceptionLog.setIllegalFlag(true);
            exceptionLogMapper.insert(exceptionLog);
            exceptionLog.setExceptionStackTrace(null);
            RabbitUtil.sendEmail(ADMIN_CONTACT_EMAIL, LocaleUtil.getMessage("A0003", loginUser.getUsername(), WEBSITE_URL_BACK), JSON.toJSONString(exceptionLog));
        } else
            exceptionLogMapper.insert(exceptionLog);
    }
}
