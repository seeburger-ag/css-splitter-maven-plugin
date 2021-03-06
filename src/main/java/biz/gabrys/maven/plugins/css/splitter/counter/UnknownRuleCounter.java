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

import biz.gabrys.maven.plugins.css.splitter.css.types.UnknownRule;

public class UnknownRuleCounter extends AbstractRuleCounter<UnknownRule> {

    public UnknownRuleCounter() {
        super(UnknownRule.class);
    }

    @Override
    protected int count2(final UnknownRule rule) {
        final String code = rule.getCode();
        final int value = code.split(";").length;

        int correction = 0;
        if (value > 1 && code.endsWith("}")) {
            String tmp = code.substring(0, code.length() - 1);
            tmp = tmp.trim();
            if (tmp.endsWith(";")) {
                correction = -1;
            }
        }

        return value + correction;
    }
}
