package biz.gabrys.maven.plugins.css.splitter.steadystate.converters;

import org.junit.Test;
import org.mockito.Mockito;

import com.steadystate.css.dom.CSSCharsetRuleImpl;
import com.steadystate.css.dom.CSSFontFaceRuleImpl;

import biz.gabrys.maven.plugins.css.splitter.css.types.NodeRule;

public final class AbstractRuleConverterTest {

    @Test(expected = UnsupportedRuleException.class)
    public void convert_typeIsInvalid_throwsException() {
        final RuleConverterImpl converter = new RuleConverterImpl();
        converter.convert(new CSSFontFaceRuleImpl());
    }

    @Test
    public void convert_typeIsValid_executesConvert2() {
        final RuleConverterImpl converter = Mockito.spy(new RuleConverterImpl());
        final CSSCharsetRuleImpl rule = new CSSCharsetRuleImpl();
        converter.convert(rule);
        Mockito.verify(converter).convert2(rule);
    }

    private static class RuleConverterImpl extends AbstractRuleConverter<CSSCharsetRuleImpl, NodeRule> {

        RuleConverterImpl() {
            super(CSSCharsetRuleImpl.class);
        }

        @Override
        protected NodeRule convert2(final CSSCharsetRuleImpl rule) {
            // do nothing
            return null;
        }
    }
}
