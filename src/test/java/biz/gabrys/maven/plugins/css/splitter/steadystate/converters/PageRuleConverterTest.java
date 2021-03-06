package biz.gabrys.maven.plugins.css.splitter.steadystate.converters;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.steadystate.css.dom.CSSPageRuleImpl;

import biz.gabrys.maven.plugins.css.splitter.css.types.StyleProperty;
import biz.gabrys.maven.plugins.css.splitter.css.types.StyleRule;

public final class PageRuleConverterTest {

    @Test
    public void getSupportedType_returnsCSSPageRuleImplClass() {
        final PageRuleConverter converter = new PageRuleConverter();
        Assert.assertEquals("Supported type", CSSPageRuleImpl.class, converter.getSupportedType());
    }

    @Test
    public void convert() {
        final PageRuleConverter converter = new PageRuleConverter();
        final CSSPageRuleImpl rule = new CSSPageRuleImpl();
        rule.setCssText("@page :first { name: value; }");

        final StyleRule converted = converter.convert(rule);
        Assert.assertNotNull("Converted rule instance", converted);
        final List<String> selectors = converted.getSelectors();
        Assert.assertNotNull("Converted rule selectors instance", selectors);
        Assert.assertEquals("Converted rule selectors size", 1, selectors.size());
        Assert.assertEquals("Converted rule selector", "@page :first", selectors.get(0));
        final List<StyleProperty> properties = converted.getProperties();
        Assert.assertNotNull("Converted rule properties instance", properties);
        Assert.assertEquals("Converted rule properties size", 1, properties.size());
        final StyleProperty styleProperty = properties.get(0);
        Assert.assertEquals("Converted rule property name", "name", styleProperty.getName());
        Assert.assertEquals("Converted rule property value", "value", styleProperty.getValue());
    }
}
