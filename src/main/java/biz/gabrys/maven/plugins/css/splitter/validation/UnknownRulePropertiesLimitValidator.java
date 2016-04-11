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

import biz.gabrys.maven.plugins.css.splitter.counter.RuleCounter;
import biz.gabrys.maven.plugins.css.splitter.counter.UnknownRuleCounter;
import biz.gabrys.maven.plugins.css.splitter.css.types.UnknownRule;

final class UnknownRulePropertiesLimitValidator extends AbstractRulePropertiesLimitValidator<UnknownRule> {

    private final RuleCounter<UnknownRule> counter;

    UnknownRulePropertiesLimitValidator() {
        this(new UnknownRuleCounter());
    }

    // for tests
    UnknownRulePropertiesLimitValidator(final RuleCounter<UnknownRule> counter) {
        super(UnknownRule.class);
        this.counter = counter;
    }

    @Override
    protected void validate2(final UnknownRule rule, final int limit) throws ValidationException {
        final int value = counter.count(rule);
        if (value > limit) {
            throwException(value, limit, rule.getCode());
        }
    }
}
