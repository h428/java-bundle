package com.hao.bundle.demo.common.base;

import com.hao.bundle.demo.extra.entity.User;
import com.hao.bundle.demo.extra.entity.User.UserBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class UserParser extends BaseExcelParser<User> {

    @Override
    protected User parseRow(Row row) throws Exception {

        UserBuilder builder = User.builder();

        Cell cell = row.getCell(0);
        if (cell != null) {
            long id = (long) cell.getNumericCellValue();
            builder.id(id);
        }

        cell = row.getCell(1);
        if (cell != null) {
            String name =  cell.getStringCellValue();
            builder.name(name);
        }

        cell = row.getCell(3);
        if (cell != null) {
            String email =  cell.getStringCellValue();
            builder.email(email);
        }

        return builder.build();
    }
}
