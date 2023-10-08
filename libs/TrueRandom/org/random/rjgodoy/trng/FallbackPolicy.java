package org.random.rjgodoy.trng;

import java.security.SecureRandom;

/**
 * Values for the {@link MH_SecureRandom#INSTANCE_FALLBACK org.random.rjgodoy.trng.fallback} property.<P>
 *
 * When the quota is exhausted, the generator may fallback to use a local PRNG,
 * or it may wait (several minutes) until the quota is positive again.
 *
 * @author Javier Godoy
 */
public enum FallbackPolicy {

	/** The RNG will return true random numbers from the remote source if the quota is positive,
	 * and will sleep if the quota is negative.*/
  TRNG,

	/** The RNG will return true random numbers from the remote source if the quota is positive,
	 * and will use a local PRNG if the quota is negative.<P>
	 * The PRNG must be seed by the application code, by calling {@link SecureRandom#setSeed(byte[])} on the instantiated <tt>SecureRandom</tt> object.
   */
	PRNG;

}
