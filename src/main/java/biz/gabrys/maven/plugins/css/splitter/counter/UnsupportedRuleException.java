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
package biz.gabrys.maven.plugins.css.splitter.counter;

import biz.gabrys.maven.plugins.css.splitter.css.types.NodeRule;

public class UnsupportedRuleException extends RuntimeException {

    private static final long serialVersionUID = 2488511304734029055L;

    public UnsupportedRuleException(final NodeRule rule) {
        super(String.format("Rule represented by \"%s\" class is unsupported! CSS code that causes error:%n%s",
                rule == null ? "null" : rule.getClass().getName(), rule));
    }
}
