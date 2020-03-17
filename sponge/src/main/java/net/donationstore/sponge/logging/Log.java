package net.donationstore.sponge.logging;

public class Log {

    public static void toConsole(String log) {
        System.out.println(String.format("[Donation Store]: %s", log));
    }

}
