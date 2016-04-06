package de.muenchen.selenipo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import de.muenchen.selenipo.config.Config;
import de.muenchen.selenipo.po.BasePo;
import de.muenchen.selenipo.po.detailPage;
import de.muenchen.selenipo.po.listPage;
import de.muenchen.selenipo.po.newPage;
import de.muenchen.selenipo.po.welcomePage;

/**
 * Unit test for simple App.
 */
@org.junit.runner.RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class })
public class AppTest {

	@Autowired
	public BasePo po;

	/**
	 * Rigourous Test :-)
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testApp() throws InterruptedException {
		welcomePage wp = new welcomePage(po);
		listPage clickAENTER = wp.clickAENTER();
		newPage clickANew = clickAENTER.clickANew();
		clickANew.getITodotitle().sendKeys("testTitle");
		clickANew.getITodonotes().sendKeys("testNote");
		detailPage clickBCreateTodo = clickANew.clickBCreateTodo();
	}
}
