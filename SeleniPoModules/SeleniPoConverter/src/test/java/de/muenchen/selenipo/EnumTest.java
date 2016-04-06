package de.muenchen.selenipo;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import de.muenchen.selenipo.config.ConverterConfig;

@org.junit.runner.RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConverterConfig.class, loader = AnnotationConfigContextLoader.class)
public class EnumTest {

	/**
	 * Rigourous Test :-)
	 */
	@Test
	public void testApp() {
		Selector s = Selector.LINK;
		if (s == Selector.LINK) {
			Assert.assertTrue(true);
		} else {
			Assert.assertFalse(true);
		}
	}
}
