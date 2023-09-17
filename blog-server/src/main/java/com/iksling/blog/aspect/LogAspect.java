package com.iksling.blog.aspect;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.entity.ExceptionLog;
import com.iksling.blog.entity.OperationLog;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.ExceptionLogMapper;
import com.iksling.blog.mapper.OperationLogMapper;
import com.iksling.blog.pojo.Email;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.util.IpUtil;
import com.iksling.blog.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
import java.util.Objects;

import static com.iksling.blog.constant.CommonConst.ADMIN_EMAIL;
import static com.iksling.blog.constant.MQConst.EMAIL_EXCHANGE;
import static com.iksling.blog.constant.OptLogConst.QUERY;

@Aspect
@Component
public class LogAspect {
    @Autowired
    private OperationLogMapper operationLogMapper;
    @Autowired
    private ExceptionLogMapper exceptionLogMapper;

    @Resource
    private HttpServletRequest request;

    @Autowired
    private RabbitTemplate rabbitTemplate;

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
                .optUri(Objects.requireNonNull(request).getRequestURI())
                .optType(optLog.optType())
                .optDesc(apiOperation.value())
                .optModule(api.tags()[0])
                .optMethod(joinPoint.getTarget().getClass().getName() + "." + method.getName())
                .optRequestParam(JSON.toJSONString(joinPoint.getArgs()))
                .optResponseData(JSON.toJSONString(keys))
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
                .optUri(Objects.requireNonNull(request).getRequestURI())
                .optType(Objects.isNull(optLog) ? QUERY : optLog.optType())
                .optDesc(apiOperation.value())
                .optModule(api.tags()[0])
                .optMethod(joinPoint.getTarget().getClass().getName() + "." + method.getName())
                .optRequestParam(JSON.toJSONString(joinPoint.getArgs()))
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
            Email email = Email.builder()
                    .email(ADMIN_EMAIL)
                    .subject("用户[" + loginUser.getUsername() + "]的非法操作已成功拦截")
                    .content(JSON.toJSONString(exceptionLog))
                    .build();
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(email), new MessageProperties()));
        } else
            exceptionLogMapper.insert(exceptionLog);
    }
}
