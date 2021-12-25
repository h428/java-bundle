package com.hao.bundle.demo.common.base;

import com.hao.bundle.demo.common.base.BaseExcelParser.ParseResult;
import com.hao.bundle.demo.extra.entity.User;
import java.io.File;
import org.junit.Test;

public class BaseExcelParserTest {

    @Test
    public void doParse() {
        UserParser userParser = new UserParser();
        ParseResult<User> userParseResult = userParser.doParse(new File("C:\\code\\java\\java-bundle\\demo-parent\\demo-common\\src\\test\\resources\\user.xlsx"));
        System.out.println(userParseResult);
    }
}