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

import biz.gabrys.maven.plugins.css.splitter.css.types.ComplexRule;
import biz.gabrys.maven.plugins.css.splitter.css.types.StyleRule;

final class ComplexRulePropertiesLimitValidator extends AbstractRulePropertiesLimitValidator<ComplexRule> {

    private final RulePropertiesLimitValidator validator;

    ComplexRulePropertiesLimitValidator() {
        this(new StyleRulePropertiesLimitValidator());
    }

    // for tests
    ComplexRulePropertiesLimitValidator(final RulePropertiesLimitValidator validator) {
        super(ComplexRule.class);
        this.validator = validator;
    }

    @Override
    protected void validate2(final ComplexRule rule, final int limit) throws ValidationException {
        for (final StyleRule child : rule.getRules()) {
            validator.validate(child, limit);
        }
    }
}
