package com.dalgen.mybatis.model.java;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by bangis.wangdf on 15/12/12. Desc
 */
public class DO extends Base{


    /**
     * The Fieldses.
     */
    private List<Filelds> fieldses    = Lists.newArrayList();



    /**
     * Gets fieldses.
     *
     * @return the fieldses
     */
    public List<Filelds> getFieldses() {
        return fieldses;
    }

    /**
     * Add fields.
     *
     * @param fields the fields
     */
    public void addFields(Filelds fields) {
        this.fieldses.add(fields);
    }


}
