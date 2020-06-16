package net.donationstore.logging;

public class Logging {

    public static String enableLog() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append("  _\n");
        builder.append(" | | \n");
        builder.append("/ __)\tDonation Store - v%s\n");
        builder.append("\\__ \\\t%s - https://donationstore.net/support\n");
        builder.append("(   /\tPlugin Enabled.\n");
        builder.append(" |_| \n");
        builder.append(" ");
        return builder.toString();
    }
}
