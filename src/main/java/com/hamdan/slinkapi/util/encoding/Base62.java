package com.hamdan.slinkapi.util.encoding;

public class Base62 {

    private static final String CHAR_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = CHAR_SET.length();

    public static String encode(long num) {
        StringBuilder sb = new StringBuilder();

        if (num == 0) return "0";

        while (num > 0) {
            sb.append(CHAR_SET.charAt((int)(num % BASE)));
            num /= BASE;
        }

        return sb.reverse().toString();
    }

    public static long decode(String str) {
        long num = 0;

        for (char c : str.toCharArray())
            num = num * BASE + CHAR_SET.indexOf(c);

        return num;
    }

}
