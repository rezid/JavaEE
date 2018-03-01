package fr.upec.m2.projects.JavaEE.utils;


import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MessageDigests {

    private MessageDigests() {
    }


    public static MessageDigest getMessageDigestInstance(String algorithm) throws UncheckedNoSuchAlgorithmException {
        try {
            return MessageDigest.getInstance(algorithm);
        }
        catch (NoSuchAlgorithmException e) {
            throw new UncheckedNoSuchAlgorithmException(e);
        }
    }

    public static byte[] digest(String message, String algorithm) throws UncheckedNoSuchAlgorithmException {
        return digest(message, UTF_8, algorithm);
    }

    public static byte[] digest(String message, Charset charset, String algorithm) throws UncheckedNoSuchAlgorithmException {
        return digest(message.getBytes(charset), algorithm);
    }

    public static byte[] digest(String message, byte[] salt, String algorithm) throws UncheckedNoSuchAlgorithmException {
        return digest(message, UTF_8, salt, algorithm);
    }

    public static byte[] digest(String message, Charset charset, byte[] salt, String algorithm) throws UncheckedNoSuchAlgorithmException {
        return digest(message.getBytes(charset), salt, algorithm);
    }

    public static byte[] digest(byte[] message, String algorithm) throws UncheckedNoSuchAlgorithmException {
        return getMessageDigestInstance(algorithm).digest(message);
    }

    public static byte[] digest(byte[] message, byte[] salt, String algorithm) throws UncheckedNoSuchAlgorithmException {
        MessageDigest messageDigest = getMessageDigestInstance(algorithm);

        messageDigest.update(salt);

        return messageDigest.digest(message);
    }
}
