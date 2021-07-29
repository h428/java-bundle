package com.demo.boot.ssm.service;

import com.demo.base.component.base.BaseService;
import com.demo.base.component.entity.Category;
import com.demo.base.component.entity.Product;
import com.demo.base.component.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class CategoryService extends BaseService<Category> {

    private static Logger logger = LoggerFactory.getLogger(CategoryService.class);


    @Autowired
    private ProductMapper productMapper;

    @Override
    public String queryCheckBeforeDelete(Object id) {

        // 将当前 id 作为 pid 查询是否存在子 category
        Example example = new Example(Category.class);

        example.createCriteria()
                .andEqualTo("pid", id);

        if (this.mapper.selectCountByExample(example) > 0) {
            return "当前分类下还存在二级标题，无法删除";
        }

        example = new Example(Product.class);

        example.createCriteria().andEqualTo("cid1", id);

        example.or().andEqualTo("cid2", id);

        if (this.productMapper.selectCountByExample(example) > 0) {
            return "还有商品引用该分类，无法删除";
        }

        // 校验通过
        return null;
    }
}
