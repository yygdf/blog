package com.iksling.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.LoginUserBackDTO;
import com.iksling.blog.dto.LoginUserDTO;
import com.iksling.blog.dto.UserOnlinesBackDTO;
import com.iksling.blog.dto.UsersBackDTO;
import com.iksling.blog.entity.*;
import com.iksling.blog.exception.*;
import com.iksling.blog.mapper.*;
import com.iksling.blog.pojo.*;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.service.UserService;
import com.iksling.blog.util.*;
import com.iksling.blog.vo.*;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.*;
import static com.iksling.blog.constant.FlagConst.DELETED;
import static com.iksling.blog.constant.FlagConst.HISTORY;
import static com.iksling.blog.constant.MQConst.EMAIL_EXCHANGE;
import static com.iksling.blog.constant.RedisConst.*;
import static com.iksling.blog.enums.FileDirEnum.*;
import static com.iksling.blog.util.CommonUtil.getSplitStringByIndex;

/**
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private MultiFileMapper multiFileMapper;
    @Autowired
    private QQAuthMapper qqAuthMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private MultiFileService multiFileService;

    @Autowired
    private SessionRegistry sessionRegistry;
    @Resource
    private HttpServletRequest request;
    @Resource
    private HttpServletResponse response;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * qq app-id
     */
    @Value("${qq.app-id}")
    private String QQ_APP_ID;

    /**
     * qq user-info-url
     */
    @Value("${qq.user-info-url}")
    private String QQ_USER_INFO_URL;

    @Override
    @Transactional
    public void saveOrUpdateUserBackVO(UserBackVO userBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        User user = BeanCopyUtil.copyObject(userBackVO, User.class);
        Integer userId = user.getId();
        if (userId == null) {
            if (userBackVO.getUsername() == null || user.getNickname() == null || user.getEmail() == null || !RegexUtil.checkEmail(user.getEmail()))
                throw new OperationStatusException();
            if (userMapper.selectBackUserAvatarById(user.getEmail(), userBackVO.getUsername(), null) != null)
                throw new OperationStatusException("用户已存在!");
            Date createTime = new Date();
            user.setAvatar(null);
            user.setCreateUser(loginUserId);
            user.setCreateTime(createTime);
            userMapper.insert(user);
            registerUser(user.getId(), loginUserId, userBackVO.getUsername(), DEFAULT_PASSWORD, createTime, false);
        } else {
            if (loginUser.getRoleWeight() > 100 && ROOT_USER_ID_LIST.contains(user.getId()))
                throw new IllegalRequestException();
            String avatar = userMapper.selectBackUserAvatarById(null, null, user.getId());
            if (avatar == null)
                throw new IllegalRequestException();
            if (user.getEmail() != null) {
                if (!RegexUtil.checkEmail(user.getEmail()))
                    throw new OperationStatusException();
                if (userMapper.selectBackUserAvatarById(user.getEmail(), null, null) != null)
                    throw new OperationStatusException("邮箱已存在!");
            }
            Date updateTime = new Date();
            if (user.getAvatar() != null) {
                if (!user.getAvatar().startsWith(STATIC_RESOURCE_URL))
                    user.setAvatar("");
                if (!avatar.equals(""))
                    updateUserAvatarBy(loginUserId, avatar.split(STATIC_RESOURCE_URL)[1], updateTime);
            }
            user.setUpdateUser(loginUserId);
            user.setUpdateTime(updateTime);
            userMapper.updateById(user);
        }
    }

    @Override
    @Transactional
    public String saveUserAvatarBackVO(UserAvatarBackVO userAvatarBackVO) {
        MultipartFile file = userAvatarBackVO.getFile();
        userAvatarBackVO.setFile(null);
        Integer userId = userAvatarBackVO.getUserId();
        LoginUser loginUser = UserUtil.getLoginUser();
        if (userId == null)
            userId = loginUser.getUserId();
        else if (loginUser.getRoleWeight() > 200 && !loginUser.getUserId().equals(userId))
            throw new OperationStatusException();
        MultiFileUtil.checkValidFile(file, IMAGE_AVATAR, true);
        List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileFullPath)
                .eq(MultiFile::getUserId, userId)
                .eq(MultiFile::getFileName, IMAGE_AVATAR.getCurrentPath())
                .eq(MultiFile::getDeletedCount, 0));
        if (mapList.isEmpty())
            throw new OperationStatusException();
        long fileName = IdWorker.getId();
        String targetAddr = mapList.get(0).get("file_full_path").toString();
        String[] originalFilenameArr = file.getOriginalFilename().split("\\.");
        String fullFileName = fileName + "." + originalFilenameArr[1];
        if (MultiFileUtil.upload(file, targetAddr, fullFileName) == null)
            throw new FileStatusException("文件上传失败!");
        String iPAddress = IpUtil.getIpAddress(request);
        multiFileMapper.insert(MultiFile.builder()
                .userId(userId)
                .parentId((Integer) mapList.get(0).get("id"))
                .fileDesc("{'userId':"+userId+"}")
                .fileMark(IMAGE_AVATAR.getCurrentPath().intValue())
                .fileName(fileName)
                .fileSize(file.getSize())
                .fileFullPath(targetAddr + "/" + fullFileName)
                .fileExtension(originalFilenameArr[1])
                .fileNameOrigin(originalFilenameArr[0])
                .deletableFlag(false)
                .ipSource(IpUtil.getIpSource(iPAddress))
                .ipAddress(iPAddress)
                .createUser(loginUser.getUserId())
                .createTime(new Date())
                .build());
        return STATIC_RESOURCE_URL + userId + "/" + IMAGE_AVATAR.getPath() + "/" + fullFileName;
    }

    @Override
    @Transactional
    public void deleteBackUserByIdList(List<Integer> idList) {
        // TODO: 物理删除用户牵扯太多数据, 延后处理
        if (idList.isEmpty())
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void deleteBackUserOnlinesByIdList(List<Integer> idList) {
        if (idList.isEmpty() || !Collections.disjoint(idList, ROOT_USER_ID_LIST))
            throw new OperationStatusException();
        List<Object> loginUserList = sessionRegistry.getAllPrincipals().stream().filter(e -> {
            LoginUser loginUser = (LoginUser) e;
            return idList.contains(loginUser.getUserId());
        }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        loginUserList.forEach(e -> allSessions.addAll(sessionRegistry.getAllSessions(e, false)));
        allSessions.forEach(SessionInformation::expireNow);
    }

    @Override
    @Transactional
    public void updateUsersStatusBackVO(StatusBackVO statusBackVO) {
        // TODO: 删除用户牵扯太多数据, 延后处理
    }

    @Override
    @Transactional
    public void updateBackUserAvatarsByFileNameList(List<Long> fileNameList) {
        LoginUser loginUser = UserUtil.getLoginUser();
        List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileFullPath, MultiFile::getFileExtension)
                .in(MultiFile::getFileName, fileNameList)
                .eq(MultiFile::getFileMark, IMAGE_AVATAR.getCurrentPath())
                .eq(MultiFile::getDeletedCount, 0)
                .eq(loginUser.getRoleWeight() > 200, MultiFile::getUserId, loginUser.getUserId()));
        if (mapList.size() != fileNameList.size())
            throw new OperationStatusException();
        mapList.forEach(e -> {
            long fileNameNew = IdWorker.getId();
            String fileFullPath = e.get("file_full_path").toString();
            String fileFullPathOld = getSplitStringByIndex(fileFullPath, "[_.]", 0);
            String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_" + DELETED + "." + e.get("file_extension");
            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                    .set(MultiFile::getFileNameNew, fileNameNew)
                    .set(MultiFile::getFileFullPath, fileFullPathNew)
                    .set(MultiFile::getDeletedCount, 1)
                    .set(MultiFile::getUpdateUser, loginUser.getUserId())
                    .set(MultiFile::getUpdateTime, new Date())
                    .eq(MultiFile::getId, e.get("id")));
            MultiFileUtil.rename(fileFullPath, fileFullPathNew);
        });
    }

    @Override
    @Transactional
    public void updateUserVO(UserVO userVO) {
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(userVO.getIntro() != null, User::getIntro, userVO.getIntro())
                .set(userVO.getGender() != null, User::getGender, userVO.getGender())
                .set(userVO.getWebsite() != null, User::getWebsite, userVO.getWebsite())
                .set(userVO.getNickname() != null, User::getNickname, userVO.getNickname())
                .set(User::getUpdateUser, loginUserId)
                .set(User::getUpdateTime, new Date())
                .eq(User::getId, loginUserId));
    }

    @Override
    @Transactional
    public String saveUserAvatar(MultiFileVO multiFileVO) {
        MultipartFile file = multiFileVO.getFile();
        multiFileVO.setFile(null);
        MultiFileUtil.checkValidFile(file, IMAGE_AVATAR, true);
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileFullPath)
                .eq(MultiFile::getUserId, loginUserId)
                .and(e -> e.eq(MultiFile::getFileName, IMAGE_AVATAR.getCurrentPath()).or().eq(MultiFile::getFileMark, IMAGE_AVATAR.getCurrentPath()))
                .eq(MultiFile::getDeletedCount, 0)
                .orderByAsc(MultiFile::getId));
        if (mapList.isEmpty())
            throw new OperationStatusException();
        String[] originalFilenameArr = file.getOriginalFilename().split("\\.");
        long fileName = IdWorker.getId();
        String targetAddr = mapList.get(0).get("file_full_path").toString();
        String fullFileName = fileName + "." + originalFilenameArr[1];
        if (MultiFileUtil.upload(file, targetAddr, fullFileName) == null)
            throw new FileStatusException("文件上传失败!");
        Date dateTime = new Date();
        String url = STATIC_RESOURCE_URL + loginUserId + "/" + IMAGE_AVATAR.getPath() + "/" + fullFileName;
        if (mapList.size() > 1)
            updateUserAvatarBy(loginUserId, mapList.get(1).get("file_full_path").toString(), dateTime);
        userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getAvatar, url)
                .set(User::getUpdateUser, loginUserId)
                .set(User::getUpdateTime, dateTime)
                .eq(User::getId, loginUserId));
        String iPAddress = IpUtil.getIpAddress(request);
        multiFileMapper.insert(MultiFile.builder()
                .userId(loginUserId)
                .parentId((Integer) mapList.get(0).get("id"))
                .fileDesc("{'userId':"+loginUserId+"}")
                .fileMark(IMAGE_AVATAR.getCurrentPath().intValue())
                .fileName(fileName)
                .fileSize(file.getSize())
                .fileFullPath(targetAddr + "/" + fullFileName)
                .fileExtension(originalFilenameArr[1])
                .fileNameOrigin(originalFilenameArr[0])
                .ipSource(IpUtil.getIpSource(iPAddress))
                .deletableFlag(false)
                .ipAddress(iPAddress)
                .createUser(loginUserId)
                .createTime(dateTime)
                .build());
        return url;
    }

    @Override
    public PagePojo<UsersBackDTO> getUsersBackDTO(Condition condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (DELETED.equals(condition.getType()) && loginUser.getRoleWeight() > 100)
            return new PagePojo<>();
        Integer count = userMapper.selectUsersBackDTOCount(condition);
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<UsersBackDTO> usersBackDTOList = userMapper.selectUsersBackDTO(condition);
        return new PagePojo<>(count, usersBackDTOList);
    }

    @Override
    public boolean getBackUserExistFlag(String email, String username) {
        if (email != null && !RegexUtil.checkEmail(email))
            return false;
        return userMapper.selectBackUserAvatarById(email, username, null) != null;
    }

    @Override
    public PagePojo<UserOnlinesBackDTO> getUserOnlinesBackDTO(Condition condition) {
        List<Integer> onlineUserIdList = sessionRegistry.getAllPrincipals().stream()
                .filter(e -> sessionRegistry.getAllSessions(e, false).size() > 0)
                .map(e -> BeanCopyUtil.copyObject(e, LoginUser.class))
                .filter(e -> CommonUtil.isEmpty(condition.getKeywords()) || e.getUsername().contains(condition.getKeywords()))
                .filter(e -> condition.getFlag() == null || e.getLoginPlatform().equals(condition.getFlag()))
                .sorted(Comparator.comparing(LoginUser::getLoginTime).reversed())
                .map(LoginUser::getUserId)
                .collect(Collectors.toList());
        int count = onlineUserIdList.size();
        if (count == 0)
            return new PagePojo<>();
        int current = (condition.getCurrent() - 1) * condition.getSize();
        if (current >= count)
            return new PagePojo<>(count, new ArrayList<>());
        int size = count > condition.getSize() ? current + condition.getSize() : count;
        List<Integer> idList = onlineUserIdList.subList(current, size);
        List<UserOnlinesBackDTO> userOnlinesBackDTOList = userMapper.selectUserOnlinesBackDTO(idList);
        return new PagePojo<>(count, userOnlinesBackDTOList);
    }

    @Override
    @Transactional
    public void saveUserEmailCode(EmailCodeVO emailCodeVO) {
        String email = emailCodeVO.getEmail();
        if (!RegexUtil.checkEmail(email))
            throw new OperationStatusException();
        Email e;
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++)
            code.append(random.nextInt(10));
        if (emailCodeVO.getType() == null) {
            if (userMapper.selectBackUserAvatarById(email, null, null) != null)
                throw new OperationStatusException("该邮箱号已被注册!");
            e = Email.builder()
                    .email(email)
                    .subject("邮箱注册验证码")
                    .content("您的验证码为 " + code.toString() + " 有效期15分钟,请不要告诉他人哦!")
                    .build();
            redisTemplate.boundValueOps(EMAIL_REGISTER_CODE + "_" + email).set(code);
            redisTemplate.expire(EMAIL_REGISTER_CODE + "_" + email, CODE_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        } else if (emailCodeVO.getType() == 2) {
            if (userMapper.selectBackUserAvatarById(email, null, null) == null)
                throw new OperationStatusException("该邮箱号不存在!");
            e = Email.builder()
                    .email(email)
                    .subject("重设密码验证码")
                    .content("您的验证码为 " + code.toString() + " 有效期15分钟,如果非本人操作请忽略!")
                    .build();
            redisTemplate.boundValueOps(EMAIL_FORGET_CODE + "_" + email).set(code);
            redisTemplate.expire(EMAIL_FORGET_CODE + "_" + email, CODE_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        } else {
            if (userMapper.selectBackUserAvatarById(email, null, null) != null)
                throw new OperationStatusException("该邮箱号已被注册!");
            e = Email.builder()
                    .email(email)
                    .subject("邮箱换绑验证码")
                    .content("您的验证码为 " + code.toString() + " 有效期15分钟,请不要告诉他人哦!")
                    .build();
            redisTemplate.boundValueOps(EMAIL_MODIFY_CODE + "_" + email).set(code);
            redisTemplate.expire(EMAIL_MODIFY_CODE + "_" + email, CODE_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        }
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(e), new MessageProperties()));
    }

    @Override
    @Transactional
    public void saveUserRegisterVO(UserRegisterVO userRegisterVO) {
        String email = userRegisterVO.getEmail();
        if (!RegexUtil.checkEmail(email))
            throw new OperationStatusException();
        if (userMapper.selectBackUserAvatarById(email, userRegisterVO.getUsername(), null) != null)
            throw new OperationStatusException("用户名或邮箱已注册!");
        Object code = redisTemplate.boundValueOps(EMAIL_REGISTER_CODE + "_" + email).get();
        if (code == null)
            throw new OperationStatusException("验证码不存在或已失效!");
        if (!userRegisterVO.getCode().equals(code.toString()))
            throw new OperationStatusException("验证码错误!");
        Date createTime = new Date();
        User user = User.builder()
                .email(userRegisterVO.getEmail())
                .nickname("用户" + IdWorker.getId())
                .createTime(createTime).build();
        userMapper.insert(user);
        registerUser(user.getId(), user.getId(), userRegisterVO.getUsername(), passwordEncoder.encode(userRegisterVO.getPassword()), createTime, false);
        redisTemplate.expire(EMAIL_REGISTER_CODE + "_" + email, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    @Transactional
    public Object qqLogin(QQOauthVO qqOauthVO) {
        QQAuth qqAuth = qqAuthMapper.selectOne(new LambdaQueryWrapper<QQAuth>()
                .select(QQAuth::getUserId, QQAuth::getAccessToken, QQAuth::getLockedFlag, QQAuth::getDisabledFlag)
                .eq(QQAuth::getOpenid, qqOauthVO.getOpenid())
                .eq(QQAuth::getDeletedFlag, false));
        Integer loginUserId;
        Date dateTime = new Date();
        Map<String, String> formData = new HashMap<>();
        formData.put("openid", qqOauthVO.getOpenid());
        formData.put("access_token", qqOauthVO.getAccessToken());
        formData.put("oauth_consumer_key", QQ_APP_ID);
        if (qqAuth == null) {
            Map map = JSON.parseObject(restTemplate.getForObject(QQ_USER_INFO_URL, String.class, formData), Map.class);
            if (map == null || !map.get("ret").equals("0"))
                throw new AuthenticationStatusException();
            Object avatar = map.get("figureurl_qq_2");
            if (avatar.equals(""))
                avatar = map.get("figureurl_qq_1");
            User user = User.builder()
                    .email("")
                    .avatar(avatar.toString())
                    .gender(map.get("gender").equals("男") ? 1 : 0)
                    .nickname(map.get("nickname").toString())
                    .createTime(dateTime).build();
            userMapper.insert(user);
            loginUserId = user.getId();
            qqAuth = QQAuth.builder().openid(qqOauthVO.getOpenid()).accessToken(qqOauthVO.getAccessToken()).build();
            qqAuthMapper.insert(QQAuth.builder()
                    .userId(loginUserId)
                    .openid(qqOauthVO.getOpenid())
                    .accessToken(qqOauthVO.getAccessToken())
                    .createUser(loginUserId)
                    .createTime(dateTime).build());
            registerUser(loginUserId, loginUserId, String.valueOf(IdWorker.getId()), DEFAULT_PASSWORD, dateTime, true);
        } else {
            if (qqAuth.getLockedFlag())
                throw new LockedStatusException("您的账号已被锁定, 如有疑问请联系管理员[" + ADMIN_CONTACT + "]");
            if (qqAuth.getDisabledFlag())
                throw new LockedStatusException("您的账号已被禁用, 如有疑问请联系管理员[" + ADMIN_CONTACT + "]");
            loginUserId = qqAuth.getUserId();
            if (!qqOauthVO.getAccessToken().equals(qqAuth.getAccessToken())) {
                Map map = JSON.parseObject(restTemplate.getForObject(QQ_USER_INFO_URL, String.class, formData), Map.class);
                if (map == null || !map.get("ret").equals(0))
                    throw new AuthenticationStatusException();
                qqAuthMapper.update(null, new LambdaUpdateWrapper<QQAuth>()
                        .set(QQAuth::getAccessToken, qqOauthVO.getAccessToken())
                        .set(QQAuth::getUpdateUser, loginUserId)
                        .set(QQAuth::getUpdateTime, dateTime));
            }
        }
        List<Map<String, Object>> mapList = roleMapper.selectLoginRoleByUserId(loginUserId);
        LoginUser loginUser = LoginUser.builder()
                .userId(loginUserId)
                .username(qqAuth.getOpenid())
                .password(qqAuth.getAccessToken())
                .roleWeight((Integer) mapList.get(0).get("role_weight"))
                .roleIdList(mapList.stream().map(ml -> ml.get("id").toString()).collect(Collectors.toList()))
                .build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getIntro, User::getEmail, User::getAvatar, User::getGender, User::getWebsite, User::getNickname)
                .eq(User::getId, loginUserId));
        Boolean loginPlatform = Boolean.parseBoolean(request.getHeader("Login-Platform"));
        loginUser.setLoginTime(dateTime);
        loginUser.setLoginPlatform(loginPlatform);
        insertLoginLog(loginUserId, dateTime, loginPlatform, request);
        if (loginPlatform) {
            return LoginUserBackDTO.builder()
                    .userId(loginUserId)
                    .intro(user.getIntro())
                    .email(user.getEmail())
                    .avatar(user.getAvatar())
                    .gender(user.getGender())
                    .weight(loginUser.getRoleWeight())
                    .website(user.getWebsite())
                    .nickname(user.getNickname())
                    .build();
        } else {
            Set<Integer> articleLikeSet = (Set<Integer>) redisTemplate.boundHashOps(ARTICLE_USER_LIKE).get(loginUserId.toString());
            Set<Integer> commentLikeSet = (Set<Integer>) redisTemplate.boundHashOps(COMMENT_USER_LIKE).get(loginUserId.toString());
            return LoginUserDTO.builder()
                    .userId(loginUserId)
                    .intro(user.getIntro())
                    .email(user.getEmail())
                    .avatar(user.getAvatar())
                    .gender(user.getGender())
                    .website(user.getWebsite())
                    .nickname(user.getNickname())
                    .articleLikeSet(articleLikeSet)
                    .commentLikeSet(commentLikeSet)
                    .build();
        }
    }

    @Override
    @Transactional
    public void updateUserEmailVO(EmailVO emailVO) {
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        List<Object> objectList = userAuthMapper.selectObjs(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getPassword)
                .eq(UserAuth::getUserId, loginUserId));
        if (!passwordEncoder.matches(emailVO.getPassword(), objectList.get(0).toString()))
            throw new OperationStatusException("密码错误!");
        String email = emailVO.getEmail();
        if (!RegexUtil.checkEmail(email))
            throw new OperationStatusException();
        if (userMapper.selectBackUserAvatarById(email, null, null) != null)
            throw new OperationStatusException("邮箱已注册!");
        Object code = redisTemplate.boundValueOps(EMAIL_MODIFY_CODE + "_" + email).get();
        if (code == null)
            throw new OperationStatusException("验证码不存在或已失效!");
        if (!emailVO.getCode().equals(code.toString()))
            throw new OperationStatusException("验证码错误!");
        userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getEmail, emailVO.getEmail())
                .set(User::getUpdateUser, loginUserId)
                .set(User::getUpdateTime, new Date())
                .eq(User::getId, loginUserId));
        redisTemplate.expire(EMAIL_MODIFY_CODE + "_" + email, 0, TimeUnit.MILLISECONDS);
    }

    private void updateUserAvatarBy(Integer loginUserId, String fileFullPath, Date updateTime) {
        long fileNameNew = IdWorker.getId();
        String[] pathArr = fileFullPath.split("\\.");
        String fileFullPathOld = pathArr[0];
        String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_" + HISTORY + "." + pathArr[pathArr.length - 1];
        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                .set(MultiFile::getFileNameNew, fileNameNew)
                .set(MultiFile::getFileFullPath, fileFullPathNew)
                .set(MultiFile::getDeletedCount, 2)
                .set(MultiFile::getUpdateUser, loginUserId)
                .set(MultiFile::getUpdateTime, updateTime)
                .eq(MultiFile::getFileName, getSplitStringByIndex(fileFullPathOld, "/", -1)));
        MultiFileUtil.rename(fileFullPath, fileFullPathNew);
    }

    private void registerUser(Integer userId, Integer loginUserId, String username, String password, Date createTime, Boolean lockedFlag) {
        userAuthMapper.insert(UserAuth.builder()
                .userId(userId)
                .username(username)
                .password(password)
                .loginMethod(lockedFlag ? 11 : null)
                .lockedFlag(lockedFlag ? true : null)
                .createUser(loginUserId)
                .createTime(createTime)
                .build());
        userRoleMapper.insert(UserRole.builder()
                .userId(userId)
                .roleId(DEFAULT_ROLE_ID)
                .build());
        List<MultiFile> multiFileList = new ArrayList<>();
        multiFileList.add(MultiFile.builder()
                .userId(userId)
                .fileName(IMAGE.getCurrentPath())
                .fileFullPath(userId + "/" + IMAGE.getPath())
                .fileNameOrigin(IMAGE.getName())
                .deletableFlag(false)
                .createUser(loginUserId)
                .createTime(createTime)
                .build());
        multiFileList.add(MultiFile.builder()
                .userId(userId)
                .fileName(AUDIO.getCurrentPath())
                .fileFullPath(userId + "/" + AUDIO.getPath())
                .fileNameOrigin(AUDIO.getName())
                .deletableFlag(false)
                .createUser(loginUserId)
                .createTime(createTime)
                .build());
        multiFileService.saveBatch(multiFileList);
        multiFileList.add(MultiFile.builder()
                .userId(userId)
                .parentId(multiFileList.get(0).getId())
                .fileName(IMAGE_AVATAR.getCurrentPath())
                .fileFullPath(userId + "/" + IMAGE_AVATAR.getPath())
                .fileNameOrigin(IMAGE_AVATAR.getName())
                .deletableFlag(false)
                .createUser(loginUserId)
                .createTime(createTime)
                .build());
        multiFileList.add(MultiFile.builder()
                .userId(userId)
                .parentId(multiFileList.get(1).getId())
                .fileName(AUDIO_CHAT.getCurrentPath())
                .fileFullPath(userId + "/" + AUDIO_CHAT.getPath())
                .fileNameOrigin(AUDIO_CHAT.getName())
                .deletableFlag(false)
                .createUser(loginUserId)
                .createTime(createTime)
                .build());
        multiFileList.remove(0);
        multiFileList.remove(0);
        multiFileService.saveBatch(multiFileList);
    }

    @Async
    public void insertLoginLog(Integer userId, Date loginTime, Boolean loginPlatform, HttpServletRequest httpServletRequest) {
        UserAgent userAgent = UserAgent.parseUserAgentString(httpServletRequest.getHeader("User-Agent"));
        String ipAddress = IpUtil.getIpAddress(httpServletRequest);
        String ipSource = IpUtil.getIpSource(ipAddress);
        LoginLog loginLog = LoginLog.builder()
                .userId(userId)
                .loginTime(loginTime)
                .loginMethod(2)
                .loginDevice(userAgent.getOperatingSystem().getDeviceType().getName())
                .loginPlatform(loginPlatform)
                .loginSystem(userAgent.getOperatingSystem().getName())
                .loginBrowser(userAgent.getBrowser().getName())
                .ipSource(ipSource)
                .ipAddress(ipAddress)
                .build();
        loginLogMapper.insert(loginLog);
        userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                .set(UserAuth::getLoginLogId, loginLog.getId())
                .eq(UserAuth::getUserId, userId));
    }
}




