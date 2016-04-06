package de.muenchen.selenipo;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import javafx.collections.ObservableList;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.AssertThrows;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import de.muenchen.selenipo.config.ConverterConfig;
import de.muenchen.selenipo.impl.fxModel.ElementFx;
import de.muenchen.selenipo.impl.fxModel.PoGenericFx;
import de.muenchen.selenipo.impl.fxModel.PoModelFx;
import de.muenchen.selenipo.impl.fxModel.TransitionFx;
import de.muenchen.selenipo.impl.persistanceModel.ElementImpl;
import de.muenchen.selenipo.impl.persistanceModel.PoGenericImpl;
import de.muenchen.selenipo.impl.persistanceModel.PoModelImpl;
import de.muenchen.selenipo.impl.persistanceModel.TransitionImpl;

@org.junit.runner.RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConverterConfig.class, loader = AnnotationConfigContextLoader.class)
public class ConverterServiceTest {

	@Autowired
	ConverterService converterService;

	@Test
	public void saveAndLoad() {
		File fileSave = new File("test.xml");
		PoModel testModel = getTestPoGenericImpl();
		converterService.persistToXml(fileSave, testModel);
		File fileLoad = new File("test.xml");
		PoModel loaded = (PoModel) converterService.loadFromXml(fileLoad);
		assertEquals(testModel, loaded);
	}

	@Test
	public void convertToFx() {
		PoModelImpl testModel = getTestPoGenericImpl();
		PoModelFx fxModel = converterService.convertToFxModel(testModel);

		Assert.assertThat(fxModel, CoreMatchers.instanceOf(PoModelFx.class));

		List<PoGeneric> poGenerics = testModel.getPoGenerics();
		ObservableList<PoGenericFx> poGenericsFx = fxModel.getPoGenericsFx();
		Assert.assertEquals(poGenerics.size(), poGenericsFx.size());

		for (int i = 0; i < poGenerics.size(); i++) {
			PoGeneric poGeneric = poGenerics.get(i);
			PoGenericFx poGenericFx = poGenericsFx.get(i);
			Assert.assertThat(poGenericFx,
					CoreMatchers.instanceOf(PoGenericFx.class));
			Assert.assertEquals(poGeneric.getIdentifier(),
					poGenericFx.getIdentifier());
			Assert.assertEquals(poGeneric.getPackageName(),
					poGenericFx.getPackageName());

			List<Element> elements = poGeneric.getElements();
			List<Element> elementsFx = poGenericFx.getElements();
			Assert.assertEquals(elements.size(), elementsFx.size());

			for (int j = 0; j < elements.size(); j++) {
				Element element = elements.get(j);
				Element elementFx = elementsFx.get(j);
				Assert.assertThat(elementFx,
						CoreMatchers.instanceOf(ElementFx.class));
				Assert.assertEquals(element.getIdentifier(),
						elementFx.getIdentifier());
				Assert.assertEquals(element.getLocator(),
						elementFx.getLocator());
				Assert.assertEquals(element.getType(), elementFx.getType());
			}

			List<Transition> transitions = poGeneric.getTransitions();
			List<Transition> transitionsFx = poGenericFx.getTransitions();
			Assert.assertEquals(transitions.size(), transitionsFx.size());

			for (int k = 0; k < elements.size(); k++) {
				Transition transition = transitions.get(k);
				Transition transitionFx = transitionsFx.get(k);
				Assert.assertThat(transitionFx,
						CoreMatchers.instanceOf(TransitionFx.class));
				Assert.assertEquals(transition.getIdentifier(),
						transitionFx.getIdentifier());
				Assert.assertEquals(transition.getLocator(),
						transitionFx.getLocator());
				Assert.assertEquals(transition.getType(),
						transitionFx.getType());
				Assert.assertEquals(transition.getType(),
						transitionFx.getType());
				Assert.assertEquals(
						transition.getDestination().getIdentifier(),
						transitionFx.getDestination().getIdentifier());
			}
		}
	}

	@Test
	public void convertToImpl() {
		PoModelFx testModel = getTestPoGenericFx();
		PoModelImpl implModel = converterService.convertToImpl(testModel);

		Assert.assertThat(implModel, CoreMatchers.instanceOf(PoModelImpl.class));

		List<PoGeneric> poGenerics = testModel.getPoGenerics();
		List<PoGeneric> poGenericsImpl = implModel.getPoGenerics();
		Assert.assertEquals(poGenerics.size(), poGenericsImpl.size());

		for (int i = 0; i < poGenerics.size(); i++) {
			PoGeneric poGeneric = poGenerics.get(i);
			PoGeneric poGenericImpl = poGenericsImpl.get(i);
			Assert.assertThat(poGenericImpl,
					CoreMatchers.instanceOf(PoGenericImpl.class));
			Assert.assertEquals(poGeneric.getIdentifier(),
					poGenericImpl.getIdentifier());
			Assert.assertEquals(poGeneric.getPackageName(),
					poGenericImpl.getPackageName());

			List<Element> elements = poGeneric.getElements();
			List<Element> elementsImpl = poGenericImpl.getElements();
			Assert.assertEquals(elements.size(), elementsImpl.size());

			for (int j = 0; j < elements.size(); j++) {
				Element element = elements.get(j);
				Element elementImpl = elementsImpl.get(j);
				Assert.assertThat(elementImpl,
						CoreMatchers.instanceOf(ElementImpl.class));
				Assert.assertEquals(element.getIdentifier(),
						elementImpl.getIdentifier());
				Assert.assertEquals(element.getLocator(),
						elementImpl.getLocator());
				Assert.assertEquals(element.getType(), elementImpl.getType());
			}

			List<Transition> transitions = poGeneric.getTransitions();
			List<Transition> transitionsImpl = poGenericImpl.getTransitions();
			Assert.assertEquals(transitions.size(), transitionsImpl.size());

			for (int k = 0; k < elements.size(); k++) {
				Transition transition = transitions.get(k);
				Transition transitionImpl = transitionsImpl.get(k);
				Assert.assertThat(transitionImpl,
						CoreMatchers.instanceOf(TransitionImpl.class));
				Assert.assertEquals(transition.getIdentifier(),
						transitionImpl.getIdentifier());
				Assert.assertEquals(transition.getLocator(),
						transitionImpl.getLocator());
				Assert.assertEquals(transition.getType(),
						transitionImpl.getType());
				Assert.assertEquals(transition.getType(),
						transitionImpl.getType());
				Assert.assertEquals(
						transition.getDestination().getIdentifier(),
						transitionImpl.getDestination().getIdentifier());
			}
		}
	}

	@Test
	public void testForDuplecateKeys() {
		PoModelFx model = getTestPoGenericFxWithDuplecateKeys();
		List<ValidationMessage> validateModel = converterService.validateModel(model);
		Assert.assertEquals(validateModel.size(), 4);
	}

	private PoModelImpl getTestPoGenericImpl() {
		PoModelImpl model = new PoModelImpl();
		PoGeneric welcomePo = new PoGenericImpl("WelcomePo", "basePackage");
		PoGeneric listPo = new PoGenericImpl("listPo", "basePackage");
		model.getPoGenerics().add(welcomePo);
		model.getPoGenerics().add(listPo);
		Transition bEnter = new TransitionImpl("enterButton", Selector.LINK,
				"bEnter", listPo);
		ElementImpl h1 = new ElementImpl("hi", Selector.XPATH, "//h1");
		TransitionImpl index = new TransitionImpl("toDo-App", Selector.LINK,
				"ToDo-App", welcomePo);
		welcomePo.getTransitions().add(bEnter);
		welcomePo.getElements().add(h1);
		listPo.getTransitions().add(index);
		return model;
	}

	private PoModelFx getTestPoGenericFx() {
		PoModelFx model = new PoModelFx();
		PoGenericFx welcomePo = new PoGenericFx("WelcomePo", "basePackage");
		PoGenericFx listPo = new PoGenericFx("listPo", "basePackage");
		model.getPoGenericsFx().add(welcomePo);
		model.getPoGenericsFx().add(listPo);
		TransitionFx bEnter = new TransitionFx("enterButton", Selector.LINK,
				"bEnter", listPo);
		ElementFx h1 = new ElementFx("hi", Selector.XPATH, "//h1");
		TransitionFx index = new TransitionFx("toDo-App", Selector.LINK,
				"toDo-App", welcomePo);
		welcomePo.getTransitionsFx().add(bEnter);
		welcomePo.getElementsFx().add(h1);
		listPo.getTransitionsFx().add(index);
		return model;
	}

	private PoModelFx getTestPoGenericFxWithDuplecateKeys() {
		PoModelFx model = new PoModelFx();
		PoGenericFx welcomePo = new PoGenericFx("WelcomePo", "basePackage");
		PoGenericFx welcomePo2 = new PoGenericFx("WelcomePo", "basePackage");
		model.getPoGenericsFx().add(welcomePo);
		model.getPoGenericsFx().add(welcomePo2);
		TransitionFx bEnter1 = new TransitionFx("key1", Selector.LINK,
				"bEnter", welcomePo);
		TransitionFx bEnter2 = new TransitionFx("key2", Selector.LINK,
				"bEnter", welcomePo);
		TransitionFx bEnter3 = new TransitionFx("key2", Selector.LINK,
				"bEnter", welcomePo);
		TransitionFx bEnter4 = new TransitionFx("key2", Selector.LINK,
				"bEnter", welcomePo);
		ElementFx h1 = new ElementFx("key1", Selector.XPATH, "//h1");
		ElementFx h2 = new ElementFx("key3", Selector.XPATH, "//h1");
		ElementFx h3 = new ElementFx("key3", Selector.XPATH, "//h1");
		ElementFx h4 = new ElementFx("key4", Selector.XPATH, "//h1");
		welcomePo.getTransitionsFx().add(bEnter1);
		welcomePo.getTransitionsFx().add(bEnter2);
		welcomePo.getTransitionsFx().add(bEnter3);
		welcomePo.getTransitionsFx().add(bEnter4);
		welcomePo.getElementsFx().add(h1);
		welcomePo.getElementsFx().add(h2);
		welcomePo.getElementsFx().add(h3);
		welcomePo.getElementsFx().add(h4);
		return model;
	}
}
