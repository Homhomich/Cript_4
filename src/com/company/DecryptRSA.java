package com.company;

import java.math.BigInteger;
import java.util.ArrayList;

public class DecryptRSA {
    private final long n;
    private final int e;
    private final String text;

    public DecryptRSA(long n, int e, String text) {
        this.n = n;
        this.e = e;
        this.text = text;
    }

    public String decryption(){
        ArrayList<Long> blocks = getBlocks();
        for (Long block: blocks) {
            System.out.println(block);
        }
        System.out.println();

        ArrayList<Long> decryptBlocks = toDecryptBlocks(getBlocks());
        for (Long block: decryptBlocks) {
            System.out.println(block);
        }
        return toChars(makeLineFromBlocks(decryptBlocks));
    }

    public String toChars(String decryptString) {
        StringBuilder result = new StringBuilder();
        while (decryptString.length() > 2) {
            char chars = (char) Integer.parseInt(decryptString.substring(0,2));
            result.append(chars);
            decryptString = decryptString.substring(2);
        }
        if (!decryptString.equals("")) {
            char chars = (char) Integer.parseInt(decryptString);
            result.append(chars);
        }
        return result.toString();
    }


    public String makeLineFromBlocks(ArrayList<Long> decryptBlocks) {
        StringBuilder result = new StringBuilder();
        for (Long decryptBlock : decryptBlocks) {
            result.append(decryptBlock);
        }
        return result.toString();
    }


    public ArrayList<Long> toDecryptBlocks(ArrayList<Long> encryptBlocks) {
        ArrayList<Long> decryptBlocks = new ArrayList<>();

        N pq = getValues();
        long phi = (pq.getP() - 1) * (pq.getQ() - 1);
        BigInteger e = new BigInteger(String.valueOf(this.e));
        BigInteger phiN = new BigInteger(String.valueOf(phi));
        BigInteger d = e.modInverse(phiN);

        for (Long block : encryptBlocks) {
            BigInteger decryptBlock = new BigInteger(String.valueOf(block));
            BigInteger n = new BigInteger(String.valueOf(this.n));
            decryptBlocks.add(decryptBlock.modPow(d, n).longValue());
        }

        return decryptBlocks;
    }

    public N getValues() {
        long val = 1;

        long x = Math.round(Math.sqrt(this.n));
        long y = ((x + val) * (x + val) - this.n);

        while ((long) Math.sqrt(y) * Math.sqrt(y) - y != 0) {
            val++;
            y = ((x + val) * (x + val) - this.n);
        }

        long a = x + val;
        long b = (long) Math.sqrt(y);

        return new N((a + b), (a - b));
    }

    public ArrayList<Long> getBlocks() {
        String text = this.text;
        int lengthN = String.valueOf(this.n).length();
        ArrayList<Long> blocks = new ArrayList<>();

        int currentTextBeginIndex;

        while (text.length() >= lengthN) {
            String block = text.substring(0, lengthN);

            if (Long.parseLong(block) <= this.n) {
                blocks.add(Long.valueOf(block));
                currentTextBeginIndex = lengthN;
            } else {
                block = block.substring(0, block.length() - 1);
                blocks.add(Long.valueOf(block));
                currentTextBeginIndex = (lengthN - 1);
            }

            text = text.substring(currentTextBeginIndex);

        }
        if (!text.equals("")) blocks.add(Long.valueOf(text));
        return blocks;

    }
}

