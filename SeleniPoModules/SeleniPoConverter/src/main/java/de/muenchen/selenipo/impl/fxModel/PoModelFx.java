package de.muenchen.selenipo.impl.fxModel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import de.muenchen.selenipo.PoGeneric;
import de.muenchen.selenipo.PoModel;

public class PoModelFx implements PoModel {

	ObservableList<PoGenericFx> poGenericsFx;

	public PoModelFx() {
		super();
		Callback<PoGenericFx, Observable[]> extractor = new Callback<PoGenericFx, Observable[]>() {
			@Override
			public Observable[] call(PoGenericFx po) {
				return new Observable[] { po.identifierProperty() };
			}
		};
		poGenericsFx = FXCollections.observableArrayList(extractor);
	}

	@Override
	public List<PoGeneric> getPoGenerics() {
		List<PoGeneric> poGen = new ArrayList<PoGeneric>();
		for (PoGenericFx poGenericFx : poGenericsFx) {
			poGen.add(poGenericFx);
		}
		return poGen;
	}

	public ObservableList<PoGenericFx> getPoGenericsFx() {
		return poGenericsFx;
	}

	public void setPoGenericsFx(ObservableList<PoGenericFx> poGenericsFx) {
		this.poGenericsFx = poGenericsFx;
	}

}
