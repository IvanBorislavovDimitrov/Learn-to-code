package com.code.to.learn.process.parser;

import com.code.to.learn.process.exception.InvalidParserTypeException;

public final class ParserFactory {

    private ParserFactory() {

    }

    public static Parser createParser(ParserType parserType) {
        switch (parserType) {
            case XML:
                return new XmlParser();
            case JSON:
                return new JsonParser();
        }
        throw new InvalidParserTypeException();
    }

}
