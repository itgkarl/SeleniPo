package de.muenchen.selenipo;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import de.muenchen.selenipo.config.GeneratorConfig;
import de.muenchen.selenipo.impl.fxModel.ElementFx;
import de.muenchen.selenipo.impl.fxModel.PoGenericFx;
import de.muenchen.selenipo.impl.fxModel.PoModelFx;
import de.muenchen.selenipo.impl.fxModel.TransitionFx;

@org.junit.runner.RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GeneratorConfig.class, loader = AnnotationConfigContextLoader.class)
public class GeneratorServiceImplTest {

	@Autowired
	public GeneratorService generatorService;

	@Test
	public void testGenerationSinglePo() throws IOException {
		PoModelFx testPoGenericFx = getTestPoGenericFx();
		Map<String, String> generatePageObject = generatorService
				.generatePageObject(testPoGenericFx.getPoGenerics().get(0),
						"./test");
		Assert.assertEquals(1, generatePageObject.size());
	}

	@Test
	public void testGenerationFromModel() throws IOException {
		PoModelFx testPoGenericFx = getTestPoGenericFx();
		Map<String, String> generatePageObject = generatorService
				.generatePageObjects(testPoGenericFx, "./test");
		Assert.assertEquals(2, generatePageObject.size());
	}

	private PoModelFx getTestPoGenericFx() {
		PoModelFx model = new PoModelFx();
		PoGenericFx welcomePo = new PoGenericFx("WelcomePo", "testTemp");
		PoGenericFx listPo = new PoGenericFx("listPo", "testTemp");
		model.getPoGenericsFx().add(welcomePo);
		model.getPoGenericsFx().add(listPo);
		TransitionFx bEnter = new TransitionFx("enterButton", Selector.LINK,
				"bEnter", listPo);
		ElementFx h1 = new ElementFx("identefierHi", Selector.XPATH, "//h1");
		TransitionFx index = new TransitionFx("ToDo-App", Selector.LINK,
				"ToDo-App", welcomePo);
		welcomePo.getTransitionsFx().add(bEnter);
		welcomePo.getElementsFx().add(h1);
		listPo.getTransitionsFx().add(index);
		return model;
	}

}
