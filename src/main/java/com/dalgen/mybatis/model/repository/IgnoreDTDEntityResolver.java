package com.dalgen.mybatis.model.repository;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by bangis.wangdf on 15/12/18. Desc
 */
public class IgnoreDTDEntityResolver implements EntityResolver {

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException,
            IOException {
        return new InputSource(new ByteArrayInputStream(
                "<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
    }
}
