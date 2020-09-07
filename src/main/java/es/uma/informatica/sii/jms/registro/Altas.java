package es.uma.informatica.sii.jms.registro;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/Registro"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class Altas implements MessageListener {
	private static final Logger LOGGER = Logger.getLogger(Altas.class.getCanonicalName());
	
	@Inject
    private JMSContext contextoJMS;

	@Resource(lookup="java:/jms/topic/altausuario")
	private Topic difusionAlta;

	public void onMessage(Message mensaje) {
		try {
			if (mensaje instanceof MapMessage) {
				MapMessage usuario = (MapMessage) mensaje;
				LOGGER.info("Recibido mensaje para dar de alta a "+usuario.getString("cuenta"));
				// TODO: labores administrativas que tenga que hacer
				contextoJMS.createProducer().send(difusionAlta, usuario);
			}
		} catch (JMSException e) {
			LOGGER.severe("Error al recibir mensaje");
		}
	}

}
