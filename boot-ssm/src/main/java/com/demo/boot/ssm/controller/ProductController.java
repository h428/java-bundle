package com.demo.boot.ssm.controller;

import com.demo.base.component.base.BaseController;
import com.demo.base.component.entity.Product;
import com.demo.base.component.enums.EntityAdd;
import com.demo.base.component.enums.EntityUpdate;
import com.demo.base.component.exception.ParamErrorException;
import com.demo.base.component.pojo.bean.PageBean;
import com.demo.base.component.pojo.bean.ResBean;
import com.demo.boot.ssm.bean.constant.StringPermission;
import com.demo.boot.ssm.bean.perms.Permission;
import com.demo.boot.ssm.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("product")
@Api("物品相关请求")
public class ProductController extends BaseController<Product> { // 由于需要添加注解做权限校验，因此要重写方法，但实现直接调用父类实现即可


    private ProductService itemService = null;

    @PostConstruct
    public void init() {
        this.itemService = (ProductService) baseService;
    }

    @Override
    @PostMapping({"import"})
    @ApiOperation("批量导入数据")
    @Permission(StringPermission.PRODUCT_ADD)
    public ResponseEntity<ResBean> importData(@RequestParam("file") MultipartFile file) {
        return super.importData(file);
    }

    @Override
    @PostMapping
    @ApiOperation("新增一条记录，冲突则返回 409")
    @Permission(StringPermission.PRODUCT_ADD)
    public ResponseEntity<ResBean> add(@RequestBody @Validated({EntityAdd.class}) Product entity, BindingResult bindingResult) {
        return super.add(entity, bindingResult);
    }

    @Override
    @DeleteMapping({"{id}"})
    @ApiOperation("根据 id 删除记录，不存在则返回 404")
    @Permission(StringPermission.PRODUCT_DEL)
    public ResponseEntity<ResBean> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PutMapping({"{id}"})
    @ApiOperation("根据 id 更新记录，不存在则返回 404")
    @Permission(StringPermission.PRODUCT_UPDATE)
    public ResponseEntity<ResBean> update(@PathVariable Long id, @RequestBody @Validated({
            EntityUpdate.class}) Product entity, BindingResult bindingResult) {
        return super.update(id, entity, bindingResult);
    }

    @Override
    @GetMapping({"{id}"})
    @ApiOperation("根据 id 查询单个记录，不存在则返回 404")
    @Permission(StringPermission.PRODUCT_QUERY)
    public Product get(@PathVariable Long id) {
        return super.get(id);
    }

    @Override
    @GetMapping
    @ApiOperation("分页查询记录，不存在返回空集(200)")
    @Permission(StringPermission.PRODUCT_QUERY)
    public PageBean<Product> list(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            @RequestParam(value = "orderBy",defaultValue = "id") String orderBy,
            @RequestParam(value = "desc",defaultValue = "false") boolean desc,
            Product entity) {
        return super.list(pageNum, pageSize, orderBy, desc, entity);
    }

    @GetMapping("search")
    @ApiOperation("分页搜索记录，不存在返回空集(200)")
    @Permission(StringPermission.PRODUCT_QUERY)
    public PageBean<Product> search(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword") String keyword) {

        if (StringUtils.isBlank(keyword) || keyword.length() < 2) {
            throw new ParamErrorException("关键字不得少于两个字符");
        }

        // 执行搜索
        return PageBean.fromPageInfo(this.itemService.listPageByKeyword(pageNum, pageSize, keyword));
    }

}
