package com.studying.udemy.restapi.utils;

import com.studying.udemy.restapi.exceptions.MissingRequiredFieldException;
import com.studying.udemy.restapi.shared.dto.UserDTO;
import com.studying.udemy.restapi.ui.model.response.ErrorMessages;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

public class UserProfileUtils {
    private final Random random = new SecureRandom();
    private final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final int iterations = 10000;
    private final int keyLength = 256;

    public void validateRequiredFields(UserDTO user) throws MissingRequiredFieldException {
        if (StringUtils.isBlank(user.getFirstName()) ||
            StringUtils.isBlank(user.getLastName()) ||
            StringUtils.isBlank(user.getEmail()) ||
            StringUtils.isBlank(user.getPassword())) {
            throw new MissingRequiredFieldException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }
    }

    public String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private String generateRandomString(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            returnValue.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        return returnValue.toString();
    }

    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    public String generateSalt(int length) {
        return generateRandomString(length);
    }

    private byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterations, keyLength);
        Arrays.fill(password, Character.MIN_VALUE);

        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return secretKeyFactory.generateSecret(keySpec).getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("Error while hashing a password " +e.getMessage(), e);
        } catch (InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password " +e.getMessage(), e);
        } finally {
            keySpec.clearPassword();
        }
    }

    public String generateSecurePassword(String password, String salt) {
        String returnValue = null;
        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
        returnValue = Base64.getEncoder().encodeToString(securePassword);

        return returnValue;
    }

    public byte[] encrypt(String securePassword, String accessTokenMaterial) {
        return hash(securePassword.toCharArray(), accessTokenMaterial.getBytes());
    }
}
