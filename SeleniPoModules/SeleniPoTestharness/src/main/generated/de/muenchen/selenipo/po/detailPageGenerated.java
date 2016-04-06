
package de.muenchen.selenipo.po;

import de.muenchen.selenipo.po.BasePo;
import de.muenchen.selenipo.po.Control;
import de.muenchen.selenipo.po.PageObject;
import de.muenchen.selenipo.po.listPage;

public class detailPageGenerated extends BasePo {

	public final Control aBack = control(by.link("bBack"));

	public detailPageGenerated(PageObject po) {
		super(po);
	}
 
	
	public listPage clickABack() {
		aBack.click();
		return new listPage(this);
	}
	
 
	/**
	 * Get the Control for the Transition aBack.
	 * @return aBack - Transition
	 */
	public Control getABack() {
		return aBack;
	}
	

}
