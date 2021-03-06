/*
 * CSS Splitter Maven Plugin
 * http://www.gabrys.biz/projects/css-splitter-maven-plugin/
 *
 * Copyright (c) 2015 Adam Gabryś
 *
 * This file is licensed under the BSD 3-Clause (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *      https://raw.githubusercontent.com/gabrysbiz/css-splitter-maven-plugin/master/src/main/resources/license.txt
 */
package biz.gabrys.maven.plugins.css.splitter.steadystate;

import java.io.IOException;
import java.io.StringReader;

import org.apache.maven.plugin.logging.Log;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;

import biz.gabrys.maven.plugins.css.splitter.css.Standard;
import biz.gabrys.maven.plugins.css.splitter.css.types.StyleSheet;
import biz.gabrys.maven.plugins.css.splitter.steadystate.converters.StyleSheetConverter;

public class SteadyStateParser {

    private final Log logger;

    public SteadyStateParser(final Log logger) {
        this.logger = logger;
    }

    public StyleSheet parse(final String css, final Standard standard) throws ParserException {
        final NativeParserFactory factory = new NativeParserFactory();
        final CSSOMParser parser = new CSSOMParser(factory.create(standard));

        final SteadyErrorHandler errorHandler = new SteadyErrorHandler(logger);
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(css));
        CSSStyleSheet stylesheet;
        try {
            stylesheet = parser.parseStyleSheet(source, null, null);
        } catch (final IOException e) {
            throw new ParserException(e);
        }
        errorHandler.validate();

        final StyleSheetConverter converter = new StyleSheetConverter();
        return converter.convert(stylesheet);
    }
}
