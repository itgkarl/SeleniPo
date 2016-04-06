
package de.muenchen.selenipo.po;

import de.muenchen.selenipo.po.BasePo;
import de.muenchen.selenipo.po.Control;
import de.muenchen.selenipo.po.PageObject;
import de.muenchen.selenipo.po.listPage;

public class welcomePageGenerated extends BasePo {

	public final Control aENTER = control(by.link("bEnter"));

	public welcomePageGenerated(PageObject po) {
		super(po);
	}
 
	
	public listPage clickAENTER() {
		aENTER.click();
		return new listPage(this);
	}
	
 
	/**
	 * Get the Control for the Transition aENTER.
	 * @return aENTER - Transition
	 */
	public Control getAENTER() {
		return aENTER;
	}
	

}
