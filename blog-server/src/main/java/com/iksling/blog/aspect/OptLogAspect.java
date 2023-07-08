package com.iksling.blog.aspect;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.entity.OperationLog;
import com.iksling.blog.mapper.OperationLogMapper;
import com.iksling.blog.util.IpUtil;
import com.iksling.blog.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

@Aspect
@Component
public class OptLogAspect {
    @Autowired
    private OperationLogMapper operationLogMapper;

    @Pointcut("@annotation(com.iksling.blog.annotation.OptLog)")
    public void optLogPointCut() {}

    @Async
    @Transactional(rollbackFor = Exception.class)
    @AfterReturning(value = "optLogPointCut()", returning = "keys")
    public void saveOptLog(JoinPoint joinPoint, Object keys) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) Objects.requireNonNull(requestAttributes).resolveReference(RequestAttributes.REFERENCE_REQUEST);
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
                .optRequestMethod(request.getMethod())
                .optResponseData(JSON.toJSONString(keys))
                .ipSource(ipSource)
                .ipAddress(ipAddress)
                .createUser(UserUtil.getLoginUser().getUserId())
                .createTime(new Date())
                .build());
    }
}
