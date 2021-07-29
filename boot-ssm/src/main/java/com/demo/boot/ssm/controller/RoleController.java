package com.demo.boot.ssm.controller;


import com.demo.base.component.base.BaseController;
import com.demo.base.component.entity.Role;
import com.demo.base.component.enums.EntityAdd;
import com.demo.base.component.enums.EntityUpdate;
import com.demo.base.component.exception.ResourceNotFoundException;
import com.demo.base.component.pojo.bean.PageBean;
import com.demo.base.component.pojo.bean.ResBean;
import com.demo.base.component.pojo.dto.RoleDTO;
import com.demo.boot.ssm.bean.constant.StringPermission;
import com.demo.boot.ssm.bean.perms.Permission;
import com.demo.boot.ssm.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.PostConstruct;
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
@RequestMapping("role")
@Api("角色相关请求")
public class RoleController extends BaseController<Role> {

    // 由于需要添加注解做权限校验，因此要重写方法，但实现直接调用父类实现即可

    private RoleService roleService = null;

    @PostConstruct
    public void init() {
        this.roleService = (RoleService) baseService;
    }

    @Override
    @PostMapping({"import"})
    @ApiOperation("批量导入数据")
    @Permission(StringPermission.ROLE_ADD)
    public ResponseEntity<ResBean> importData(@RequestParam("file") MultipartFile file) {
        return super.importData(file);
    }


    @Override
    @ApiOperation("新增一条记录，冲突则返回 409")
    @Permission(StringPermission.ROLE_ADD)
    @PostMapping
    public ResponseEntity<ResBean> add(@RequestBody @Validated({EntityAdd.class}) Role entity,
            BindingResult bindingResult) {
        return super.add(entity, bindingResult);
    }

    @Override
    @DeleteMapping({"{id}"})
    @ApiOperation("根据 id 删除记录，不存在则返回 404")
    @Permission(StringPermission.ROLE_DEL)
    public ResponseEntity<ResBean> delete(@PathVariable Long id) {
        // 在 Service 内部对删除做校验
        return this.roleService.deleteRoleWithPerm(id) == 1 ? ResBean.ok_200("删除成功")
                : ResBean.not_found_404("记录不存在");
    }


    @GetMapping({"checkDelete/{id}"})
    @ApiOperation("删除记录前的校验，允许删除 message 为 null，否则 message 为提示信息")
    @Permission(StringPermission.ROLE_QUERY)
    public ResponseEntity<ResBean> checkDelete(@PathVariable Long id) {
        return ResBean.ok_200(super.baseService.queryCheckBeforeDelete(id));
    }

    @Override
    @PutMapping({"{id}"})
    @ApiOperation("根据 id 更新记录，不存在则返回 404")
    @Permission(StringPermission.ROLE_UPDATE)
    public ResponseEntity<ResBean> update(@PathVariable Long id, @RequestBody @Validated({
            EntityUpdate.class}) Role entity, BindingResult bindingResult) {
        return super.update(id, entity, bindingResult);
    }

    @PutMapping({"{roleId}/auth"})
    @Permission(StringPermission.ROLE_UPDATE)
    public ResponseEntity auth(@PathVariable Long roleId, @RequestBody List<Long> permIdList) {
        this.roleService.updateRolePerm(roleId, permIdList); // 全部取消授权也可能返回 0，因此不能用 > 0判断是否成功
        return ResBean.ok_200("设置权限成功");
    }

    @GetMapping({"{id}"})
    @ApiOperation("根据 id 查询单个记录，不存在则返回 404")
    @Permission(StringPermission.ROLE_QUERY)
    public RoleDTO getRoleWithPermId(@PathVariable Long id) {
        RoleDTO roleWithPermId = this.roleService.getRoleWithPermId(id);
        if (roleWithPermId != null) {
            return roleWithPermId;
        } else {
            throw new ResourceNotFoundException("记录不存在");
        }
    }

    @Override
    @GetMapping
    @ApiOperation("分页查询记录，不存在返回空集(200)")
    @Permission(StringPermission.ROLE_QUERY)
    public PageBean<Role> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value = "desc", defaultValue = "false") boolean desc,
            Role entity) {
        return super.list(pageNum, pageSize, orderBy, desc, entity);
    }

}
