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
package biz.gabrys.maven.plugins.css.splitter.css.types;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;

public class StyleRule extends AbstractTextRule {

    private final List<String> selectors;
    private final List<StyleProperty> properties;
    private final boolean splittable;

    public StyleRule(final List<String> selectors, final List<StyleProperty> properties) {
        this(selectors, properties, true);
    }

    public StyleRule(final List<String> selectors, final List<StyleProperty> properties, final boolean splittable) {
        TreeUtils.fillNeighbors(this, properties);
        this.selectors = new ArrayList<String>(selectors);
        this.properties = new ArrayList<StyleProperty>(properties);
        this.splittable = splittable;
    }

    public List<String> getSelectors() {
        return new ArrayList<String>(selectors);
    }

    public List<StyleProperty> getProperties() {
        return new ArrayList<StyleProperty>(properties);
    }

    public boolean isSplittable() {
        return splittable;
    }

    @Override
    public List<String> getLines() {
        final List<String> lines = new ArrayList<String>(2 + properties.size());
        lines.add(StringUtils.join(selectors.iterator(), ", ") + " {");
        for (final StyleProperty property : properties) {
            lines.add(INDENTATION + property);
        }
        lines.add("}");
        return lines;
    }
}
