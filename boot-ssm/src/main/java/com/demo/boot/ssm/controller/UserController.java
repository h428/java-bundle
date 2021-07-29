package com.demo.boot.ssm.controller;

import com.demo.base.component.base.BaseController;
import com.demo.base.component.entity.User;
import com.demo.base.component.enums.EntityAdd;
import com.demo.base.component.enums.EntityUpdate;
import com.demo.base.component.pojo.bean.PageBean;
import com.demo.base.component.pojo.bean.ResBean;
import com.demo.base.component.pojo.dto.UserDTO;
import com.demo.base.component.util.CodecUtil;
import com.demo.base.component.util.SnowflakeIdWorker;
import com.demo.boot.ssm.bean.constant.StringPermission;
import com.demo.boot.ssm.bean.perms.Permission;
import com.demo.boot.ssm.bean.thredlocal.UserIdThreadLocal;
import com.demo.boot.ssm.service.UploadService;
import com.demo.boot.ssm.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("user")
@Api("用户相关请求")
public class UserController extends BaseController<User> {

    // 数据库中存储的名称是不带前缀的，因此返回路径时需要加上前缀，此处使用 127.0.0.1:8082 模拟图片服务器地址
    // 需要在 nginx 中配置将 127.0.0.1:8082 转发到你保存头像的那个路径
    private static String IMG_SERVER_PREFIX = "http://127.0.0.1:8082/";

    // 还有另一种解决方案是利用域名，直接在数据库中存 储域名+文件名，则返回时不需要在额外添加前缀
    // 然后 nginx 针对域名配置将请求转发到本地路径（如果有图片服务器，如 fastDFS 则转发到服务器地址）
    // 前提是你必须拥有该域名，或者在 host 文件中将该域名映射到你可以访问的 ip

    @Autowired
    private UserService userService;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private UploadService uploadService;


    @PostMapping
    @ApiOperation("新增一条记录，冲突则返回 409")
    @Permission(StringPermission.USER_ADD)
    public ResponseEntity<ResBean> add(
            @RequestBody @Validated({EntityAdd.class}) UserDTO userDTO,
            BindingResult bindingResult) {
        User user = userDTO.convertToEntity();
        user.setId(this.snowflakeIdWorker.nextId());
        user.setSalt(CodecUtil.generateSalt256()); // 生成并设置盐值
        user.setUserPass(CodecUtil.sha256Hex(user.getUserPass(), user.getSalt())); // 对原密码加盐hash后保存到实体
        return super.add(user, bindingResult);
    }

    @Override
    @DeleteMapping({"{id}"})
    @ApiOperation("根据 id 删除记录，不存在则返回 404")
    @Permission(StringPermission.USER_DEL)
    public ResponseEntity<ResBean> delete(@PathVariable Long id) {
        return super.delete(id);
    }


    @PutMapping({"{id}"})
    @ApiOperation("根据 id 更新记录，不存在则返回 404")
    @Permission(StringPermission.USER_UPDATE)
    public ResponseEntity<ResBean> update(
            @PathVariable Long id,
            @RequestBody @Validated({EntityUpdate.class}) UserDTO userDTO,
            BindingResult bindingResult) {

        // 不允许更新 email, userPass 而是要调用其他特殊接口
        userDTO.setEmail(null);
        userDTO.setUserPass(null);

        return super.update(id, userDTO.convertToEntity(), bindingResult);
    }

    @Override
    @GetMapping({"{id}"})
    @ApiOperation("根据 id 查询单个记录，不存在则返回 404")
    @Permission(StringPermission.PRODUCT_QUERY)
    public User get(@PathVariable Long id) {
        return super.get(id);
    }


    @ApiOperation("上传头像")
    @PostMapping("uploadProfile")
    public ResponseEntity<ResBean> uploadFile(@RequestParam("file") MultipartFile file) {

        // 上传文件并得到新的文件名
        String fileName = this.uploadService.uploadFile(file);

        // 构造 User，设置 id 和新头像路径以用于更新（有需要还可以加一步删除原头像的步骤，此处不加）
        User user = User.builder()
                .id(UserIdThreadLocal.get())
                .avatar(fileName).build();

        if (this.userService.updateSelectiveById(user) == 1) {
            return ResBean.ok_200("更新头像成功");
        }

        return ResBean.not_found_404("用户不存在，更新失败");
    }


    @Override
    @GetMapping
    @ApiOperation("分页查询记录，不存在返回空集(200)")
    @Permission(StringPermission.USER_QUERY)
    public PageBean<User> list(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            @RequestParam(value = "orderBy",defaultValue = "id") String orderBy,
            @RequestParam(value = "desc",defaultValue = "false") boolean desc,
            User entity) {
        return super.list(pageNum, pageSize, orderBy, desc, entity);
    }
}
