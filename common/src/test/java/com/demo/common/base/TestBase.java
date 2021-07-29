package com.demo.common.base;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BaseServiceTest.class,
        BaseServiceWithDeletedTest.class,
})
public class TestBase {

}
