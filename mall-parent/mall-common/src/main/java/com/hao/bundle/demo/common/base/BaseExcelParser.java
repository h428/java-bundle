package com.hao.bundle.demo.common.base;

import com.google.common.collect.Lists;
import com.hao.bundle.demo.common.enums.Regexp.Matcher;
import com.hao.bundle.demo.common.exception.BaseException;
import com.hao.bundle.demo.common.util.SnowflakeIdWorker;
import com.hao.bundle.demo.common.util.ValidationUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * 基础解析类，继承该类，复写 parseRow 方法来解析 Excel，每一行对应一个 T 类型的对象；
 * 此外，内置了对实体的校验流程，需要校验的地方，在实体上打上注解即可
 *
 * @param <T> 实体类型
 */
public abstract class BaseExcelParser<T> {

    // id 生成器
    protected static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1, 0);

    // 抽象出的变化 : 不同的数据表解析流程是一样的，主要是每一行的数据格式不一样，抽象出来到子类中实现
    protected abstract T parseRow(Row row) throws Exception;

    private Workbook open(InputStream in, boolean isExcel2003) {
        try {
            if (isExcel2003) {
                return new HSSFWorkbook(in);
            }
            return new XSSFWorkbook(in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ParseResult<T> doParse(InputStream in, boolean isExcel2003) {
        Workbook workbook = open(in, isExcel2003);
        // 第一张 sheet
        Sheet sheet = workbook.getSheetAt(0);
        return doParseWithSheet(sheet);
    }


    /**
     * 解析普通本地文件
     *
     * @param file 本地文本
     * @return 解析结果
     */
    public ParseResult<T> doParse(File file) {
        try {
            // 根据文件扩展名判断类型，以创建不同的工作簿对象
            String fileName = file.getName();
            boolean isExcel2003 = Matcher.EXCEL_2003.matches(fileName);
            return doParse(new FileInputStream(file), isExcel2003);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 解析 Spring 的 MultipartFile 文件
     *
     * @param file Spring 的 MultipartFile 文件
     * @return 解析结果
     */
    public ParseResult<T> doParse(MultipartFile file) {
        try {
            // 根据文件扩展名判断类型，以创建不同的工作簿对象
            String fileName = file.getOriginalFilename();
            boolean isExcel2003 = Matcher.EXCEL_2003.matches(fileName);
            return doParse(file.getInputStream(), isExcel2003);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // 解析 excel 表格，但不同的数据表有不同的结构，体现在列上
    private ParseResult<T> doParseWithSheet(Sheet sheet) {

        ParseResult<T> parseResult = new ParseResult<>();

        List<T> dataList = parseResult.getDataList(); // 解析结果中的数据对象

        if (sheet != null) {
            // 第 0 行是表头不解析，从第 1 行开始解析
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                // 取出第 i 行
                Row row = sheet.getRow(i);

                // 跳过空行
                if (row != null) {
                    // 判断是否结束行
                    Cell cell = row.getCell(0);
                    if (cell == null) {
                        continue;
                    }

                    T entity = null;

                    try {
                        entity = parseRow(row);// 解析该行并得到一个实体
                    } catch (Exception e) {
                        throw new BaseException(String.format("解析失败，第 %d 行数据有误，可能是数字格式不正确", i + 1));
                    }
                    // 校验实体，如果存在错误直接抛出通用异常
                    ValidationUtil.validate(entity);

                    // 解析成功，将该行数据添加到数据中
                    dataList.add(entity);
                }
            }
        }

        return parseResult;
    }


    /**
     * 封装的解析结果内部类
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Accessors(chain = true)
    @Builder
    @ToString
    public static class ParseResult<T> {
        private List<T> dataList = Lists.newArrayList();
    }


}
