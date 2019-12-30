package com.code.to.learn.core.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlParser extends Parser {

    XmlParser() {

    }

    @Override
    protected ObjectMapper createMapper() {
        return new XmlMapper();
    }
}
