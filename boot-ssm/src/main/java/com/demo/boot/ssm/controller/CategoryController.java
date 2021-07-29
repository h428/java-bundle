package com.demo.boot.ssm.controller;

import com.demo.base.component.base.BaseController;
import com.demo.base.component.entity.Category;
import com.demo.base.component.enums.EntityAdd;
import com.demo.base.component.enums.EntityUpdate;
import com.demo.base.component.pojo.bean.PageBean;
import com.demo.base.component.pojo.bean.ResBean;
import com.demo.boot.ssm.bean.constant.StringPermission;
import com.demo.boot.ssm.bean.perms.Permission;
import com.demo.boot.ssm.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("category")
@Api("分类相关请求")
public class CategoryController extends
        BaseController<Category> { // 由于需要添加注解做权限校验，因此要重写方法，但实现直接调用父类实现即可

    private CategoryService categoryService = null;

    @PostConstruct
    public void init() {
        this.categoryService = (CategoryService) baseService;
    }


    @Override
    @PostMapping({"import"})
    @ApiOperation("批量导入数据")
    @Permission(StringPermission.CATEGORY_ADD)
    public ResponseEntity<ResBean> importData(@RequestParam("file") MultipartFile file) {
        return super.importData(file);
    }

    @Override
    @PostMapping
    @ApiOperation("新增一条记录，冲突则返回 409")
    @Permission(StringPermission.CATEGORY_ADD)
    public ResponseEntity<ResBean> add(@RequestBody @Validated({EntityAdd.class}) Category entity,
            BindingResult bindingResult) {
        return super.add(entity, bindingResult);
    }

    @Override
    @DeleteMapping({"{id}"})
    @ApiOperation("根据 id 删除记录，不存在则返回 404")
    @Permission(StringPermission.CATEGORY_DEL)
    public ResponseEntity<ResBean> delete(@PathVariable Long id) {
        // 在 Service 内部对删除做校验
        return super.delete(id);
    }

    @GetMapping({"subCategoryNum/{id}"})
    @ApiOperation("根据 id 查询该 category 下的 sub category 数量")
    @Permission(StringPermission.CATEGORY_QUERY)
    public ResponseEntity<ResBean> subCategoryNum(@PathVariable Long id) {
        // 将当前 id 作为 pid 查询是否存在子 category
        Category query = Category.builder()
                .pid(id).build();
        return ResBean.ok_200(String.valueOf(super.baseService.countByEntity(query)));
    }

    @GetMapping({"checkDelete/{id}"})
    @Permission(StringPermission.CATEGORY_QUERY)
    public ResponseEntity<ResBean> checkDelete(@PathVariable Long id) {
        return ResBean.ok_200(super.baseService.queryCheckBeforeDelete(id));
    }

    @Override
    @PutMapping({"{id}"})
    @ApiOperation("根据 id 更新记录，不存在则返回 404")
    @Permission(StringPermission.CATEGORY_UPDATE)
    public ResponseEntity<ResBean> update(@PathVariable Long id,
            @RequestBody @Validated({EntityUpdate.class}) Category entity,
            BindingResult bindingResult) {
        return super.update(id, entity, bindingResult);
    }

    @Override
    @GetMapping({"{id}"})
    @ApiOperation("根据 id 查询单个记录，不存在则返回 404")
    @Permission(StringPermission.CATEGORY_QUERY)
    public Category get(@PathVariable Long id) {
        return super.get(id);
    }

    @Override
    @GetMapping
    @ApiOperation("分页查询记录，不存在返回空集(200)")
    @Permission(StringPermission.CATEGORY_QUERY)
    public PageBean<Category> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value = "desc", defaultValue = "false") boolean desc,
            Category entity) {
        return super.list(pageNum, pageSize, orderBy, desc, entity);
    }

}
