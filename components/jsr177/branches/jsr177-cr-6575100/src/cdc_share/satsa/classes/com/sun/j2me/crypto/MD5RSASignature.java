/*
 *
 * Copyright  1990-2007 Sun Microsystems, Inc. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License version
 * 2 only, as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License version 2 for more details (a copy is
 * included at /legal/license.txt).
 * 
 * You should have received a copy of the GNU General Public License
 * version 2 along with this work; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 * 
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa
 * Clara, CA 95054 or visit www.sun.com if you need additional
 * information or have any questions.
 */

package com.sun.j2me.crypto;

import java.security.SignatureSpi;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.sun.satsa.crypto.RSAPublicKey;

/**
 * Signature Service Provider Interface class for MD5 hashing used with RSA signing
 */
public class MD5RSASignature extends SignatureSpi {
    /**
     * Expected prefix in the decrypted result when MD5 hashing is used
     * with RSA signing. This prefix is followed by the MD5 hash.
     */
    private static final byte[] PREFIX_MD5 = {    
        (byte) 0x30, (byte) 0x20, (byte) 0x30, (byte) 0x0c,
        (byte) 0x06, (byte) 0x08, (byte) 0x2a, (byte) 0x86,
        (byte) 0x48, (byte) 0x86, (byte) 0xf7, (byte) 0x0d,
        (byte) 0x02, (byte) 0x05, (byte) 0x05, (byte) 0x00,
        (byte) 0x04, (byte) 0x10
    };

    /** Common signature class. */
    RSASignature rsaSig;

    /**
     * Constructs an RSA signature object that uses MD5 as 
     * message digest algorithm.
     *
     * @exception RuntimeException if MD5 is not available
     */
    public MD5RSASignature()
    {
        try {
            rsaSig = new RSASignature(PREFIX_MD5, MessageDigest.getInstance("MD5"));
        } catch (com.sun.j2me.crypto.NoSuchAlgorithmException e) {
            throw new RuntimeException("Needed algorithm not available");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Needed algorithm not available");
        }
    }

    /**
     * Updates the data to be signed or verified, using the 
     * specified array of bytes, starting at the specified offset.
     *
     * @param b the array of bytes  
     * @param off the offset to start from in the array of bytes 
     * @param len the number of bytes to use, starting at offset
     *
     * @exception SignatureException if the engine is not initialized 
     * properly
     */
    protected void engineUpdate(byte[] b, int off, int len) 
        throws SignatureException {
        rsaSig.update(b, off, len);
    }

    /**
     * Initializes this signature object with the specified
     * public key for verification operations.
     *
     * @param publicKey the public key of the identity whose signature is
     * going to be verified.
     * 
     * @exception InvalidKeyException if the key is improperly
     * encoded, parameters are missing, and so on.  
     */
    protected boolean engineVerify(byte[] sigBytes) 
        throws SignatureException {
        return rsaSig.verify(sigBytes, 0, sigBytes.length);
    }

    /**
     * Initializes this signature object with the specified
     * public key for verification operations.
     * 
     * @param publicKey the public key of the identity whose signature is
     * going to be verified.
     * 
     * @exception InvalidKeyException if the key is improperly
     * encoded, parameters are missing, and so on.  
     */
    protected void engineInitVerify(PublicKey publicKey)
	    throws InvalidKeyException {

        if (!(publicKey instanceof RSAPublicKey)){
            throw new InvalidKeyException();
        }

        try {
            rsaSig.initVerify(((RSAPublicKey)publicKey).getKey());
        } catch (com.sun.j2me.crypto.InvalidKeyException e) {
            throw new InvalidKeyException();
        }
    }

    protected byte[] engineSign() throws SignatureException {
        return null;
    }

    protected void engineUpdate(byte b) throws SignatureException {
    }

    protected void engineInitSign(PrivateKey privateKey)
	    throws InvalidKeyException {
    }
}
