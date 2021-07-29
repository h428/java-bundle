package com.demo.boot.ssm.bean.parser;

import com.demo.base.component.base.BaseExcelParser;
import com.demo.base.component.entity.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

/**
 * Item 数据表的解析器，数据格式参考 resources/item.xlsx
 */
@Component // 由 Spring 托管，以让 BaseController 中能扫描到
public class ItemParser extends BaseExcelParser<Product> {


    @Override
    protected Product parseRow(Row row) throws Exception {

        Product item = Product.builder().build();

        // 第 0 列 : 编号，此处不解析

        // 第 1 列 : 名称
        Cell cell = row.getCell(1);
        if (cell != null) {
            cell.setCellType(CellType.STRING);
            item.setName(cell.getStringCellValue());
        }

        // 第 2 列 : 价格（我的 xls 中应该是文本类型）
        cell = row.getCell(2);
        if (cell != null) {
            cell.setCellType(CellType.STRING);
            item.setPrice(Float.valueOf(cell.getStringCellValue()));
        }

        // 第 3 列 : 备注
        cell = row.getCell(3);
        if (cell != null) {
            cell.setCellType(CellType.STRING);
            item.setNote(cell.getStringCellValue());
        }

        // 设置 id
        item.setId(BaseExcelParser.snowflakeIdWorker.nextId());

        return item;
    }
}
