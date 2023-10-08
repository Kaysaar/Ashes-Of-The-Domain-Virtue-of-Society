package org.random.rjgodoy.trng;

import java.security.SecureRandom;

/**
 * Values for the {@link MH_SecureRandom#INSTANCE_MODE org.random.rjgodoy.trng.mode} property.<P>
 *
 * Depending on the intended use of the generator, it may or may be not be acceptable using non-private random bits
 * (e.g., if the generator is used for private key generation). Therefore, two operation modes are provided:
 *
 * The generator may return random bits as obtained from random.org ({@link #TRNG} mode)
 * or it may xor them with random bits from a local PRNG ({@link #TRNG_XOR_PRNG} mode).
 *
 * @author Javier Godoy
 */
public enum GeneratorMode {

	/** The RNG will return true random numbers from the remote source.	 *
	 * This mode is recommended if you trust in the remote source.*/
  TRNG,

	/** The RNG will obtain true random numbers from the remote source,
	 *  and will XOR them with pseudo-random numbers obtained from a local PRNG.
	 *  This mode is recommended if you are paranoiac about the remote source.<P>
	 *  The PRNG must be seed by the application code, by calling {@link SecureRandom#setSeed(byte[])} on the instantiated <tt>SecureRandom</tt> object.
	 *
	 */
	TRNG_XOR_PRNG;

}
