package com.hao.bundle.demo.cache;

import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.pojo.constant.PermTag;
import org.junit.Assert;
import org.junit.Test;

public class PermCacheTest extends BaseTest {

    @Test
    public void userHasPerm() {
        Assert.assertTrue(PermCache.userHasPerm(1L, PermTag.PERM));
        Assert.assertFalse(PermCache.userHasPerm(2L, PermTag.PERM));
        Assert.assertFalse(PermCache.userHasPerm(3L, PermTag.PERM));
    }

    @Test
    public void roleHasPerm() {
    }
}