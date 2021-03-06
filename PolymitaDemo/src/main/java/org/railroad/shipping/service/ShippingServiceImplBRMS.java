package org.railroad.shipping.service;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.inject.Inject;

import org.drools.runtime.StatefulKnowledgeSession;
import org.mortbay.log.Log;
import org.railroad.shipping.BillOfLading;
import org.railroad.shipping.Container;
import org.railroad.shipping.Item;
import org.railroad.shipping.Shipment;
import org.railroad.shipping.util.BRMSUtil;

/**
 * This class creates the StatefulKnowledgeSession and interacts with BRMS.
 * 
 * @author rowagner
 * 
 */
@Stateful
public class ShippingServiceImplBRMS implements ShippingService, Serializable {

	private static final long serialVersionUID = 6821952169434330759L;

	@Inject
	private BRMSUtil brmsUtil;

	/**
	 * Creates a new instance of the ShippingServiceImplBRMS.
	 */
	public ShippingServiceImplBRMS() {

	}

	public StatefulKnowledgeSession priceShipment(Shipment s) {

		if (s != null) {

			StatefulKnowledgeSession ksession = null;

			try {

				// If there is at least one container in the shipment.
				if (s.getContainersList().size() > 0) {

					ksession = brmsUtil.getStatefulSession();

					BillOfLading bol = new BillOfLading();
					bol = s.getBillOfLadingParent();
					bol.setState("start-process");
					s.setState("start-process");
					ksession.insert(bol);
					ksession.insert(s);

					for (Container c : s.getContainersList()) {
						c.setState("start-process");
						ksession.insert(c);

						for (Item i : c.getItemsList()) {
							i.setState("start-process");
							ksession.insert(i);
						}

					}

					ksession.startProcess("org.railroad.shipping.MasterFlow");
					
					Thread rules = new Thread(new ksessionRuleRunnable(ksession));
					rules.start();

				}

				/**
				 * Check price to see if the flow worked!
				 */

				for (Container c : s.getContainersList()) {
					for (Item i : c.getItemsList()) {
						System.out.println("Item Weight: " + i.getWeightLb());
						System.out.println("Item Price: $" + i.getPriceDollars());
					}
					System.out.println("Container Weight: " + c.getWeightLb());
					System.out.println("Container Price: $" + c.getPriceDollars());
				}

				System.out.println("Shipment Price: $" + s.getPriceDollars());

				System.out.println("BOL price: $" + s.getBillOfLadingParent().getPriceDollars());

			} catch(Exception e){
				Log.debug("Something went wrong with pricing the shipment");
				Log.debug(e);
			}
			
			return ksession;
		}
		//Only happens when the shipment is null
		return null;

	}
}

class ksessionRuleRunnable implements Runnable  {

	private StatefulKnowledgeSession ksession;
	
	public ksessionRuleRunnable(StatefulKnowledgeSession k){
		ksession = k;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ksession.fireUntilHalt();
		System.out.println("Process Halted");
		ksession.dispose();
	}
}