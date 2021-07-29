package com.demo.common.base;

import com.demo.common.enums.EntityAdd;
import com.demo.common.enums.EntityUpdate;
import com.demo.common.pojo.bean.PageBean;
import com.demo.common.pojo.converter.PojoConverter;
import com.demo.common.util.ClassUtil;
import com.demo.common.util.DtoUtil;
import com.demo.common.util.RegexpString;
import com.demo.common.util.SnowflakeIdWorker;
import com.demo.common.exception.ResourceNotFoundException;
import com.demo.common.pojo.bean.ResBean;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通用 Controller，基于实体类型 T 对应的 Dto 进行操作，实体校验基于 Add, Update 两个分组
 * @param <T>
 */
public abstract class BaseControllerWithDto<T, DTO extends PojoConverter<DTO, T>> {


    @Autowired
    protected BaseService<T> baseService;

    @Autowired
    protected SnowflakeIdWorker snowflakeIdWorker;

    protected Class<T> entityClass;
    protected Class<DTO> dtoClass;


    public BaseControllerWithDto() {
        this.entityClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.dtoClass = (Class<DTO>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Autowired(required = false)
    private BaseExcelParser<T> baseExcelParser;


//    @PostMapping("import")
    @ApiOperation("批量导入数据")
    public ResponseEntity<ResBean> importData(@RequestParam("file") MultipartFile file) {

        // 校验文件类型
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName) || !fileName.matches(RegexpString.XLS)) {
            return ResBean.badRequest_400("文件类型不正确，请上传后缀为 .xls/.xlsx 的文件");
        }

        if (baseExcelParser == null) {
            ResBean.internal_server_error_500("Excel 解析器为空，无法解析");
        }

        // 解析文件
        BaseExcelParser.ParseResult<T> parseResult = baseExcelParser.doParse(file);

        // 插入解析结果
        this.baseService.saveParseResult(parseResult);

        return ResBean.ok_200("导数数据成功");
    }



//    @PostMapping
    @ApiOperation("新增一条记录，冲突则返回 409")
    public ResponseEntity<ResBean> add(@RequestBody @Validated(EntityAdd.class) DTO dto, BindingResult bindingResult) {

        // 参数校验
        DtoUtil.checkDtoParams(bindingResult);

        // TODO : 更新和新增，可能需要唯一性检查


        // 生成并设置 id
        T entity = dto.convertToEntity();
        ClassUtil.setValue(entityClass, entity, "id", this.snowflakeIdWorker.nextId());

        // 保存
        if (this.baseService.saveSelective(entity) == 1) {
            return ResBean.ok_200("新增成功");
        }

        return ResBean.conflict_409("发生冲突，新增记录失败");
    }

//    @DeleteMapping("{id}")
    @ApiOperation("根据 id 删除记录，不存在则返回 404")
    public ResponseEntity<ResBean> delete(@PathVariable Long id) {
        if (this.baseService.deleteById(id) == 1) {
            return ResBean.ok_200("删除成功");
        } else {
            return ResBean.not_found_404("记录不存在");
        }
    }


//    @PutMapping("{id}")
    @ApiOperation("根据 id 更新记录，不存在则返回 404")
    public ResponseEntity<ResBean> update(@PathVariable Long id, @RequestBody @Validated(EntityUpdate.class) DTO dto, BindingResult bindingResult) {
        DtoUtil.checkDtoParams(bindingResult);

        // 设置 id 并更新
        T entity = dto.convertToEntity();
        ClassUtil.setValue(entityClass, entity, "id", id);

        if (this.baseService.updateSelectiveById(entity) == 1) {
            return ResBean.ok_200("更新成功");
        } else {
            return ResBean.not_found_404("记录不存在");
        }
    }

//    @GetMapping("{id}")
    @ApiOperation("根据 id 查询单个记录，不存在则返回 404")
    public DTO get(@PathVariable Long id) throws IllegalAccessException, InstantiationException {

        T entity = this.baseService.getById(id);

        if (entity != null) {
            DTO dto = dtoClass.newInstance();
            return dto.convertFromEntity(entity);
        } else {
            throw new ResourceNotFoundException("记录不存在");
        }

    }

//    @GetMapping
    @ApiOperation("分页查询记录，不存在返回空集(200)")
    public <QueryDTO extends PojoConverter<QueryDTO, T>> PageBean<DTO> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value = "desc", defaultValue = "false") boolean desc,
            QueryDTO queryDto ) {

        // 根据 queryDto 做分页查询
        PageInfo<T> pageInfo = this.baseService.listPageByQueryDTO(pageNum, pageSize, queryDto, orderBy, desc);

        // 把里面的数据部分转化为 DTO
        List<DTO> dtoList = pageInfo.getList().stream()
                .map(entity -> {
                    try {
                        DTO dto = this.dtoClass.newInstance();
                        dto.convertFromEntity(entity);
                        return dto;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        return PageBean.<DTO>builder()
                .total((int)pageInfo.getTotal())
                .pages(pageInfo.getPages())
                .list(dtoList).build();
    }

}
