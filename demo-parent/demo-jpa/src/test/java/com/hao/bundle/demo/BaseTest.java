package com.hao.bundle.demo;

import javax.transaction.Transactional;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoJpaTestApplication.class)
@Transactional
public class BaseTest {



}
