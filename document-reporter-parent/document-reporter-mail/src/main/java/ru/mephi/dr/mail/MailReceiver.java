package ru.mephi.dr.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mephi.dr.mail.util.MyReceivedDateTerm;
import ru.mephi.dr.model.util.FileUtils;

public class MailReceiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

	private final String username;
	private final String password;
	private final Properties mailProps;

	/**
	 * Create instance and read properties into memory.
	 * 
	 * @param username
	 *            - username on mail server
	 * @param password
	 *            - password on mail server
	 * @param propertyLocation
	 *            - location of property file with mail connection properties
	 * @throws IOException
	 *             if some troubles with property file
	 */
	public MailReceiver(String username, String password, String propertyLocation) throws IOException {
		super();
		this.username = username;
		this.password = password;
		mailProps = new Properties();
		try (InputStream is = new FileInputStream(propertyLocation)) {
			mailProps.load(is);
		}
	}

	/**
	 * Load all doc(x) attachments from mail starting from <b>startLoadDate</b>
	 * and save it to <b>folder</b>
	 * 
	 * @param startLoadDate
	 *            mails before this date are omitted
	 * @param folder
	 *            save folder for documents
	 * @return date of last mail with attachment
	 * @throws MessagingException
	 *             if some troubles with mail receiving
	 * @throws IOException
	 *             if some troubles with document writing
	 */
	public Date load(Date startLoadDate, String folder) throws MessagingException, IOException {
		Store store = connect();
		Date lad = saveFiles(store, startLoadDate, folder);
		return lad;
	}

	/**
	 * Test establishing of connection
	 * 
	 * @throws MessagingException
	 *             if can't establish connection
	 */
	public void testConnection() throws MessagingException {
		Store s = connect();
		s.close();
	}

	private Store connect() throws MessagingException {
		Session session = Session.getDefaultInstance(mailProps, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		Store store = session.getStore();
		store.connect("smtp.gmail.com", username, password);
		return store;
	}

	private Date saveFiles(Store store, Date startLoadDate, String folderName) throws MessagingException, IOException {
		Folder mailFolder = store.getFolder("INBOX");
		mailFolder.open(Folder.READ_ONLY);
		// first term receives all younger this date (not time)
		SearchTerm dateTerm = new ReceivedDateTerm(ComparisonTerm.GE, startLoadDate);
		// second term really sorts by time
		MyReceivedDateTerm myDateTerm = new MyReceivedDateTerm(ComparisonTerm.GT, startLoadDate);
		SearchTerm term = new AndTerm(dateTerm, myDateTerm);
		Message[] sResult = mailFolder.search(term);
		File folder = new File(folderName);
		FileUtils.checkDirectory(folder);
		Date resultDate = startLoadDate;
		int received = 0;
		for (Message message : sResult) {
			if (StringUtils.startsWith(message.getContentType(), "multipart")) {
				Multipart multipart = (Multipart) message.getContent();

				for (int i = 0; i < multipart.getCount(); i++) {
					BodyPart bodyPart = multipart.getBodyPart(i);
					if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())
							|| !StringUtils.isNotBlank(bodyPart.getFileName())) {
						continue; // dealing with attachments only
					}
					if (!bodyPart.getFileName().matches(".*\\.docx?")) {
						// doc(x) files only
						continue;
					}
					InputStream is = bodyPart.getInputStream();
					File saved = FileUtils.createFile(is, folder, bodyPart.getFileName());
					Date rd = message.getReceivedDate();
					if (resultDate.before(rd)) {
						resultDate = rd;
					}
					LOGGER.debug("Saved {} file", saved);
					received++;
				}
			}
		}
		LOGGER.info("Totally received {} files. Newest attachment: {}", received, resultDate);
		return resultDate;
	}

}
