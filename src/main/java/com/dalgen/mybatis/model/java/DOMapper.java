package com.dalgen.mybatis.model.java;

import com.dalgen.mybatis.model.java.domapper.DOMapperMethod;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by bangis.wangdf on 15/12/13. Desc
 */
public class DOMapper extends Base {



    /**
     * The Motheds.
     */
    private List<DOMapperMethod> motheds     = Lists.newArrayList();


    /**
     * Gets motheds.
     *
     * @return the motheds
     */
    public List<DOMapperMethod> getMotheds() {
        return motheds;
    }

    /**
     * Add mothed.
     *
     * @param mothed the mothed
     */
    public void addMothed(DOMapperMethod mothed) {
        this.motheds.add(mothed);
    }


}
