package com.code.to.learn.util.parser;

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
