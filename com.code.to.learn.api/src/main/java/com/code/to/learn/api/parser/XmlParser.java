package com.code.to.learn.api.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlParser extends Parser {

    @Override
    protected ObjectMapper createMapper() {
        return new XmlMapper();
    }
}
