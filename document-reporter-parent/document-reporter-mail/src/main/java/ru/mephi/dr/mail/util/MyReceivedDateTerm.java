package ru.mephi.dr.mail.util;

import java.util.Date;

import javax.mail.Message;
import javax.mail.search.DateTerm;

public final class MyReceivedDateTerm extends DateTerm {

    private static final long serialVersionUID = -2756695246195503170L;

    /**
     * Constructor.
     *
     * @param comparison	the Comparison type
     * @param date		the date to be compared
     */
    public MyReceivedDateTerm(int comparison, Date date) {
	super(comparison, date);
    }

    /**
     * The match method.
     *
     * @param msg	the date comparator is applied to this Message's
     *			received date
     * @return		true if the comparison succeeds, otherwise false
     */
    public boolean match(Message msg) {
	Date d;

	try {
	    d = msg.getReceivedDate();
	} catch (Exception e) {
	    return false;
	}

	if (d == null)
	    return false;

	return super.match(d);
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof MyReceivedDateTerm))
	    return false;
	return super.equals(obj);
    }
}