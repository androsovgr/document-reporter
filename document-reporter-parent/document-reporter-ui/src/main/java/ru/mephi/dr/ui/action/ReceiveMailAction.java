package ru.mephi.dr.ui.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.xml.bind.JAXBException;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mephi.dr.configuration.MainConfiguration;
import ru.mephi.dr.configuration.SortingConfigurations;
import ru.mephi.dr.configuration.util.ConfigFolderResolver;
import ru.mephi.dr.mail.MailReceiver;
import ru.mephi.dr.mail.MailSorter;

public class ReceiveMailAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveMailAction.class);

	private final MainConfiguration mc;
	private final SortingConfigurations sc;

	public ReceiveMailAction() throws ConfigurationException, FileNotFoundException, JAXBException {
		mc = new MainConfiguration();
		sc = new SortingConfigurations();
	}

	public void action() throws IOException, MessagingException, ConfigurationException {
		LOGGER.info("Started receive operation");
		String emailPropertyLocation = new File(ConfigFolderResolver.getConfigFolder(),
				"mail/" + mc.getEmailType() + ".properties").getAbsolutePath();
		MailReceiver mr = new MailReceiver(mc.getEmailLogin(), mc.getEmailPassword(), emailPropertyLocation);
		Date lastLoaded = mr.load(mc.getLastLodadedFileDate(), mc.getWorkAllFolder().getAbsolutePath());
		mc.setLastLodadedFileDate(lastLoaded);
		MailSorter ms = new MailSorter(mc.getWorkAllFolder(), mc.getWorkSortedFolder());
		ms.sort(sc.getAllSortingRules());
	}
}
