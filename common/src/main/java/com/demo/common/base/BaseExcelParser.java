package com.demo.common.base;

import com.demo.common.enums.EntityAdd;
import com.demo.common.util.RegexpString;
import com.demo.common.util.SnowflakeIdWorker;
import com.demo.common.exception.ExcelParseException;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Set;

/**
 * 基础解析类，继承该类，复写 parseRow 方法来解析 Excel，每一行对应一个 T 类型的对象 <br/>
 * 此外，内置了对实体的校验流程，需要校验的地方，在实体上打上注解即可，使用的是 Add.class 分组
 * @param <T> 实体类型
 */
public abstract class BaseExcelParser<T> {

    // id 生成器
    protected static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1, 0);
    protected Sheet sheet; // 数据表格
    protected ParseResult<T> parseResult; // 解析结果
    protected Validator validator; // 为了通用性，这里自己初始化校验器而不进行注入，这样子类可以不依赖 Spring 环境，对于 boot-ssm 直接注入也是可以的

    {
        // 初始化校验器
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
        // 初始化解析结果
        this.parseResult = new ParseResult<>();
    }

    // 抽象出的变化 : 不同的数据表解析流程是一样的，主要是每一行的数据格式不一样，抽象出来到子类中实现
    protected abstract T parseRow(Row row) throws Exception;


    public ParseResult<T> doParse(File file) {
        try {
            // 根据文件扩展名判断类型，以创建不同的工作簿对象
            String fileName = file.getName();
            Workbook workbook;
            if (fileName.matches(RegexpString.XLS_2003)) {
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } else {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            }

            // 第一张 sheet
            this.sheet = workbook.getSheetAt(0);
            return doParseWithSheet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public ParseResult<T> doParse(MultipartFile file) {
        try {
            // 根据文件扩展名判断类型，以创建不同的工作簿对象
            String fileName = file.getOriginalFilename();
            Workbook workbook;
            if (fileName.matches(RegexpString.XLS_2003)) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else {
                workbook = new XSSFWorkbook(file.getInputStream());
            }

            // 第一张 sheet
            this.sheet = workbook.getSheetAt(0);
            return doParseWithSheet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // 解析 excel 表格，但不同的数据表有不同的结构，体现在列上
    private ParseResult<T> doParseWithSheet() {

        List<T> dataList = this.parseResult.getDataList(); // 解析结果中的数据对象

        if (this.sheet != null) {
            // 第 0 行是表头不解析，从第 1 行开始解析
            for (int i = 1; i <= this.sheet.getLastRowNum(); i++) {

                // 取出第 i 行
                Row row = this.sheet.getRow(i);

                // 跳过空行
                if (row != null) {
                    // 判断是否结束行
                    Cell cell = row.getCell(0);
                    if (cell != null) {
                        cell.setCellType(CellType.STRING);
                        if ("end".equals(cell.getStringCellValue())) {
                            break;
                        }
                    }

                    T entity = null;

                    try {
                        entity = parseRow(row);// 解析该行并得到一个实体
                    } catch (Exception e) {
                        throw new ExcelParseException(String.format("解析失败，第 %d 行数据有误，可能是数字格式不正确（注意：最后一行要使用 end 标记）", i+1));

                    }

                    // 基于 Add 分组校验实体，如果存在错误直接抛出 ExcelParseException 异常
                    Set<ConstraintViolation<T>> constraintViolationSet = this.validator.validate(entity, EntityAdd.class);
                    if (constraintViolationSet != null && !constraintViolationSet.isEmpty()) {
                        // 抛出全局解析异常，此处为打印
                        for (ConstraintViolation<T> constraintViolation : constraintViolationSet) {
                            throw new ExcelParseException(String.format("解析失败，第 %d 行数据有误，%s", i+1, constraintViolation.getMessage()));
                        }
                    }

                    // 否则解析成功，将该行数据添加到数据中
                    dataList.add(entity);
                }
            }
        }

        return this.parseResult;

    }



    /**
     * 封装的解析结果内部类
     */
    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    public static class ParseResult<T> {
        private List<T> dataList = Lists.newArrayList();
    }


}
