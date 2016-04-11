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
package biz.gabrys.maven.plugins.css.splitter.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import biz.gabrys.maven.plugins.css.splitter.css.types.NodeRule;
import biz.gabrys.maven.plugins.css.splitter.css.types.StyleSheet;

public final class StylePropertiesLimitValidator {

    private final List<RulePropertiesLimitValidator> validators;
    private final int limit;

    public StylePropertiesLimitValidator(final int limit) {
        this(Arrays.<RulePropertiesLimitValidator>asList(new StyleRulePropertiesLimitValidator(), new ComplexRulePropertiesLimitValidator(),
                new UnknownRulePropertiesLimitValidator()), limit);
    }

    StylePropertiesLimitValidator(final List<RulePropertiesLimitValidator> validators, final int limit) {
        this.limit = limit;
        this.validators = new ArrayList<RulePropertiesLimitValidator>(validators);
    }

    public void validate(final StyleSheet stylesheet) throws ValidationException {
        for (final NodeRule rule : stylesheet.getRules()) {
            validate(rule);
        }
    }

    private void validate(final NodeRule rule) throws ValidationException {
        for (final RulePropertiesLimitValidator validator : validators) {
            if (validator.isSupportedType(rule)) {
                validator.validate(rule, limit);
                return;
            }
        }
    }
}
